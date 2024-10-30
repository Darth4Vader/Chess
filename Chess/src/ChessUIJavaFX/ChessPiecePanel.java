package ChessUIJavaFX;
import ChessDataTypes.ChessData.Piece;
import ChessDataTypes.ChessMove;
import ChessDataTypes.ChessMoves;
import ChessDataTypes.ChessPiece;
import ChessDataTypes.ChessPosition;
import javafx.scene.Cursor;

public class ChessPiecePanel extends ChessPieceImage {
	
	private ChessMain board;
	private Piece currentType;
    private ChessPositionPanel position;
    
    private double startDragX;
    private double startDragY;
    
	public ChessPiecePanel(ChessPiece piece, ChessMain board) {
		super(piece);
		this.currentType = Piece.PIECE_UNKOWN;
		this.board = board;
		updateImage();
		this.setPickOnBounds(false);	
		this.setOnMouseEntered((e) -> setCursor(Cursor.HAND));
		this.focusedProperty().addListener((obs, oldVal, newVal) -> {
			if(newVal)
				possibleMoves = board.getChessBoard().getPossibleMoves(position.getPosition());
			updateMovesPossibilities(newVal);
		});
		this.parentProperty().addListener((obs, oldVal, newVal) -> {
			if(newVal instanceof ChessPositionPanel) {
				this.position = (ChessPositionPanel) newVal;
			}
		});
		this.setOnDragDetected(e -> this.startFullDrag());
		this.setOnMousePressed(e -> {
			this.requestFocus();
            startDragX = e.getSceneX();
            startDragY = e.getSceneY();
			position.toFront(); //instead of this.getParent().getParent().toFront();
			//disable the cursor to work on the current piece, so the positions in the background can receive the mouse event
			this.setMouseTransparent(true);
			this.getParent().setMouseTransparent(true);
		});
		this.setOnMouseDragged(e -> {
			this.setManaged(false);
            this.setTranslateX(e.getSceneX() - startDragX);
            this.setTranslateY(e.getSceneY() - startDragY);
	        e.consume();
		});
		this.setOnMouseReleased(e -> resest());
	}
	
	public void setChessPositionPanel(ChessPositionPanel position) {
		this.position = position;
	}
	
	public ChessPositionPanel getChessPositionPanel() {
        return position;
	}
	
	private void resest() {
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
		checkUpdates();
	}
	
	public void checkUpdates() {
		ChessPosition positionPanel = this.position.getPosition();
		ChessPosition currentPosition = getChessPiece().getPosition();
		if(positionPanel != null && !positionPanel.equals(currentPosition)) {
			resest();
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
}