package ChessDataTypes;

import javax.management.MalformedObjectNameException;

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
		Move move = Move.MOVE_UNKOWN;
		if(type == Piece.PAWN) {
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
						return checkPawnCaptures();
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
					return checkPawnCaptures();
			}
		}
		else if(type == Piece.KNIGHT) {
			if(rank-1 == toRank) {
				if(file-2 == toFile)
					return isMoveOrCapture();
				if(file+2 == toFile)
					return isMoveOrCapture();
			}
			if(rank+1 == toRank) {
				if(file-2 == toFile)
					return isMoveOrCapture();
				if(file+2 == toFile)
					return isMoveOrCapture();
			}
			if(rank-2 == toRank) {
				if(file-1 == toFile)
					return isMoveOrCapture();
				if(file+1 == toFile)
					return isMoveOrCapture();
			}
			if(rank+2 == toRank) {
				if(file-1 == toFile)
					return isMoveOrCapture();
				if(file+1 == toFile)
					return isMoveOrCapture();
			}
		}
		else if(type == Piece.KING) {
			if(Math.abs(rank-toRank) == 1 && file == toFile)
				return isMoveOrCapture();
			if(rank==toRank && Math.abs(file-toFile) == 1)
				return isMoveOrCapture();
			if(Math.abs(rank-toRank) == 1 && Math.abs(file-toFile) == 1)
				return isMoveOrCapture();
			return checkCastling();
		}
		else {
			if(type == Piece.BISHOP || type == Piece.QUEEN) {
				move = moveOrCapture(1, 1, move);
				move = moveOrCapture(1, -1, move);
				move = moveOrCapture(-1, 1, move);
				move = moveOrCapture(-1, -1, move);
			}
			if(type == Piece.ROOK || type == Piece.QUEEN) {
				if(move == Move.MOVE_UNKOWN) {
					move = moveOrCapture(1, 0, move);
					move = moveOrCapture(-1, 0, move);
					move = moveOrCapture(0, 1, move);
					move = moveOrCapture(0, -1, move);
				}
			}
		}
		return move;
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
	
	private Move isMoveOrCapture() {
		if(moveToPosition.isEmpty())
			return Move.MOVE;
		else
			return isCapture();
	}
	
	private Move isCapture() {
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
	
	private Move checkPawnCaptures() {
		Move move = isCapture();
		if(!moveToPosition.isEmpty()) return move;
		if(file > 0)
			move = checkPawnEnPasent(0, -1, move);
		if(file < FILE-1)
			move = checkPawnEnPasent(0, 1, move);
		return move;
	}
	
	private Move checkPawnEnPasent(int rankLoop, int fileLoop, Move move) {
		if(move != Move.MOVE_UNKOWN) return move;
		int rank = this.rank + rankLoop;
		int file = this.file + fileLoop;
		ChessPosition playPosition = chess.getChessPosition(rank, file);
		boolean isLegal = checkPawnEnPasent(playPosition);
		if(isLegal) {
			this.playPosition = playPosition;
			return Move.EN_PASSANT;
		}
		return Move.MOVE_UNKOWN;
	}
	
	/**
	 * Special Chess move only for <strong>PAWN</strong> pieces.
	 * @param piece
	 * @return
	 */
	private boolean checkPawnEnPasent(ChessPosition position) {
		if(position == null) return false;
		ChessPiece piece = position.getChessPiece();
		if(piece == null || currentPiece.isColor(piece) || !piece.isPiece(Piece.PAWN)) return false;
		ChessMove lastOppositeMove = chess.getLastMove();
		if(lastOppositeMove != null) {
			ChessPosition lastMovedPosition = lastOppositeMove.getMoveToPosition();
			if(position.equals(lastMovedPosition)) {
				if(lastOppositeMove.getMoveName() == Move.PAWN_START) {
					int rank = lastMovedPosition.getRank(), file = lastMovedPosition.getFile();
					if(rank == this.rank && (file+1 == this.file || file-1 == this.file))
						if((rank-1 == toRank || rank+1 == toRank) && file == toFile)
							return true;
				}
			}
		}
		return false;
	}
	
	private Move checkCastling() {
		if(!moveToPosition.isEmpty()) return Move.MOVE_UNKOWN;
		ChessPosition position;
		if(this.file < this.toFile)
			position = chess.getChessPosition(rank, 7);
		else
			position = chess.getChessPosition(rank, 0);
		if(checkCastling(position)) {
			playPosition = position;
			return Move.CASTLING;
		}
		return Move.MOVE_UNKOWN;
	}
	
	private boolean checkCastling(ChessPosition position) {
		if(!(toFile == 2 || toFile == 6) && !(toRank == 0 || toRank == 7)) return false;
		if(position == null) return false;
		ChessPiece piece = position.getChessPiece();
		if(piece == null || (!this.currentPosition.getChessPiece().isFirstMove() && !piece.isFirstMove())) return false;
		if(!piece.isPiece(Piece.ROOK) || !currentPiece.isColor(piece)) return false;
		int rank = position.getRank(), file = position.getFile();
		if(rank != this.rank) return false;
		if(this.file < file) {
			for(int i = this.file+1;i < file; i++)
				if(!chess.isPositionEmpty(rank, i))
					return false;
		}
		else
			for(int i = this.file-1;i > 0; i--)
				if(!chess.isPositionEmpty(rank, i))
					return false;
		return true;
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
	
	public void initiateChange() {
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
			if(isPromotion)
				piece.setType(toType);
			moveToPosition.setChessPiece(piece);
			chess.history.add(this);
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
}