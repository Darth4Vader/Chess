package ChessUIJavaFX;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.border.Border;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import ChessDataTypes.Chess;
import ChessDataTypes.ChessData;
import ChessDataTypes.ChessException;
import ChessDataTypes.ChessMove;
import ChessDataTypes.ChessMove.PromotionChooseException;
import ChessDataTypes.ChessMoves;
import ChessDataTypes.ChessPiece;
import ChessDataTypes.ChessPosition;
import FileUtilities.FilesUtils;
import OtherUtilities.ImageUtils;
import SwingUtilities.SwingUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class ChessMain extends GridPane implements ChessData {
	
	private final Chess chess;
	private final ChessPositionPanel[][] arr;
	private final Color whiteColor, blackColor;
	
	public ChessMain() throws ChessException {
		this(new Chess());
	}
	
	public ChessMain(String FEN) throws ChessException {
		this(new Chess(FEN));
	}
	
	public ChessMain(Chess chess) {
		this.chess = chess;
		this.arr = new ChessPositionPanel[8][8];
		this.whiteColor = Color.rgb(255, 229, 204);
		this.blackColor = Color.rgb(255, 178, 102);
		this.loadChessBoard();
		this.setVisible(true);
	}
	
	public Color getWhiteColor() {
		return this.whiteColor;
	}
	
	public Color getBlackColor() {
		return this.blackColor;
	}
	
	public Chess getChessBoard() {
		return this.chess;
	}
	
	public ChessPositionPanel[][] getArr() {
		return this.arr;
	}
	
	public void loadChessBoard() {
		for(int rank = 0;rank < 8; rank++) {
			for(int file = 0;file < 8; file++) {
				this.arr[rank][file] = new ChessPositionPanel(chess.getChessPosition(rank, file), this);
				this.add(this.arr[rank][file], rank, file);
			}
		}
	}
	
	public void switchTurn(ChessMove move) {
		try {
			move.initiateChange();
		} catch (PromotionChooseException promotionException) {
			JPanel panel = new JPanel() {
				/*@Override
				public Dimension getSize() {
					return this.getPreferredSize();
				}*/
				@Override
				public Dimension getPreferredSize() {
					//System.out.println("Hello " + SwingUtils.getRatioSize(ChessMain.this, 0.4, 0.4));
                    Dimension size = ChessMain.this.getSize(); 
					return new Dimension((int) (size.width*0.6), (int) (size.height*0.4));
					//return SwingUtils.getRatioSize(ChessMain.this, 0.4, 0.4);
				}
			};
			panel.setOpaque(false);
			panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
			panel.setLayout(new GridLayout());
			JDialog dialog = new JDialog(this);
			Arrays.asList(Piece.QUEEN, Piece.BISHOP, Piece.KNIGHT, Piece.ROOK)
					.stream()
					.map((type) -> ChessData.getChessPieceClass(type))
					.map((piece) -> ChessPiece.newInstance(piece, move.getChess().getCurrentTurn()))
					.map((piece) -> new ChessPieceImage(piece) {
						/*@Override
						public Dimension getPreferredSize() {
							//System.out.println("Hello " + SwingUtils.getRatioSize(ChessMain.this, 0.4, 0.4));
                            Dimension size = ChessMain.this.getSize(); 
							return new Dimension((int) (size.width*0.4), (int) (size.height*0.4));
							//return SwingUtils.getRatioSize(ChessMain.this, 0.4, 0.4);
						}
						@Override
						public Dimension getSize() {
							return getPreferredSize();
						}*/
					})
					.forEach((chessPieceImage) -> {
						chessPieceImage.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								promotionException.setPromotion(chessPieceImage.getType());
								refreshFrame();
								dialog.dispose();
							}
						});
						panel.add(chessPieceImage);
					});
			dialog.setLayout(null);
			dialog.setContentPane(panel);
			dialog.pack();
			dialog.setVisible(true);
			//panel.add(new Label("ney wooo"));
			//panel.setSize(100, 100);
			//this.getLayeredPane().add(panel);
		}
		refreshFrame();
		this.chess.switchTurn();
		if(!this.chess.isGameActivate())
			addVictoryPanel();
	}
	
	public void addVictoryPanel() {
		DialogPane dialogPane = new DialogPane();
		JPanel panel = new JPanel() {
			/*@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				//System.out.println(getSize());
				//g.drawString(str + " Won", 0, 0);
			}*/
			@Override
			public Dimension getPreferredSize() {
				return ChessMain.this.getSize();
			}
		};
		panel.setLayout(new BorderLayout());
		panel.setOpaque(false);
		panel.setSize(panel.getPreferredSize());
		String str = "";
		TurnColor winner = chess.getWinner();
		if(winner == TurnColor.DRAW) 
			str = "Draw";
		else {
			if(winner == TurnColor.WHITE)
				str = "White";
			else
				if(winner == TurnColor.BLACK)
					str = "Black";
			str += " Won";
		}
		JLabel lbl = new JLabel(str, SwingConstants.CENTER);
		lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
		lbl.setAlignmentY(Component.CENTER_ALIGNMENT);
		panel.add(lbl);
		panel.setSize(100, 50);
		this.getLayeredPane().add(panel);
	}
	
	public ChessPositionPanel getChessPositionPanel(ChessPosition position) {
		return getChessPositionPanel(position.getRank(), position.getFile());
	}
	
	public ChessPositionPanel getChessPositionPanel(int rank, int file) {
		if(0 <= rank && rank < RANK && 0 <= file && file < FILE)
			return arr[rank][file];
		return null;
	}
	
	public static Image loadImage(String path) {
		return ImageUtils.loadImage(FilesUtils.readResource(ChessMain.class, path));
	}
}
