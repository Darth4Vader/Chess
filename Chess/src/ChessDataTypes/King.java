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
	public List<PossibleMoves> getPossibleMoves(ChessPosition currentPosition) {
		List<PossibleMoves> moves = new ArrayList<>();
		int rank = currentPosition.getRank(), file = currentPosition.getFile();
		ChessPiece piece = currentPosition.getChessPiece();
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
		if(piece.isFirstMove()) {
			moves.add(new PossibleMoves(rank, file-2));
			moves.add(new PossibleMoves(rank, file+2));
		}
		return moves;
	}

	@Override
	public Move getMove(ChessMove chessMove) {
		ChessPosition currentPosition = chessMove.getCurrentPosition();
		ChessPosition moveToPosition = chessMove.getMoveToPosition();
		int rank = currentPosition.getRank(), file = currentPosition.getFile();
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
		if(!moveToPosition.isEmpty()) return Move.MOVE_UNKOWN;
		ChessPosition currentPosition = chessMove.getCurrentPosition();
		int file = currentPosition.getFile();
		int rank = currentPosition.getRank();
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
		ChessPosition currentPosition = chessMove.getCurrentPosition();
		int file = currentPosition.getFile(), rank = currentPosition.getRank();
		Chess chess = chessMove.getChess();
		ChessPiece currentPiece = currentPosition.getChessPiece();
		ChessPiece piece = playPosition.getChessPiece();
		if(piece == null || (!currentPosition.getChessPiece().isFirstMove() && !piece.isFirstMove())) return false;
		if(!piece.isPiece(Piece.ROOK) || !currentPiece.isColor(piece)) return false;
		int playRank = playPosition.getRank(), playFile = playPosition.getFile();
		if(playRank != rank) return false;
		if(file < playFile) {
			for(int i = file+1;i < playFile; i++)
				if(!chess.isPositionEmpty(playRank, i))
					return false;
		}
		else
			for(int i = file-1;i > 0; i--)
				if(!chess.isPositionEmpty(playRank, i))
					return false;
		return true;
	}
}