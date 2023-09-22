package ChessDataTypes;
public class LegalChessMoves extends ChessMoves {
	
	public LegalChessMoves(Chess chess, int rank, int file) {
		this(chess, chess.getChessPosition(rank, file));
	}
	
	public LegalChessMoves(Chess chess, ChessPosition currentPosition) {
		super(chess, currentPosition);
	}
	
	@Override
	protected ChessMove createNewMove(int rank, int file) {
		return new LegalChessMove(chess, currentPosition, chess.getChessPosition(rank, file));
	}
	
}