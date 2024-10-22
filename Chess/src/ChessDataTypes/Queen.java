package ChessDataTypes;

import java.util.ArrayList;
import java.util.List;

import ChessDataTypes.ChessMoves.PossibleMoves;

public class Queen extends ChessPiece {

	public Queen(TurnColor color) {
		super(Piece.QUEEN, color);
		// TODO Auto-generated constructor stub
	}

	public Queen(ChessPiece piece) {
		super(Piece.QUEEN, piece);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public List<PossibleMoves> getPossibleMoves() {
		List<PossibleMoves> moves = new ArrayList<>();
		moves.add(new PossibleMoves(1,0, true));
		moves.add(new PossibleMoves(-1,0, true));
		moves.add(new PossibleMoves(0,1, true));
		moves.add(new PossibleMoves(0,-1, true));

		moves.add(new PossibleMoves(1,1, true));
		moves.add(new PossibleMoves(1,-1, true));
		moves.add(new PossibleMoves(-1,1, true));
		moves.add(new PossibleMoves(-1,-1, true));
		return moves;
	}

	@Override
	public Move getMove(ChessMove chessMove) {
		Move move = Move.MOVE_UNKOWN;
		move = chessMove.moveOrCapture(1, 0, move);
		move = chessMove.moveOrCapture(-1, 0, move);
		move = chessMove.moveOrCapture(0, 1, move);
		move = chessMove.moveOrCapture(0, -1, move);
		move = chessMove.moveOrCapture(1, 1, move);
		move = chessMove.moveOrCapture(1, -1, move);
		move = chessMove.moveOrCapture(-1, 1, move);
		move = chessMove.moveOrCapture(-1, -1, move);
		return move;
	}
}
