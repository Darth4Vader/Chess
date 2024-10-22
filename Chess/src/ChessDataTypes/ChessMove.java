package ChessDataTypes;

import javax.management.MalformedObjectNameException;

import ChessDataTypes.ChessData.Move;
import ChessDataTypes.ChessData.Piece;
import ChessDataTypes.ChessException.ChessExceptionType;

public class ChessMove implements ChessData {
	
	protected Chess chess;
	private boolean isPromotion;
	protected boolean pgnRank;
	protected boolean pgnFile;
	
	protected boolean isCheckMate;
	private int rank, file, toRank, toFile;
	protected final TurnColor color;
	private Piece type, toType;
	private ChessPosition currentPosition, moveToPosition, playPosition;
	private ChessPiece currentPiece;
	protected Move moveType;
	private String FEN;
	
	public ChessMove(Chess chess, ChessPosition currentPosition, ChessPosition moveToPosition) {
		this.chess = chess;
		this.currentPosition = currentPosition;
		this.moveToPosition = moveToPosition;
		rank = currentPosition.getRank();file = currentPosition.getFile();
		toRank = moveToPosition.getRank();toFile = moveToPosition.getFile();
		this.currentPiece = currentPosition.getChessPiece();
		this.color = this.currentPiece.getColor();
		this.type = this.currentPiece.getType();
		this.moveType = getMove();
		if(type == Piece.PAWN && (this.moveType == Move.MOVE || this.moveType == Move.CAPTURE)) {
			if(color == TurnColor.WHITE && toRank == 0) {
				isPromotion = true;
				toType = Piece.QUEEN;
			}
			else if(color == TurnColor.BLACK && toRank == 7) {
				isPromotion = true;
				toType = Piece.QUEEN;
			}
		}
	}
	
	/**
	 * Activate when searching for a check
	 * @param move
	 */
	public ChessMove(ChessMove move, Chess chess) {
		this.chess = chess;
		this.currentPosition = cloneWhenCheck(move.currentPosition);
		this.moveToPosition = cloneWhenCheck(move.moveToPosition);
		this.playPosition = cloneWhenCheck(move.playPosition);
		if(currentPosition != null && moveToPosition != null) {
			rank = currentPosition.getRank();file = currentPosition.getFile();
			toRank = moveToPosition.getRank();toFile = moveToPosition.getFile();
		}
		this.currentPiece = this.currentPosition.getChessPiece();
		color = this.currentPiece.getColor();
		type = this.currentPiece.getType();
		this.moveType = move.moveType;
	}
	
	public ChessMove(Chess chess, String pgn, TurnColor color) throws ChessException {
		this.chess = chess;
		this.color = color;
		rank=-1;file=-1;
		int idx = 0;
		if(pgn.equals("O-O-O")) {
			this.type = Piece.KING;
			this.moveType = Move.CASTLING;
			this.toFile = 2;
		}
		else if(pgn.equals("O-O")) {
			this.moveType = Move.CASTLING;
			this.type = Piece.KING;
			this.toFile = 6;
		}
		else {
			char c = pgn.charAt(idx);
			if(Character.isUpperCase(c)) {
				type = ChessData.getChessPieceNumber(Character.toLowerCase(c));
				if(type == Piece.PIECE_UNKOWN || type == Piece.PAWN)
					throw new ChessException(ChessExceptionType.PGN_LOAD, "The piece " + c + " is not valid");
				idx++;
			}
			else
				type = Piece.PAWN;
			c = pgn.charAt(idx);
			int pgnLength = minLength(pgn, '=', '+', '#');
			if(c == 'x') {
				moveType = Move.CAPTURE;
				if(type == Piece.PAWN)
					throw new ChessException(ChessExceptionType.PGN_LOAD, "a Pawn Capture must include the originate pawn column");
				idx++;
			}
			else if((type != Piece.PAWN && pgnLength >= 3) || (pgn.contains("x"))) {
				if(Character.isLetter(c)) {
					file = ChessPosition.getFileNumber(c);
					checkPositionValid(file);
					pgnFile = true;
				}
				else if(Character.isDigit(c)) {
					if(type == Piece.PAWN)
						throw new ChessException(ChessExceptionType.PGN_LOAD, "Pawn current rank is not valid in pgn");
					rank = ChessPosition.getRankNumber(Character.getNumericValue(c));
					checkPositionValid(rank);
					pgnRank = true;
				}
				else
					throw new ChessException(ChessExceptionType.PGN_LOAD, "the character '" + c + "' is not valid");
				idx++;
				c = pgn.charAt(idx);
				if(c == 'x') {
					moveType = Move.CAPTURE;
					idx++;
				}
			}
			toFile = ChessPosition.getFileNumber(pgn.charAt(idx++));
			checkPositionValid(toFile);
			toRank = ChessPosition.getRankNumber(Character.getNumericValue(pgn.charAt(idx++)));
			checkPositionValid(toRank);
			if(pgn.length() > idx && pgn.charAt(idx+1) == '=') {
				if(type == Piece.PAWN) {
					idx++;
					isPromotion = true;
					c = pgn.charAt(idx++);
					if(Character.isUpperCase(c))
						toType = ChessData.getChessPieceNumber(Character.toLowerCase(c));
					else
						throw new ChessException(ChessExceptionType.PGN_LOAD, "The piece " + c + " is not valid");
					if(toType == Piece.KING || toType == Piece.PAWN)
						throw new ChessException(ChessExceptionType.PGN_LOAD, "Cannot Promote a PAWN to the chess type" + toType);
				}
				else
					throw new ChessException(ChessExceptionType.PGN_LOAD, "exceeding characters at the end of the pgn");
			}
		}
	}
	
