package ChessDataTypes;

/**
 * <code>ChessPosition</code> is an Object representing a position in a chess board.
 */
public class ChessPosition implements ChessData {
	
	/**
	 * The rank value of the <code>ChessPosition</code>. 
	 * Similar to row number. 
	 */
	private final int rank;
	
	/**
	 * The file value of the <code>ChessPosition</code>. 
	 * Similar to column number. 
	 */
	private final int file;
	
	/**
	 * The <code>TurnColor</code> backgroundColor of the <code>ChessPosition</code>. 
	 * This is not the value of the colors for the different players, 
	 * rather the position color.
	 */
	private final TurnColor backgroundColor;
	
	/**
	 * The current <code>ChessPiece</code> of the <code>ChessPosition</code>. 
	 * if the position is empty then the value of the piece will be <code>null</code>.
	 */
	private ChessPiece piece;
	
	/**
     * Constructs a new <code>ChessPosition</code> of a <code>TurnColor</code> background color,
     * in rank and file position.
	 * @param backgroundColor the background color to use
	 * @param rank the rank/row number
	 * @param file the file/column number
	 */
	public ChessPosition(TurnColor backgroundColor, int rank, int file) {
		this.rank = rank;
		this.file = file;
		this.backgroundColor = backgroundColor;
	}
	
	/**
	 * Returns the rank/row number of the <code>ChessPosition</code>
	 * @return the rank/row number
	 */
	public int getRank() {
		return rank;
	}
	
	/**
	 * Returns the file/column number of the <code>ChessPosition</code>
	 * @return the file/column number
	 */
	public int getFile() {
		return file;
	}
	
	/**
	 * Returns the <code>TurnColor</code> background color of the <code>ChessPosition</code>
	 * @return the <code>TurnColor</code> background color
	 */
	public TurnColor getBackgroundColor() {
		return backgroundColor;
	}
	
	/**
	 * Return the <code>ChessPiece</code> that is placed on the <code>ChessPosition</code>
	 * @return the <code>ChessPiece</code> that is placed on the <code>ChessPosition</code>
	 */
	public ChessPiece getChessPiece() {
		return this.piece;
	}
	
	/**
	 * Removes the current <code>ChessPiece</code> (if exists) from the <code>ChessPosition</code>, and then 
	 * Sets a new <code>ChessPiece</code> that will be contained inside the <code>ChessPosition</code>.
	 * @param piece a given <code>ChessPiece</code> piece to set.
	 */
	public void setChessPiece(ChessPiece piece) {
		if(this.piece != null) // first we remove the previous piece from this position
			this.piece.setChessPosition(null);
		if(piece != null) //then if there is a new piece, we add it to this position
			piece.setChessPosition(this);
		this.piece = piece;
	}
	
	/**
	 * Returns <code>true</code> only if the <code>ChessPosition</code> 
	 * does not contain a <code>ChessPiece</code>.
	 * @return <code>true</code>, if there isn't a <code>ChessPiece</code> inside the <code>ChessPosition</code>, 
	 * otherwise returns <code>false</code>
	 */
	public boolean isEmpty() {
		return this.piece == null;
	}
	
	/**
	 * Returns <code>true</code> only if the <code>ChessPosition</code> contains a
	 * <code>ChessPiece</code>.
	 * 
	 * @return <code>true</code>, if there is a <code>ChessPiece</code> inside the
	 *         <code>ChessPosition</code>, otherwise returns <code>false</code>
	 */
	public boolean isPresent() {
		return !isEmpty();
	}
	
	/**
	 * Returns the row number of a given rank number.
	 * Rank number is in the order of 0 -> 8, from the bottom to the top.
	 * Row number is in the order of 8 -> 0, from the top to the bottom.
	 * @param rank a given rank number
	 * @return an equivalent row number of the given rank number
	 */
	public static int getRowName(int rank) {
		return (RANK - rank);
	}
	
	/**
	 * Returns the column name of a given file number.
	 * File number are in the values of 0 -> 7
	 * Column Names are in the values of 'a' -> 'h'
	 * @param file a given file number
	 * @return an equivalent column name of the given file number.
	 */
	public static char getColumnName(int file) {
		return (char) ('a' + file);
	}
	
	/**
	 * Returns the rank number of a given row number.
	 * Rank number is in the order of 0 -> 8, from the bottom to the top.
	 * Row number is in the order of 8 -> 0, from the top to the bottom.
	 * @param row a given row number
	 * @return an equivalent rank number of the given row number
	 */
	public static int getRankNumber(int rowNumber) {
		return RANK - rowNumber;
	}
	
	/**
	 * Returns the file number of a given column name.
	 * File number are in the values of 0 -> 7
	 * Column Names are in the values of 'a' -> 'h'
	 * @param colummnName a given column name
	 * @return an equivalent file number of the given column name.
	 */
	public static int getFileNumber(char colummnName) {
		return (int) (colummnName - 'a');
	}
	
	/**
	 * Checks if a given <code>ChessPosition</code> and the current <code>ChessPosition</code> are equals 
	 * by comparing the file and rank values of them.
	 * @param position the <code>ChessPosition</code> with which to compare with the current one.
	 * @return true, if they are equals, returns false otherwise.
	 */
	public boolean equals(ChessPosition position) {
		return position != null ? this.rank == position.getRank() && 
				this.file == position.getFile() : false;
	}
	
	@Override
	public String toString() {
		String str = ""+ 'r' + rank + 'f' + file;
		return str;
	}
}