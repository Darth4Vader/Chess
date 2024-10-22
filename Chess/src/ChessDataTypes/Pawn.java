package ChessDataTypes;

import java.util.ArrayList;
import java.util.List;

import ChessDataTypes.ChessData.Move;
import ChessDataTypes.ChessData.Piece;
import ChessDataTypes.ChessData.TurnColor;
import ChessDataTypes.ChessMoves.PossibleMoves;

public class Pawn extends ChessPiece {

	public Pawn(TurnColor color) {
		super(Piece.PAWN, color);
		// TODO Auto-generated constructor stub
	}

	public Pawn(ChessPiece piece) {
		super(Piece.PAWN, piece);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public List<PossibleMoves> getPossibleMoves(ChessPosition currentPosition) {
        List<PossibleMoves> moves = new ArrayList<>();
        int rank = currentPosition.getRank(), file = currentPosition.getFile();
        TurnColor color = getColor();
        if(color == TurnColor.BLACK) {
            //Pawn Starts
            if(rank == 1)
                moves.add(new PossibleMoves(rank+2,file));
            //Pawn Moves
            moves.add(new PossibleMoves(rank+1, file));
            //Pawn Captures
            moves.add(new PossibleMoves(rank+1, file+1));
            moves.add(new PossibleMoves(rank+1, file-1));
        } else
        if(color == TurnColor.WHITE) {
            //Pawn Starts
            if(rank == 6)
                moves.add(new PossibleMoves(rank-2,file));
            //Pawn Moves
            moves.add(new PossibleMoves(rank-1, file));
            //Pawn Captures
            moves.add(new PossibleMoves(rank-1, file+1));
            moves.add(new PossibleMoves(rank-1, file-1));
        }
        return moves;
	}

	@Override
	public Move getMove(ChessMove chessMove) {
		ChessPosition currentPosition = chessMove.getCurrentPosition();
		ChessPosition moveToPosition = chessMove.getMoveToPosition();
		int rank = currentPosition.getRank(), file = currentPosition.getFile();
		int toRank = moveToPosition.getRank(), toFile = moveToPosition.getFile();
		Chess chess = chessMove.getChess();
		ChessPiece currentPiece = currentPosition.getChessPiece();
		TurnColor color = currentPiece.getColor();
		if(color == TurnColor.BLACK) {
				//Pawn Starts
				if(rank == 1)
					if(rank+2 == toRank && file == toFile && moveToPosition.isEmpty() && chess.isPositionEmpty(rank+1, file))
						return Move.PAWN_START;
				//Pawn Moves
				if(rank+1 == toRank && file == toFile && moveToPosition.isEmpty())
					return Move.MOVE;
				//Pawn Captures (Capture or En Pasent)
				if((file-1 == toFile || file+1 == toFile) && rank+1 == toRank)
					return checkPawnCaptures(chessMove);
		} else
		if(color == TurnColor.WHITE) {
			//Pawn Starts
			if(rank == 6)
				if(rank-2 == toRank && file == toFile && moveToPosition.isEmpty() && chess.isPositionEmpty(rank-1, file))
					return Move.PAWN_START;
			//Pawn Moves
			if(rank-1 == toRank && file == toFile && moveToPosition.isEmpty()) 
				return Move.MOVE;
			//Pawn Captures (Capture or En Pasent)
			if((file-1 == toFile || file+1 == toFile) && rank-1 == toRank)
				return checkPawnCaptures(chessMove);
		}
		return Move.MOVE_UNKOWN;
	}
	
	private Move checkPawnCaptures(ChessMove chessMove) {
		Move move = chessMove.isCapture();
		ChessPosition moveToPosition = chessMove.getMoveToPosition();
		if(!moveToPosition.isEmpty()) return move;
		ChessPosition currentPosition = chessMove.getCurrentPosition();
		int file = currentPosition.getFile();
		if(file > 0)
			move = checkPawnEnPasent(0, -1, move, chessMove);
		if(file < FILE-1)
			move = checkPawnEnPasent(0, 1, move, chessMove);
		return move;
	}
	
	private Move checkPawnEnPasent(int rankLoop, int fileLoop, Move move, ChessMove chessMove) {
		if(move != Move.MOVE_UNKOWN) return move;
		ChessPosition currentPosition = chessMove.getCurrentPosition();
		int rank = currentPosition.getRank(), file = currentPosition.getFile();
		int toRank = rank + rankLoop;
		int toFile = file + fileLoop;
		Chess chess = chessMove.getChess();
		ChessPosition playPosition = chess.getChessPosition(toRank, toFile);
		boolean isLegal = checkPawnEnPasent(playPosition, chessMove);
		if(isLegal) {
			chessMove.setPlayPosition(playPosition);
			return Move.EN_PASSANT;
		}
		return Move.MOVE_UNKOWN;
	}
	
	/**
	 * Special Chess move only for <strong>PAWN</strong> pieces.
	 * @param piece
	 * @return
	 */
	private boolean checkPawnEnPasent(ChessPosition position, ChessMove chessMove) {
		if(position == null) return false;
		ChessPiece piece = position.getChessPiece();
		ChessPosition currentPosition = chessMove.getCurrentPosition();
		ChessPiece currentPiece = currentPosition.getChessPiece();
		if(piece == null || currentPiece.isColor(piece) || !piece.isPiece(Piece.PAWN)) return false;
		Chess chess = chessMove.getChess();
		ChessPosition moveToPosition = chessMove.getMoveToPosition();
		ChessMove lastOppositeMove = chess.getLastMove();
		if(lastOppositeMove != null) {
			ChessPosition lastMovedPosition = lastOppositeMove.getMoveToPosition();
			if(position.equals(lastMovedPosition)) {
				if(lastOppositeMove.getMoveName() == Move.PAWN_START) {
					int lastMovedRank = lastMovedPosition.getRank(), lastMovedFile = lastMovedPosition.getFile();
					int rank = currentPosition.getRank(), file = currentPosition.getFile();
					int toRank = moveToPosition.getRank(), toFile = moveToPosition.getFile();
					if(lastMovedRank == rank && (lastMovedFile+1 == file || lastMovedFile-1 == file))
						if((lastMovedRank-1 == toRank || lastMovedRank+1 == toRank) && lastMovedFile == toFile)
							return true;
				}
			}
		}
		return false;
	}
}
