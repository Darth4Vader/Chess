package ChessDataTypes;
import java.util.ArrayList;
import java.util.List;

public class ChessMoves implements ChessData {
	
	protected Chess chess;
	protected ChessPosition currentPosition;
	private List<ChessMove> moves;
	private ChessMove check; /* a piece can only check in 1 move the opposite king */
	private int rank, file;
	
	public ChessMoves(Chess chess, ChessPosition currentPosition) {
		this.chess = chess;
		moves = new ArrayList<>();
		check = null;
		this.currentPosition = currentPosition;
		rank = currentPosition.getRank();file = currentPosition.getFile();
		ChessPiece piece = currentPosition.getChessPiece();
		if(piece == null) return;
		List<PossibleMoves> possibleMoves = piece.getPossibleMoves();
		possibleMoves.stream().forEach(possibleMove -> {
			if (possibleMove.isLoop) {
				addPossibleMoves(possibleMove.rank, possibleMove.file);
			} else {
				addPossibleMove(possibleMove.rank, possibleMove.file);
			}
		});
	}
	
	public static class PossibleMoves {
		private int rank, file;
		private boolean isLoop;
		
		public PossibleMoves(int rank, int file) {
            this(rank, file, false);
        }
		
		public PossibleMoves(int rank, int file, boolean isLoop) {
			this.rank = rank;
			this.file = file;
			this.isLoop = isLoop;
		}
	}
	
	private void addPossibleMoves(int rankLoop, int fileLoop) {
		if(rankLoop == 0 && fileLoop == 0) return;
		int rank = this.rank + rankLoop;
		int file = this.file + fileLoop;
		while(addPossibleMove(rank, file)) {
			rank += rankLoop;
			file += fileLoop;
		}	
	}
	
	protected boolean addPossibleMove(int rank, int file) {
		if(0 <= rank && rank < RANK && 0 <= file && file < FILE) {
			ChessMove move = createNewMove(rank, file);
			if(move.isLegal())
				moves.add(move);
			if(move.isCheck())
				this.check = move;
			return chess.getChessPosition(rank, file).isEmpty();
		}
		return false;
	}
	
	protected ChessMove createNewMove(int rank, int file) {
		return new ChessMove(chess, currentPosition, chess.getChessPosition(rank, file));
	}
	
	public boolean canMove() {
		return moves != null && moves.size() != 0;
	}
	
	public boolean canCheck() {
		return check != null;
	}
	
	public boolean isPossibleMove(ChessPosition position) {
		for(ChessMove move : moves) {
			if(move.getMoveToPosition().equals(position))
				return true;
		}
		return false;
	}
	
	public boolean canAvoidCheck(ChessMoves oppositeMoves) {
		for(ChessMove move : moves) {
			if(oppositeMoves.isPossibleCheck(move.getMoveToPosition()) != true)
				return true;
		}
		return false;
	}
	
	public boolean isPossibleCheck(ChessPosition position) {
		if(check != null && position != null) {
			ChessPiece piece = position.getChessPiece();
			if(piece != null) {
				if(this.check.getMoveToPosition().equals(position) && this.check.isCheck()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public List<ChessMove> getPossibleMoves(){
		return moves;
	}
	
	public ChessMove getPossibleCheck(){
		return this.check;
	}
	
}