	private void checkPositionValid(int num) throws ChessException {
		if(num < 0 || num > 7)
			throw new ChessException(ChessExceptionType.PGN_LOAD, "The position " + num + " is not valid");
	}
	
	private int minLength(String str, char... arr) {
		int min = str.length();
		for(char c : arr) {
			int x = str.indexOf(c);
			if(x != -1)
				min = Math.min(x, min);
		}
		return min - 1;
	}
	
	private ChessPosition cloneWhenCheck(ChessPosition position) {
		if(position != null)
			return this.chess.getChessPosition(position.getRank(), position.getFile());
		return null;
	}
	
	
	public TurnColor getColor() {
		return this.color;
	}
	
	protected Move getMove() {
		if(currentPosition == null || moveToPosition == null) return Move.MOVE_UNKOWN;
		if(currentPosition.equals(moveToPosition)) return Move.MOVE_UNKOWN;
		ChessPiece piece = currentPosition.getChessPiece();
		return piece.getMove(this);
	}
	
	public Move moveOrCapture(int rankLoop, int fileLoop, Move move) {
		if(move != Move.MOVE_UNKOWN || (rankLoop == 0 && fileLoop == 0)) return move;
		int rank = this.rank + rankLoop;
		int file = this.file + fileLoop;
		while(0 <= rank && rank < RANK && 0 <= file && file < FILE) {
			ChessPosition curPosition = chess.getChessPosition(rank, file);
			if(curPosition != null && curPosition.equals(moveToPosition))
				return isMoveOrCapture();
			if(!curPosition.isEmpty()) break;
			rank += rankLoop;
			file += fileLoop;
		}
		return Move.MOVE_UNKOWN;
	}
	
	public boolean isCheck() {
		return moveType == Move.CHECK;
	}
	
	protected Move isMoveOrCapture() {
		if(moveToPosition.isEmpty())
			return Move.MOVE;
		else
			return isCapture();
	}
	
	protected Move isCapture() {
		if(!moveToPosition.isEmpty()) {
			TurnColor toColor = moveToPosition.getChessPiece().getColor();
			Piece toType = moveToPosition.getChessPiece().getType();
			if(toColor != color) {
				if(toType == Piece.KING)
					return Move.CHECK;
				else {
					playPosition = moveToPosition;
					return Move.CAPTURE;
				}
			}
		}
		return Move.MOVE_UNKOWN;
	}
	
	public void setPlayPosition(ChessPosition position) {
		this.playPosition = position;
	}
	
	public ChessPosition getMoveToPosition() {
		return this.moveToPosition;
	}
	
	public boolean isLegal() {
		return moveType != Move.MOVE_UNKOWN && moveType != Move.CHECK;
	}
	
