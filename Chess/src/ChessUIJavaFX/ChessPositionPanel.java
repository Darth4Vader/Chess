package ChessUIJavaFX;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseListener;
import java.util.List;

import javax.naming.InitialContext;
import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import ChessDataTypes.ChessData.TurnColor;
import ChessDataTypes.ChessMove;
import ChessDataTypes.ChessMoves;
import ChessDataTypes.ChessPiece;
import ChessDataTypes.ChessPosition;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class ChessPositionPanel extends BorderPane {
	
	private ChessMain board;
	private final ChessPosition position;
	private ChessPiecePanel piece;
	private boolean isPossible;
	
	public ChessPositionPanel(ChessPosition position, ChessMain board) {
		this.position = position;
		this.isPossible = false;
		this.board = board;
		TurnColor color = position.getBackgroundColor();
		this.setBackground(Background.fill(color == TurnColor.WHITE ? board.getWhiteColor() : board.getBlackColor()));
		this.setBorder(Border.stroke(Color.GRAY));
		this.setOnMousePressed(e -> {
            if(piece != null) {
                if(e.getButton() == MouseButton.SECONDARY)
                    piece.requestFocus();
            }
            else
                this.requestFocus();
        });
		this.getChildren().addListener((ListChangeListener<javafx.scene.Node>) c -> {
            while(c.next()) {
                if(c.wasAdded()) {
            		if(!board.getChessBoard().isGameActivate())
            			board.addVictoryPanel();
                }
            }
		});
		this.setPickOnBounds(false);
		//this.setOpacity(0.5);
		initiateChessPiecePanel();
		
		this.setOnMouseDragEntered(e -> {
			Object obj = e.getGestureSource();
			if(obj instanceof ChessPiecePanel) {
				ChessPiecePanel piece = (ChessPiecePanel)obj;
				ChessPositionPanel source = piece.getChessPositionPanel();
				if(!this.equals(source)) {
					prevBorder = this.getBorder();
					this.setBorder(Border.stroke(Color.BLUE
					//Color.DARKGRAY
					));
				}
			}
		});
		
		/*
		this.setOnMouseDragOver(e -> {
			dragEvent = e;
			Object obj = e.getGestureSource();
			if(obj instanceof ChessPiecePanel) {
				ChessPiecePanel piece = (ChessPiecePanel)obj;
				ChessPositionPanel source = piece.getChessPositionPanel();
				if(!this.equals(source)) {
					prevBorder = this.getBorder();
					this.setBorder(Border.stroke(Color.BLUE
					//Color.DARKGRAY
					));
				}
			}
		});*/
		
		
		this.setOnMouseDragExited(e -> {
			System.out.println("Thisssss");
			System.out.println(this);
			if(prevBorder != null)
				this.setBorder(prevBorder);
		});
		//this.setOnMouse
		/*this.setOnMouseReleased(e -> {
			System.out.println("SouRCE: " + e.getSource());
			if(dragEvent == null) return;
			Object obj = dragEvent.getGestureSource();
			dragEvent = null;
			if(obj instanceof ChessPiecePanel) {
				ChessPiecePanel piece = (ChessPiecePanel)obj;
				System.out.println("fool : " + obj + " " + this.getPosition());
				ChessMoves possibleMoves = piece.getPossibleMoves(); 
				boolean b = true;
				System.out.println(possibleMoves);
				if(possibleMoves != null) {
					List<ChessMove> list = possibleMoves.getPossibleMoves();
					if(list.size() != 0) {
						for(ChessMove move : list) {
							System.out.println("POS: " + move.getMoveToPosition() + " " + this.position);
							if(this.position.equals(move.getMoveToPosition())) {
								board.switchTurn(move);
								System.out.println("Boston");
								System.out.println();
								b = false;
								piece.checkUpdates();
								break;
							}
						}
						if(b)
							this.requestFocus();
					}
				}
			}
		});*/
		this.setOnMouseDragReleased(e -> {
			Object obj = e.getGestureSource();
			if(obj instanceof ChessPiecePanel) {
				ChessPiecePanel piece = (ChessPiecePanel)obj;
				System.out.println("fool : " + e.getGestureSource() + " " + this.getPosition());
				ChessMoves possibleMoves = piece.getPossibleMoves(); 
				boolean b = true;
				System.out.println(possibleMoves);
				if(possibleMoves != null) {
					List<ChessMove> list = possibleMoves.getPossibleMoves();
					if(list.size() != 0) {
						for(ChessMove move : list) {
							System.out.println("POS: " + move.getMoveToPosition() + " " + this.position);
							if(this.position.equals(move.getMoveToPosition())) {
								this.board.switchTurn(move);
								System.out.println("Boston");
								System.out.println();
								b = false;
								piece.checkUpdates();
								break;
							}
						}
						if(b)
							this.requestFocus();
					}
				}
			}
		});
		//this.setOnMouseDragOver(getOnDragDetected());
	}
	
	private MouseDragEvent dragEvent;
	private Border prevBorder;
	
	public void setIfPossible(boolean isPossible) {
		this.isPossible = isPossible;
	}
	
	/*
	@Override 
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(isPossible) {
			g.setColor(new Color(128, 128, 128, 150));
			if(piece == null) {
				int width = (int)(getWidth()*0.3);
				int height = (int)(getHeight()*0.3);
				int x = (int)((getWidth() - width)*0.5);
				int y = (int)((getHeight() - height)*0.5);
				g.drawOval(x, y, width, height);
				g.fillOval(x, y, width, height);
			}
			else {
				g.drawOval(0, 0, getWidth(), getHeight());
			}
		}
	}
	*/
	
	private void initiateChessPiecePanel() {
		ChessPiece piece = position.getChessPiece();
		if(piece != null) {
			this.piece = new ChessPiecePanel(piece, board);
			this.piece.setChessPositionPanel(this);
			System.out.println("Ready");
			this.setCenter(this.piece);
		}
		else
			this.piece = null;
	}
	
	public void setChessPiecePanel(ChessPiecePanel piece) {
		getChildren().clear();
		this.piece = piece;
		if(this.piece != null)
			this.setCenter(this.piece);
	}
	
	public ChessPosition getPosition() {
		return position;
	}
	
	public boolean isEmpty() {
		return this.piece == null;
	}
	
	public ChessPiecePanel getChessPiecePanel() {
		return piece;
	}
	
	/*@Override
	public JLayeredPane removeWhenDragged() {
		if(piece != null) {
			this.setCenter(null);
		}
		return board.getLayeredPane();
		
	}

	@Override
	public void addAfterDragged() {
		if(piece != null) {
			this.setCenter(this.piece);
		}
	}*/
	
	public boolean equals(ChessPositionPanel chessSquare) {
		return getPosition().equals(chessSquare.getPosition());
	}
}