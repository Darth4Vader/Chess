package ChessUIJavaFX;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
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
import java.awt.image.BufferedImage;
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
import JavaFXUtilities.JavaFXUtils;
import OtherUtilities.ImageUtils;
import SwingUtilities.SwingUtils;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
		this.arr = new ChessPositionPanel[RANK][FILE];
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
		RowConstraints rc = new RowConstraints();
		rc.setPercentHeight(100d / RANK);

		for (int i = 0; i < RANK; i++) {
		    this.getRowConstraints().add(rc);
		}

		ColumnConstraints cc = new ColumnConstraints();
		cc.setPercentWidth(100d / FILE);

		for (int i = 0; i < RANK; i++) {
		    this.getColumnConstraints().add(cc);
		}
		for(int rank = 0;rank < RANK; rank++) {
			for(int file = 0;file < FILE; file++) {
				this.arr[rank][file] = new ChessPositionPanel(chess.getChessPosition(rank, file), this);
				this.add(this.arr[rank][file], file, rank);
				/*if(!(rank == 7 && file >= 1)) {
					this.arr[rank][file] = new ChessPositionPanel(chess.getChessPosition(rank, file), this);
					this.add(this.arr[rank][file], file, rank);
				}
				else {
					ChessPosition position = chess.getChessPosition(rank, file);
					this.arr[rank][file] = new ChessPositionPanel(new ChessPosition(position.getBackgroundColor(), rank, file), this);
					this.add(new Pane(this.arr[rank][file]), file, rank);
				}*/
			}
		}
	}
	
	public void switchTurn(ChessMove move) {
		try {
			move.initiateChange();
		} catch (PromotionChooseException promotionException) {
			GridPane panel = new GridPane();
			panel.prefWidthProperty().bind(this.widthProperty().multiply(0.6));
			panel.prefHeightProperty().bind(this.heightProperty().multiply(0.4));
			/*panel.setOpaque(false);
			panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
			panel.setLayout(new GridLayout());*/
			//DialogPane dialog = new DialogPane();
			
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(this.getScene().getWindow());
			
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
						chessPieceImage.setOnMouseClicked((e) -> {
							promotionException.setPromotion(chessPieceImage.getType());
							dialog.close();
						});
						panel.getChildren().add(chessPieceImage);
					});
			dialog.setScene(new Scene(panel));
			dialog.show();
			//panel.add(new Label("ney wooo"));
			//panel.setSize(100, 100);
			//this.getLayeredPane().add(panel);
		}
		this.chess.switchTurn();
		if(!this.chess.isGameActivate())
			addVictoryPanel();
	}
	
	public void addVictoryPanel() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
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
		alert.setContentText(str);
		alert.show();
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
		return JavaFXUtils.getImageResource(ChessMain.class, path, false);
	}
}
