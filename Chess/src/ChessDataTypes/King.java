package ChessDataTypes;

import java.util.ArrayList;
import java.util.List;

import ChessDataTypes.ChessData.Move;
import ChessDataTypes.ChessData.Piece;
import ChessDataTypes.ChessMoves.PossibleMoves;

public class King extends ChessPiece {

	public King(TurnColor color) {
		super(Piece.KING, color);
		// TODO Auto-generated constructor stub
	}

	public King(ChessPiece piece) {
		super(Piece.KING, piece);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public List<PossibleMoves> getPossibleMoves() {
		List<PossibleMoves> moves = new ArrayList<>();
		int rank = getRank(), file = getFile();
		//Move
		if(rank > 0) {
			moves.add(new PossibleMoves(rank-1, file));
			if(file > 0)
				moves.add(new PossibleMoves(rank-1, file-1));
			if(file < FILE-1)
				moves.add(new PossibleMoves(rank-1, file+1));
		}
		if(rank < RANK-1) {
			moves.add(new PossibleMoves(rank+1, file));
			if(file > 0)
				moves.add(new PossibleMoves(rank+1, file-1));
			if(file < FILE-1)
				moves.add(new PossibleMoves(rank+1, file+1));
		}
		if(file > 0)
			moves.add(new PossibleMoves(rank, file-1));
		if(file < FILE-1)
			moves.add(new PossibleMoves(rank, file+1));
		//Castling
		if(this.isFirstMove()) {
			moves.add(new PossibleMoves(rank, file-2));
			moves.add(new PossibleMoves(rank, file+2));
		}
		return moves;
	}

	@Override
	public Move getMove(ChessMove chessMove) {
		ChessPosition moveToPosition = chessMove.getMoveToPosition();
		int rank = getRank(), file = getFile();
		int toRank = moveToPosition.getRank(), toFile = moveToPosition.getFile();
		if(Math.abs(rank-toRank) == 1 && file == toFile)
			return chessMove.isMoveOrCapture();
		if(rank==toRank && Math.abs(file-toFile) == 1)
			return chessMove.isMoveOrCapture();
		if(Math.abs(rank-toRank) == 1 && Math.abs(file-toFile) == 1)
			return chessMove.isMoveOrCapture();
		return checkCastling(chessMove);
	}
	
	private Move checkCastling(ChessMove chessMove) {
		ChessPosition moveToPosition = chessMove.getMoveToPosition();
		if(moveToPosition.isPresent()) return Move.MOVE_UNKOWN;
		int file = getFile(), rank = getRank();
		int toFile = moveToPosition.getFile();
		Chess chess = chessMove.getChess();
		ChessPosition playPosition = file < toFile ? chess.getChessPosition(rank, 7) 
											   	   : chess.getChessPosition(rank, 0);
		if(checkCastling(chessMove, playPosition)) {
			chessMove.setPlayPosition(playPosition);
			return Move.CASTLING;
		}
		return Move.MOVE_UNKOWN;
	}
	
	private boolean checkCastling(ChessMove chessMove, ChessPosition playPosition) {
		if(playPosition == null) return false;
		ChessPosition moveToPosition = chessMove.getMoveToPosition();
		int toFile = moveToPosition.getFile(), toRank = moveToPosition.getRank();
		if(!(toFile == 2 || toFile == 6) && !(toRank == 0 || toRank == 7)) return false;
		ChessPiece playPiece = playPosition.getChessPiece();
		if(playPiece == null || (!this.isFirstMove() && !playPiece.isFirstMove())) return false;
		if(!playPiece.isPiece(Piece.ROOK) || !isColor(playPiece)) return false;
		return chessMove.getChess().isPathToPositionEmpty(getPosition(), playPosition);
	}
}