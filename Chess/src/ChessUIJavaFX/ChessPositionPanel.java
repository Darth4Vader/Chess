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
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class ChessPositionPanel extends BorderPaneCanvasPane {
	
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
		this.getBorderPane().getChildren().addListener((ListChangeListener<javafx.scene.Node>) c -> {
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
								piece.checkUpdates();
								break;
							}
						}
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
		paintComponent();
	}
	
	@Override 
	protected void paintComponent() {
		super.paintComponent();
		System.out.println(canvas.getWidth() + " " + canvas.getHeight() + " " + getWidth() + " " + getHeight() + " " + position.getChessPiece() + " " );
		if(this.piece != null)
		System.out.println(this.piece.getWidth() + " " + this.piece.getHeight());
		GraphicsContext gc = getCanvas().getGraphicsContext2D();
		gc.clearRect(0, 0, getWidth(), getHeight());
		if(isPossible) {
			gc.setStroke(Color.rgb(128, 128, 128, 150/255.0));
			gc.setFill(Color.rgb(128, 128, 128, 150/255.0));
			System.out.println("Holsta: " + piece);
			if(piece == null) {
				int width = (int)(getWidth()*0.3);
				int height = (int)(getHeight()*0.3);
				int x = (int)((getWidth() - width)*0.5);
				int y = (int)((getHeight() - height)*0.5);
				gc.strokeOval(x, y, width, height);
				//gc.drawOval(x, y, width, height);
				gc.fillOval(x, y, width, height);
			}
			else {
				gc.strokeOval(0, 0, getWidth(), getHeight());
				//gc.drawOval(0, 0, getWidth(), getHeight());
			}
		}
	}
	
	private void initiateChessPiecePanel() {
		ChessPiece piece = position.getChessPiece();
		if(piece != null) {
			this.piece = new ChessPiecePanel(piece, board);
			this.piece.setChessPositionPanel(this);
			System.out.println("Ready");
			this.setCenter(this.piece);
			//this.getChildren().add(this.piece);
			//StackPane.setAlignment(this.piece, Pos.CENTER);
		}
		else
			this.piece = null;
	}
	
	public void setChessPiecePanel(ChessPiecePanel piece) {
		this.setCenter(null);
		this.piece = piece;
		if(this.piece != null) {
			System.out.println("Ready Or Not");
			this.setCenter(this.piece);
			//this.getChildren().add(this.piece);
			//StackPane.setAlignment(this.piece, Pos.CENTER);
		}
		/*else
			this.setCenter(null);*/
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