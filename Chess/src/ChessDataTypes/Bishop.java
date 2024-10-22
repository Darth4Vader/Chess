package ChessDataTypes;

import java.util.ArrayList;
import java.util.List;

import ChessDataTypes.ChessData.Move;
import ChessDataTypes.ChessData.Piece;
import ChessDataTypes.ChessMoves.PossibleMoves;

public class Bishop extends ChessPiece {

	public Bishop(TurnColor color) {
		super(Piece.BISHOP, color);
		// TODO Auto-generated constructor stub
	}

	public Bishop(ChessPiece piece) {
		super(Piece.BISHOP, piece);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public List<PossibleMoves> getPossibleMoves() {
		List<PossibleMoves> moves = new ArrayList<>();
		moves.add(new PossibleMoves(1,1, true));
		moves.add(new PossibleMoves(1,-1, true));
		moves.add(new PossibleMoves(-1,1, true));
		moves.add(new PossibleMoves(-1,-1, true));
		return moves;
	}

	@Override
	public Move getMove(ChessMove chessMove) {
		Move move = Move.MOVE_UNKOWN;
		move = chessMove.moveOrCapture(1, 1, move);
		move = chessMove.moveOrCapture(1, -1, move);
		move = chessMove.moveOrCapture(-1, 1, move);
		move = chessMove.moveOrCapture(-1, -1, move);
		return move;
	}

}
