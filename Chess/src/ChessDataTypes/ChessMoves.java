package ChessDataTypes;
import java.util.ArrayList;
import java.util.List;

import ChessDataTypes.ChessData.Move;

public class ChessMoves implements ChessData {
	
	protected Chess chess;
	protected ChessPosition currentPosition;
	private List<ChessMove> moves;
	private ChessMove check; /* a piece can only check in 1 move the opposite king */
	private int rank, file;
	//private TurnColor color;
	private ChessPiece piece;
	protected boolean checkPositionEmpty;
	
	public ChessMoves(Chess chess, ChessPosition currentPosition) {
		this(chess, currentPosition, false);
	}
	
	public ChessMoves(Chess chess, ChessPosition currentPosition, boolean checkPositionEmpty) {
		this.chess = chess;
		this.checkPositionEmpty = checkPositionEmpty;
		moves = new ArrayList<>();
		check = null;
		this.currentPosition = currentPosition;
		rank = currentPosition.getRank();file = currentPosition.getFile();
		piece = currentPosition.getChessPiece();
		if(piece == null) return;
		Piece type = piece.getType();
		TurnColor color = piece.getColor();
		if(type == Piece.PAWN) {
			if(color == TurnColor.BLACK) {
				//Pawn Starts
				if(rank == 1)
					addPossibleMove(rank+2,file);
				//Pawn Moves
				addPossibleMove(rank+1, file);
				//Pawn Captures
				addPossibleMove(rank+1, file+1);
				addPossibleMove(rank+1, file-1);
			} else
			if(color == TurnColor.WHITE) {
				//Pawn Starts
				if(rank == 6)
					addPossibleMove(rank-2,file);
				//Pawn Moves
				addPossibleMove(rank-1, file);
				//Pawn Captures
				addPossibleMove(rank-1, file+1);
				addPossibleMove(rank-1, file-1);
			}
		}
		else if(type == Piece.KNIGHT) {
			addPossibleMove(rank-1, file-2);
			addPossibleMove(rank-1, file+2);
			addPossibleMove(rank+1, file-2);
			addPossibleMove(rank+1, file+2);
			addPossibleMove(rank-2, file-1);
			addPossibleMove(rank-2, file+1);
			addPossibleMove(rank+2, file-1);
			addPossibleMove(rank+2, file+1);
		} 
		else if(type == Piece.KING) {
			//Move
			if(rank > 0) {
				addPossibleMove(rank-1, file);
				if(file > 0)
					addPossibleMove(rank-1, file-1);
				if(file < FILE-1)
					addPossibleMove(rank-1, file+1);
			}
			if(rank < RANK-1) {
				addPossibleMove(rank+1, file);
				if(file > 0)
					addPossibleMove(rank+1, file-1);
				if(file < FILE-1)
					addPossibleMove(rank+1, file+1);
			}
			if(file > 0)
				addPossibleMove(rank, file-1);
			if(file < FILE-1)
				addPossibleMove(rank, file+1);
			//Castling
			if(piece.isFirstMove()) {
				//System.out.println("Hollo How " + rank + "   " + (file-2) + " , " + (file+2));
				addPossibleMove(rank, file-2);
				//System.out.println("Bleach");
				addPossibleMove(rank, file+2);
			}
		}
		else {
			if(type == Piece.BISHOP || type == Piece.QUEEN) {
				addPossibleMoves(1,1);
				addPossibleMoves(1,-1);
				addPossibleMoves(-1,1);
				addPossibleMoves(-1,-1);
			}
			if(type == Piece.ROOK || type == Piece.QUEEN) {
				addPossibleMoves(1,0);
				addPossibleMoves(-1,0);
				addPossibleMoves(0,1);
				addPossibleMoves(0,-1);
			}
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
