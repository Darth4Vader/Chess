package ChessUIJavaFX;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ChessDataTypes.Chess;
import ChessDataTypes.ChessData;
import ChessDataTypes.ChessData.Piece;
import ChessDataTypes.ChessData.TurnColor;
import FileUtilities.FilesUtils;
import JavaFXUtilities.JavaFXUtils;
import ChessDataTypes.ChessMove;
import ChessDataTypes.ChessMoves;
import ChessDataTypes.ChessPiece;
import ChessDataTypes.ChessPosition;
import SwingUtilities.SwingUtils;
import javafx.collections.ListChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.Light.Point;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;

public class ChessPiecePanel extends ChessPieceImage {
	
	private ChessMain board;
	private Piece currentType;
    private ChessPositionPanel position;
    
	public ChessPiecePanel(ChessPiece piece, ChessMain board) {
		super(piece);
		this.currentType = Piece.PIECE_UNKOWN;
		this.board = board;
		updateImage();
		this.setOnMouseEntered((e) -> setCursor(Cursor.HAND));
		this.focusedProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal) {
				System.out.println("Currebt p " + position.getPosition() + " \n\n\n\n\n");
				System.out.println("\n\n\n\n\n\n");
				System.out.println(position.getPosition().getChessPiece());
				possibleMoves = board.getChessBoard().getPossibleMoves(position.getPosition());
				updateMovesPossibilities(true);
			}
			else {
				updateMovesPossibilities(false);
			}
		});
		this.parentProperty().addListener((obs, oldVal, newVal) -> {
			if(newVal instanceof ChessPositionPanel) {
				this.position = (ChessPositionPanel) newVal;
			}
		});
		
		this.setOnMousePressed((e) -> mousePressed(e));
		this.setOnMouseReleased((e) -> mouseReleased(e));
		
		//this.setOpaque(false);
		//this.addMouseListener(RemoveDragListener.createDefaultDragListener());
		
		this.setOnDragDetected((e) -> {
			this.startFullDrag();
		});
	}
	
	public void setChessPositionPanel(ChessPositionPanel position) {
		this.position = position;
	}
	
	@Override
	protected void paintComponent() {
		updateImage();
		super.paintComponent();
		//System.out.println(position.getPosition() + "  " + piece.getPosition());
		checkUpdates();
	}
	
	private void checkUpdates() {
		ChessPosition positionPanel = this.position.getPosition();
		ChessPosition currentPosition = getChessPiece().getPosition();
		//System.out.println("Lo " + positionPanel + "  " + currentPosition + "  " + (positionPanel != null && !positionPanel.equals(positionPanel)));
		if(positionPanel != null && !positionPanel.equals(currentPosition)) {
			System.out.println("Enter\n\n\n\n\n");
			System.out.println(getChessPiece());
			System.out.println(positionPanel + "  " + currentPosition);
			System.out.println(positionPanel.getChessPiece());
			this.position.setChessPiecePanel(null);
			this.position = null;
			if(currentPosition != null) {
				this.position = board.getChessPositionPanel(currentPosition);
				if(this.position != null) {
					this.position.setChessPiecePanel(this);
				}
			} else if(positionPanel != null) {
				ChessPiece piece = positionPanel.getChessPiece();
				if (piece != null) {
					this.position = board.getChessPositionPanel(positionPanel);
					if(this.position != null) {
						this.position.setChessPiecePanel(new ChessPiecePanel(piece, board));
					}
				}
			}
		}
	}
	
	private void updateImage() {
		Piece type = this.getChessPiece().getType();
		if(!type.equals(this.currentType)) {
			this.currentType = type;
			loadImage();
		}
	}
	
	private ChessMoves possibleMoves;
	
	private void updateMovesPossibilities(boolean canMove) {
		if(possibleMoves != null) {
	        for(ChessMove move : possibleMoves.getPossibleMoves()) {
	        	ChessPosition position = move.getMoveToPosition();
	        	ChessPositionPanel positionPanel = board.getChessPositionPanel(position);
	        	if(positionPanel != null)
	        		positionPanel.setIfPossible(canMove);
	        }
		}
	}
	
	private ChessPositionPanel prevSquare;
	private boolean canDrag;
	private Border prevBorder;
	private Point2D currentPoint;
	private Point2D mouseDownCompCoords = new Point2D(0,0);
	
	public void mouseDragged(MouseEvent e) {
		if(canDrag == true) {
		    Bounds currCoords = this.getBoundsInLocal();
		    Point2D newMouseDownCompCoords = new Point2D(e.getX(), e.getY());
		    currentPoint = new Point2D(currCoords.getMinX() + newMouseDownCompCoords.getX() - mouseDownCompCoords.getX(), currCoords.getMinY() + newMouseDownCompCoords.getY() - mouseDownCompCoords.getY());
		    Bounds bounds = board.getBoundsInLocal();
		    if(!(currentPoint.getY() + currCoords.getHeight() > bounds.getHeight()) &&
		    		!(currentPoint.getX() + currCoords.getWidth() > bounds.getWidth()) &&
		    		!(currentPoint.getY() < bounds.getMinY()) &&  !(currentPoint.getX() < bounds.getMinX())) {
		    	this.relocate(currentPoint.getX(), currentPoint.getY());
		    }
		    Point2D point = new Point2D((int)(currentPoint.getX() + (this.getWidth()*0.5)), (int)(currentPoint.getY() + (this.getHeight()*0.5)));
		    Node component = JavaFXUtils.pick(board, point.getX(), point.getY());
			if(component instanceof ChessPositionPanel) {
				ChessPositionPanel destPosition = (ChessPositionPanel) component;
					
				if(prevSquare == null || !prevSquare.equals(destPosition)) {
					if(prevSquare != null)
						prevSquare.setBorder(prevBorder);
					prevSquare = destPosition;prevBorder = prevSquare.getBorder();
					destPosition.setBorder(Border.stroke(Color.DARKGRAY));
				}
			}
		}
	}

	public void mousePressed(MouseEvent e) {
		this.requestFocus();
		if(e.getButton() != MouseButton.PRIMARY) return;
		mouseDownCompCoords = new Point2D(e.getX(), e.getY());
	    Bounds currCoords = this.getBoundsInLocal();
	    Point2D newMouseDownCompCoords = new Point2D(e.getX(), e.getY());
	    currentPoint = new Point2D(currCoords.getMinX() + newMouseDownCompCoords.getX() - mouseDownCompCoords.getX(), currCoords.getMinY() + newMouseDownCompCoords.getY() - mouseDownCompCoords.getY());
		canDrag = true;
		System.out.println("Pressed");
	}

	public void mouseReleased(MouseEvent e) {
		if(e.getButton() != MouseButton.PRIMARY) return;
		if(prevSquare != null)
			prevSquare.setBorder(prevBorder);
	    Point2D point = new Point2D((int)(currentPoint.getX() + (this.getWidth()*0.5)), (int)(currentPoint.getY() + (this.getHeight()*0.5)));
		System.out.println(point);
	    Node component = null;//JavaFXUtils.pick(board, point.getX(), point.getY());
		System.out.println("Sourced: " + e.getSource().getClass());
		System.out.println("Comp: " + component);
		if(component instanceof ChessPositionPanel && possibleMoves != null) {
			ChessPositionPanel destPosition = (ChessPositionPanel) component;
			List<ChessMove> list = possibleMoves.getPossibleMoves();
			if(list.size() != 0) {
				boolean b = true;
				for(ChessMove move : list) {
					if(destPosition.getPosition().equals(move.getMoveToPosition())) {
						board.switchTurn(move);
						b = false;
						break;
					}
				}
				if(b)
					this.requestFocus();
			}
		}
		canDrag = false;
		mouseDownCompCoords = new Point2D(0, 0);
	}
}