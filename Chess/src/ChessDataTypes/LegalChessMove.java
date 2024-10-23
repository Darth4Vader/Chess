package ChessDataTypes;

import ChessDataTypes.ChessData.TurnColor;

public class LegalChessMove extends ChessMove {
	
	public LegalChessMove(Chess chess, ChessPosition currentPosition, ChessPosition moveToPosition) {
		super(chess, currentPosition, moveToPosition);
		ChessPiece mainPiece = currentPosition.getChessPiece();
		Piece type = mainPiece.getType();
		boolean moreThenOneType = false;
		ChessPosition position = null;
		if(this.moveType != Move.MOVE_UNKOWN)
			if(type != Piece.KING && type != Piece.PAWN) {
				for(int rank = 0;rank < RANK && moreThenOneType != true; rank++) {
					for(int file = 0;file < FILE && moreThenOneType != true; file++) {
						position = this.chess.getChessPosition(rank, file);
						if(!currentPosition.equals(position)) {
							ChessPiece piece = position.getChessPiece();
							if(mainPiece.equalsPieceTypeColor(piece)) {
								ChessMoves moves = new ChessMoves(this.chess, position);
								if(moves.isPossibleMove(moveToPosition))
									moreThenOneType = true;
							}
						}
					}
				}
				if(moreThenOneType) {
					if(position.getFile() == currentPosition.getFile())
						pgnRank = true;
					else
						pgnFile = true;
				}
		}
	}
	
	@Override
	protected Move getMove() {
		Move moveType = super.getMove();
		// check if king is in check and if move is legal
		if(moveType != Move.MOVE_UNKOWN) {
			Chess chess = new Chess(this.chess);
			ChessMove move = new ChessMove(this, chess);
			try {
				move.initiateChange();
			} catch (PromotionChooseException e) {
				e.setPromotion(Piece.QUEEN);
			}
			TurnColor color = chess.getCurrentTurn();
			chess.updateColorInCheck(color);
			if(chess.isColorInCheck(color))
				moveType = Move.MOVE_UNKOWN;
			this.isCheckMate = !chess.isGameActivate();
		}
		return moveType;
	}
}