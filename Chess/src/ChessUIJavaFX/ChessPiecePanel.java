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
import javafx.scene.Scene;
import javafx.scene.effect.Light.Point;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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
		
		this.setPickOnBounds(false);
		
		this.setOnMouseEntered((e) -> setCursor(Cursor.HAND));
		this.focusedProperty().addListener((obs, oldVal, newVal) -> {
			System.out.println("kkk");
			if (newVal) {
				System.out.println("break it");
				/*System.out.println("Currebt p " + position.getPosition() + " \n\n\n\n\n");
				System.out.println("\n\n\n\n\n\n");
				System.out.println(position.getPosition().getChessPiece());*/
				possibleMoves = board.getChessBoard().getPossibleMoves(position.getPosition());
				updateMovesPossibilities(true);
			}
			else {
				System.out.println("Bye: " + this + " " + position.getPosition());
				updateMovesPossibilities(false);
			}
		});
		this.parentProperty().addListener((obs, oldVal, newVal) -> {
			if(newVal instanceof ChessPositionPanel) {
				this.position = (ChessPositionPanel) newVal;
			}
		});
		
		//this.setOnMousePressed((e) -> mousePressed(e));
		//this.setOnMouseReleased((e) -> mouseReleased(e));
		
		//this.setOpaque(false);
		//this.addMouseListener(RemoveDragListener.createDefaultDragListener());
		
		this.setOnDragDetected((e) -> {
			/*Dragboard db = this.startDragAndDrop();
			ClipboardContent content = new ClipboardContent();
			*/
			Parent parent = this.getParent();
			/*
			if(parent instanceof Pane) {
				Scene scene = this.getScene();
				
				Pane pane = (Pane) parent;
				double width = this.getWidth(), height = this.getHeight();
				pane.getChildren().remove(this);
				
				Parent root = scene.getRoot();
				StackPane stack = new StackPane();
				stack.getChildren().add(root);
				stack.getChildren().add(this);
				this.setWidth(width);
				this.setHeight(height);
				scene.setRoot(stack);
			}
			*/
	        
			/*
			x = this.getLayoutX() - e.getSceneX();
	        y = this.getLayoutY() - e.getSceneY();
			*/
			
			/*
            x = e.getX();
            y = e.getY();
            */
			
			/*x = e.getSceneX();
			y = e.getSceneY();*/
			
	        System.out.println("Sick");
			//this.startFullDrag();
	        
	        /*
            Dragboard db = this.startDragAndDrop(TransferMode.MOVE);

            // This is where the magic happens, you take a snapshot of the HBox.
            db.setDragView(this.snapshot(null, null));
            
            ClipboardContent content = new ClipboardContent();
            content.put(new DataFormat("drag"), "");
            db.setContent(content);

            e.consume();
            */
	        
			//this.toFront();
	        /*Dragboard d = this.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.put(new DataFormat("drag"), "");
            d.setContent(content);*/
	        //d.set
			this.startFullDrag();
		});
		this.setOnMousePressed((e) -> {
			this.requestFocus();
			x = e.getSceneX() - this.getTranslateX();
			y = e.getSceneY() - this.getTranslateY();
			this.getParent().getParent().toFront();
			//disable the cursor to work on the current piece, so the positions in the background can receive the mouse event
			this.setMouseTransparent(true);
			this.getParent().setMouseTransparent(true);
			
            startDragX = e.getSceneX();
            startDragY = e.getSceneY();
		});
		this.setOnMouseDragged((e) -> {
			this.setManaged(false);
			//System.out.println("Sick");
			
			
	        /*this.setLayoutX(e.getSceneX() + x);
	        this.setLayoutY(e.getSceneY() + y);*/
		    
			
			
			
			
			
			
			
			
			
			/*this.setTranslateX(e.getX() + this.getTranslateX());
		    this.setTranslateY(e.getY() + this.getTranslateY());*/
			
			System.out.println(e.getX() + "  " + e.getY() + "  ");
			
			/*this.setTranslateX(e.getX() + this.getTranslateX());
		    this.setTranslateY(e.getY() + this.getTranslateY());*/
			
            this.setTranslateX(e.getSceneX() - startDragX);
            this.setTranslateY(e.getSceneY() - startDragY);
            
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
			/*
			double deltaX = e.getX() - x;
            double deltaY = e.getY() - y;
            x += deltaX;
            y += deltaY;
            this.setTranslateX(this.getTranslateX() + deltaX);
            this.setTranslateY(this.getTranslateY() + deltaY);
            */
			
			//this.setPickOnBounds(false);
			
			
			//this.relocate(e.getSceneX() - x, e.getSceneY() - y);
			 /*this.setTranslateX(e.getSceneX() - x);
			 this.setTranslateY(e.getSceneY() - y);*/ 
			
		    /*this.setTranslateX(e.getX() + this.getTranslateX());
		    this.setTranslateY(e.getY() + this.getTranslateY());*/
            
	        /*System.out.println(this.getLayoutX() + "  " + this.getLayoutY());
	        System.out.println(this.getWidth() + "  " + this.getHeight());
	        System.out.println(this.getTranslateX() + "  " + this.getTranslateY());
	        
	        System.out.println(this.getBoundsInLocal());
	        System.out.println(this.getBoundsInParent());
	        System.out.println(this.getLayoutBounds());*/
			
	        //mouseDragged(e);
	        e.consume();
		});
		
		/*
		this.setOnMouseDragReleased((e) -> {
			System.out.println("Bad2");
			this.setTranslateX(0);
			this.setTranslateY(0);
			this.setManaged(true);
		});*/
		/*
		this.setOnMouseDragReleased(e -> {
			System.out.println("Bad2");
		});*/
		this.setOnMouseReleased(e -> resest());
		
		/*this.setOnMouseDragOver(e -> {
			System.out.println("hhh55");
		});
		this.setOnMouseDragReleased((e) -> {
			System.out.println("hhh22");
		});*/
	}
	
	private double x, y;
	

    private double startDragX;
    private double startDragY;
	
	public void setChessPositionPanel(ChessPositionPanel position) {
		this.position = position;
	}
	
	public ChessPositionPanel getChessPositionPanel() {
        return position;
	}
	
	@Override
	protected void layoutChildren() {
		checkUpdates();
	}
	
	private void resest() {
		System.out.println("Bad1");
		this.setTranslateX(0);
		this.setTranslateY(0);
		this.setManaged(true);
		
		//enable the cursor to work on the current piece, after the dragging is finished
		this.setMouseTransparent(false);
		this.getParent().setMouseTransparent(false);
	}
	
	@Override
	protected void paintComponent() {
		updateImage();
		super.paintComponent();
		//System.out.println(position.getPosition() + "  " + piece.getPosition());
		checkUpdates();
	}
	
	public void checkUpdates() {
		ChessPosition positionPanel = this.position.getPosition();
		ChessPosition currentPosition = getChessPiece().getPosition();
		//System.out.println("Lo " + positionPanel + "  " + currentPosition + "  " + (positionPanel != null && !positionPanel.equals(positionPanel)));
		System.out.println("Door: " + !positionPanel.equals(currentPosition) + " " + positionPanel + "  " + currentPosition);
		if(positionPanel != null && !positionPanel.equals(currentPosition)) {
			resest();
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
	
	public ChessMoves getPossibleMoves() {
		return possibleMoves;
	}
	
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
	
	public void mouseDragged2(MouseEvent e) {
		if(canDrag == true) {
		    Bounds currCoords = this.getBoundsInLocal();
		    Point2D newMouseDownCompCoords = new Point2D(e.getX(), e.getY());
		    currentPoint = new Point2D(currCoords.getMinX() + newMouseDownCompCoords.getX() - mouseDownCompCoords.getX(), currCoords.getMinY() + newMouseDownCompCoords.getY() - mouseDownCompCoords.getY());
		    Bounds bounds = board.getBoundsInLocal();
		    if(!(currentPoint.getY() + currCoords.getHeight() > bounds.getHeight()) &&
		    		!(currentPoint.getX() + currCoords.getWidth() > bounds.getWidth()) &&
		    		!(currentPoint.getY() < bounds.getMinY()) &&  !(currentPoint.getX() < bounds.getMinX())) {
		    	System.out.println("Relocating " + currentPoint.getX() + "  " + currentPoint.getY() + "  " + this.getWidth() + "  " + this.getHeight());
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
		
		
		
	    Point2D point = new Point2D(e.getX() + (this.getWidth()*0.5),e.getY() + (this.getHeight()*0.5));
		System.out.println(point);
		System.out.println(this.localToScene(point));
		point = this.localToScene(point);
	    Node component = JavaFXUtils.pick(board, point.getX(), point.getY());
		System.out.println("Sourced: " + e.getSource().getClass());
		System.out.println("Comp: " + component);
		boolean b = true;
		if(component instanceof ChessPositionPanel && possibleMoves != null) {
			ChessPositionPanel destPosition = (ChessPositionPanel) component;
			System.out.println(destPosition.getPosition());
			List<ChessMove> list = possibleMoves.getPossibleMoves();
			if(list.size() != 0) {
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
		System.out.println(b);
		/*if(!b) {
			
		}
		else {
			this.setTranslateX(0);
			this.setTranslateY(0);
			this.setManaged(true);
		}*/
		this.setTranslateX(0);
		this.setTranslateY(0);
		this.setManaged(true);
		canDrag = false;
		mouseDownCompCoords = new Point2D(0, 0);
	}

	public void mouseReleased2(MouseEvent e) {
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