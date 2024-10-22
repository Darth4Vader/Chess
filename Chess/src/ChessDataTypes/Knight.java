package ChessDataTypes;

import java.util.ArrayList;
import java.util.List;

import ChessDataTypes.ChessData.Piece;
import ChessDataTypes.ChessMoves.PossibleMoves;

public class Knight extends ChessPiece {

	public Knight(TurnColor color) {
		super(Piece.KNIGHT, color);
		// TODO Auto-generated constructor stub
	}

	public Knight(ChessPiece piece) {
		super(Piece.KNIGHT, piece);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public List<PossibleMoves> getPossibleMoves() {
        List<PossibleMoves> moves = new ArrayList<>();
        ChessPosition pos = this.getPosition();
        int rank = pos.getRank(), file = pos.getFile();
        //Move
        if(rank > 1) {
            moves.add(new PossibleMoves(rank-2, file-1));
            moves.add(new PossibleMoves(rank-2, file+1));
        }
        if(rank < RANK-2) {
            moves.add(new PossibleMoves(rank+2, file-1));
            moves.add(new PossibleMoves(rank+2, file+1));
        }
        if(file > 1) {
            moves.add(new PossibleMoves(rank-1, file-2));
            moves.add(new PossibleMoves(rank+1, file-2));
        }
        if(file < FILE-2) {
            moves.add(new PossibleMoves(rank-1, file+2));
            moves.add(new PossibleMoves(rank+1, file+2));
        }
        return moves;
	}

	@Override
	public Move getMove(ChessMove chessMove) {
		ChessPosition moveToPosition = chessMove.getMoveToPosition();
		int rank = getRank(), file = getFile();
		int toRank = moveToPosition.getRank(), toFile = moveToPosition.getFile();
		if(rank-1 == toRank) {
			if(file-2 == toFile)
				return chessMove.isMoveOrCapture();
			if(file+2 == toFile)
				return chessMove.isMoveOrCapture();
		}
		if(rank+1 == toRank) {
			if(file-2 == toFile)
				return chessMove.isMoveOrCapture();
			if(file+2 == toFile)
				return chessMove.isMoveOrCapture();
		}
		if(rank-2 == toRank) {
			if(file-1 == toFile)
				return chessMove.isMoveOrCapture();
			if(file+1 == toFile)
				return chessMove.isMoveOrCapture();
		}
		if(rank+2 == toRank) {
			if(file-1 == toFile)
				return chessMove.isMoveOrCapture();
			if(file+1 == toFile)
				return chessMove.isMoveOrCapture();
		}
		return Move.MOVE_UNKOWN;
	}

}
