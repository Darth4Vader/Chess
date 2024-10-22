package ChessDataTypes;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import ChessDataTypes.ChessMoves.PossibleMoves;

/**
 * <code>ChessPiece</code> is an Object representing a playable chess piece
 */
public abstract class ChessPiece implements ChessData {
	
	/**
	 * The <code>TurnColor</code> color of the <code>ChessPiece</code>
	 */
	private final TurnColor color;
	
	/**
	 * The current <code>Piece</code> type of the <code>ChessPiece</code>. 
	 * it can be changed in a move like PROMOTION a PAWN can do.
	 */
	private Piece type;
    
	/**
	 * The current <code>ChessPosition</code> of the <code>ChessPiece</code>. 
	 * if the piece is out of the game, then this will be <code>null</code>.
	 */
	private ChessPosition position;
    
	/**
     * Tells if this is the first turn of the <code>ChessPiece</code>, 
     * that it means it didn't move from it's starting position by now.
     */
    private boolean isFirstTurn;
    
    /**
     * The first Move the piece made. useful for checking for example 
     * if a PAWN can do EN_PASSANT.
     */
    private ChessMove firstMove;
    
    /**
     * Constructs a new <code>ChessPiece</code> of a <code>Piece</code> 
     * in a given <code>TurnColor</code>
     * @param type a given piece type
     * @param color a given turn color
     */
	public ChessPiece(Piece type, TurnColor color) {
		this.type = type;
		this.color = color;
		this.isFirstTurn = true;
	}
	
    /**
     * Constructs a Clone of a given <code>ChessPiece</code>, of a type based on the input Piece type
     * @param type the New Piece type
     * @param piece a given chess piece
     */
	public ChessPiece(Piece type, ChessPiece piece) {
		this(type, piece.color);
		this.isFirstTurn = piece.isFirstTurn;
		this.firstMove = piece.firstMove;
	}
	
	/**
	 * Constructs a Clone of a given <code>ChessPiece</code>, of a type based on the
	 * input Piece Class
	 * 
	 * @param piece a given chess piece
	 * @param clasz the new Piece Class
	 */
	public ChessPiece newInstance(Class<? extends ChessPiece> clasz) {
		try {
			return clasz.getDeclaredConstructor(ChessPiece.class).newInstance(this);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}
	
	public ChessPiece newInstance() {
		try {
			return this.getClass().getDeclaredConstructor(ChessPiece.class).newInstance(this);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static ChessPiece newInstance(Class<? extends ChessPiece> clasz, TurnColor color) {
		try {
			return clasz.getDeclaredConstructor(TurnColor.class).newInstance(color);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Return the <code>ChessPosition</code> that contains the <code>ChessPiece</code>
	 * @return the <code>ChessPosition</code> that contains the <code>ChessPiece</code>
	 */
	public ChessPosition getPosition() {
		return position;
	}
	
	public int getRank() {
		return position.getRank();
	}
	
	public int getFile() {
		return position.getFile();
	}
	
	/**
	 * Sets the <code>ChessPosition</code> position of the <code>ChessPiece</code>
	 * @param position a given <code>ChessPosition</code> position to set.
	 */
	public void setChessPosition(ChessPosition position) {
		this.position = position;
	}
	
	/**
	 * Returns the <code>TurnColor</code> type of the <code>ChessPiece</code>
	 * @return the <code>TurnColor</code> type
	 */
	public TurnColor getColor() {
		return color;
	}
	
	/**
	 * Returns the <code>Piece</code> type of the <code>ChessPiece</code>
	 * @return the current <code>Piece</code> type
	 */
	public Piece getType() {
		return type;
	}
	
	/**
	 * Sets the <code>Piece</code> type of the <code>ChessPiece</code>
	 * @param type a given <code>Piece</code> type to set.
	 */
	public void setType(Piece type) {
		this.type = type;
	}
	
	/**
	 * Sets the first <code>ChessMove</code> the <code>ChessPiece</code> made.
	 * @param move the first <code>ChessMove</code> the <code>ChessPiece</code> made
	 */
	public void setFirstTurn(ChessMove move) {
		if(this.isFirstTurn && this.firstMove == null) { //this is the first move
			this.isFirstTurn = false;
			this.firstMove = move;
		}
		else { //this is the second, or more move
			this.isFirstTurn = false;
			this.firstMove = null;
		}
	}
	
	/**
	 * Returns the first <code>ChessMove</code> the <code>ChessPiece</code> has done. 
	 * if the <code>ChessPiece</code> made more then one moves, or did not move once, then returns null.
	 * @return the first move the <code>ChessPiece</code> has done, 
	 * if the piece did more then one move, or did not move once, returns null
	 */
	public ChessMove getFirstMove() {
		return this.firstMove;
	}
	
	public boolean isColor(ChessPiece piece) {
		return piece != null ? piece.color == this.color : false;
	}
	
	public boolean isPiece(ChessPiece piece) {
		return piece != null ? piece.type == this.type : false;
	}
	
	public boolean isPiece(Piece type) {
		return type == this.type;
	}
	
	/**
	 * Returns whether the <code>ChessPiece</code> has made only one move.
	 * @return whether the <code>ChessPiece</code> has made only one move.
	 */
	public boolean isFirstMove() {
		return this.isFirstTurn;
	}
	
	@Override
	public String toString() {
		return type + " - " + color;
	}
	
	public abstract List<PossibleMoves> getPossibleMoves();
	
	public abstract Move getMove(ChessMove chessMove);
}