	public String getFEN() {
		return this.FEN;
	}
	
	public Move getMoveName() {
		return moveType;
	}
	
	private ChessPosition getCastelingRookMove() {
		if(toFile > file)
			return chess.getChessPosition(rank, file+1);
		return chess.getChessPosition(rank, file-1);
	}
	
	public void initiateChange() throws PromotionChooseException {
		ChessPiece piece = currentPosition.getChessPiece();
		if(piece != null && this.moveType != Move.MOVE_UNKOWN && this.moveType != Move.CHECK) {
			if(playPosition != null) {
				if((moveType == Move.CAPTURE || moveType == Move.EN_PASSANT || moveType == Move.CASTLING) && playPosition != null) {
					ChessPiece rookPiece = playPosition.getChessPiece();
					playPosition.setChessPiece(null);
					if(moveType == Move.CASTLING) {
						ChessPosition moveRook = getCastelingRookMove();
						moveRook.setChessPiece(rookPiece);
					}
				}
			}
			piece.setFirstTurn(this);
			currentPosition.setChessPiece(null);
			/*if(isPromotion)
				throws new PromotionChooseException("Must Choose Promotion");
				piece = piece.newInstance(ChessData.getChessPieceClass(toType));*/
			moveToPosition.setChessPiece(piece);
			chess.history.add(this);
			if(isPromotion)
					throw new PromotionChooseException("Must Choose Promotion");
		}
	}
	
	public class PromotionChooseException extends Exception {
		private static final long serialVersionUID = 1L;

		public PromotionChooseException(String message) {
			super(message);
		}
		
		public void setPromotion(Piece type) {
			toType = type;
			if(isPromotion) {
				ChessPiece piece = moveToPosition.getChessPiece();
				piece = piece.newInstance(ChessData.getChessPieceClass(toType));
				moveToPosition.setChessPiece(piece);
			}
		}
	}
	
	public String getPGN() {
		String str = "";
		if(type == Piece.KING && this.moveType == Move.CASTLING) {
			if(toFile < 3)
				return "O-O-O";
			else
				return "O-O";			
		}
		if(type != Piece.PAWN) {
			str += Character.toUpperCase(ChessData.getChessPieceName(type));
			if(pgnFile)
				str += ChessPosition.getColumnName(file);
			else if(pgnRank)
				str += ChessPosition.getRowName(rank);
		}
		if(this.moveType == Move.CAPTURE || this.moveType == Move.EN_PASSANT) {
			if(this.type == Piece.PAWN) {
				str += ChessPosition.getColumnName(file);
			}
			if(this.moveType == Move.EN_PASSANT && this.type != Piece.PAWN) {
				System.out.println("Error: ");
			}
			else
				str += 'x';
		}
		str += "" + ChessPosition.getColumnName(toFile) + "" + ChessPosition.getRowName(toRank);
		if(type == Piece.PAWN && isPromotion)
			str += "=" + Character.toUpperCase(ChessData.getChessPieceName(toType));
		if(isCheckMate)
			str += '#';
		else {
			if(this.moveType == Move.CHECK)
				str += '+';
		}
		return str;
	}
	
	public String getUpdates() {
		//System.out.println("0(5)11_21{0-15}");
		ChessPiece piece = this.currentPiece;
		Piece type = piece.getType();
		TurnColor color = piece.getColor();
		String str = color+"("+type+")"+currentPosition.getRank()+""+currentPosition.getFile()+'_'+moveToPosition.getRank()+""+moveToPosition.getFile();
		//System.out.println(color+'('+type+')');
		//System.out.println(str);
		if(moveType != Move.MOVE_UNKOWN) {
			str += "{"+moveType;
			if(moveType != Move.MOVE && moveType != Move.PAWN_START && moveType != Move.CHECK)
				if(playPosition != null)
					str += "-"+playPosition.getRank()+""+playPosition.getFile();
			str += "}";
		}
		return str;
	}
	
	@Override
	public String toString() {
		return moveType + " - " + type + " : " + currentPosition + " -> " + moveToPosition;
	}

	public ChessPosition getCurrentPosition() {
		return currentPosition;
	}
	
	
	public Chess getChess() {
		return chess;
	}
}