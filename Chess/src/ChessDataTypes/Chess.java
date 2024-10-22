package ChessDataTypes;
import java.util.ArrayList;
import java.util.List;

import ChessDataTypes.ChessException.ChessExceptionType;

public class Chess implements ChessData {
	
	private final ChessPosition[][] board;
	public final List<ChessMove> history;
	private TurnColor currentTurn;
	private boolean isGameActivate;
	private boolean isWhiteInCheck;
	private boolean isBlackInCheck;
	private ChessPiece blackKing, whiteKing;
	private String startingFEN;
	
	public Chess() throws ChessException {
		this(START_FEN);
	}
	
	public Chess(String fen) throws ChessException {
		this.board = new ChessPosition[RANK][FILE];
		this.history = new ArrayList<>();
		this.isGameActivate = true;
		this.currentTurn = TurnColor.WHITE;
		initiateBoard();
		loadFEN(fen);
		this.startingFEN = fen;
	}
	
	public Chess(Chess chess) {
		this.board = new ChessPosition[RANK][FILE];
		this.history = new ArrayList<>();
		this.history.addAll(chess.history);
		this.isGameActivate = chess.isGameActivate;
		this.currentTurn = chess.currentTurn;
		initiateBoard();
		for(int rank = 0;rank < RANK; rank++) {
			for(int file = 0;file < FILE; file++) {
				ChessPiece piece =  chess.getChessPiece(rank, file);
				if(piece != null) {
					ChessPiece newPiece = piece.newInstance(); 
					System.out.println(newPiece);
							//new ChessPiece(piece);
					setChessPiece(rank, file, newPiece);
				}
			}
		}
		//this.startingFEN = chess.getFEN();
	}
	
	private void initiateBoard() {
		TurnColor backgroundColor = TurnColor.WHITE;
		for(int rank = 0;rank < RANK; rank++) {
			for(int file = 0;file < FILE; file++) {
				setChessPosition(backgroundColor, rank, file);
				backgroundColor = ChessData.getOppositeColor(backgroundColor);
			}
			backgroundColor = ChessData.getOppositeColor(backgroundColor);
		}		
	}
	
	private void cleanChessBoard() {
		for(int rank = 0;rank < RANK; rank++)
			for(int file = 0;file < FILE; file++)
				setChessPiece(rank, file, null);
		this.whiteKing = null;
		this.blackKing = null;
	}
	
	public ChessPosition getChessPosition(int rank, int file) {
		if(0 <= rank && rank < RANK && 0 <= file && file < FILE)
			return board[rank][file];
		return null;
	}
	
	/*public static ChessPosition getChessPosition(Chess chess, int rank, int file) {
		return chess != null ? chess.getChessPosition(rank, file) : null;
	}*/
	
	private void setChessPosition(TurnColor backgroundColor, int rank, int file) {
		if(0 <= rank && rank < RANK && 0 <= file && file < FILE)
			board[rank][file] = new ChessPosition(backgroundColor, rank, file);
	}
	
	public ChessPiece getChessPiece(int rank, int file) {
		ChessPosition position = getChessPosition(rank, file);
		return position == null ? null : position.getChessPiece();
	}
	
	private void setChessPiece(int rank, int file, ChessPiece piece) {
		ChessPosition position = getChessPosition(rank, file);
		if(position != null) {
			position.setChessPiece(piece);
			if(piece != null) {
				TurnColor color = piece.getColor();
				Piece type = piece.getType();
				if(type == Piece.KING) {
					if(color == TurnColor.WHITE)
						this.whiteKing = piece;
					else if(color == TurnColor.BLACK)
						this.blackKing = piece;
				}
			}
		}
	}
	
	public boolean isPositionEmpty(int rank, int file) {
		ChessPosition position = getChessPosition(rank, file);
		return position != null && position.isEmpty();
	}
	
	public boolean isPathToPositionEmpty(ChessPosition pos1, ChessPosition pos2) {
		int rank1 = pos1.getRank(), file1 = pos1.getFile();
		int rank2 = pos2.getRank(), file2 = pos2.getFile();
		if(rank1 != rank2) return false;
		int fromFile, toFile;
		if(file1 < file2) {
			fromFile = file1;
			toFile = file2;
		}
		else {
			fromFile = file2;
			toFile = file1;
		}
		for(int i = fromFile+1;i < toFile; i++)
			if(!isPositionEmpty(rank1, i))
				return false;
		return true;
	}
	
	public TurnColor getCurrentTurn() {
		return this.currentTurn;
	}
	
	public void loadFEN(int history) throws ChessException {
		loadFEN(this.history.get(history).getFEN());
	}
	
	public void loadFEN(String FEN) throws ChessException {
		int rank = 0;
		int file = 0;
		cleanChessBoard();
		for(char c : FEN.toCharArray()) {
			if(rank >= RANK)
				throw new ChessException(ChessExceptionType.FEN_LOAD, "The Number of allowed rows are 8");
			if(c == '/')  {
				if(file != FILE)
					throw new ChessException(ChessExceptionType.FEN_LOAD, "The Size of allowed columns are 8");
				rank++;
				file = 0;
			}
			else if(file < FILE) {
				if(Character.isDigit(c)){
					int i = Character.getNumericValue(c);
					if(1 <= i && i <= FILE) {
						file += i;
						if(file > FILE)
							throw new ChessException(ChessExceptionType.FEN_LOAD, "The Size of allowed columns are 8");
					}
					else
						throw new ChessException(ChessExceptionType.FEN_LOAD, "The Size of allowed columns are 8");
				}
				else if(Character.isAlphabetic(c)) {
					Piece type = ChessData.getChessPieceNumber(Character.toLowerCase(c));
					if(type != Piece.PIECE_UNKOWN) {
						TurnColor color = Character.isUpperCase(c) ? TurnColor.WHITE : TurnColor.BLACK;
						ChessPiece piece = ChessPiece.newInstance(ChessData.getChessPieceClass(type), color); 
								//new ChessPiece(type, color);
						setChessPiece(rank, file, piece);
						file++;
					}
					else
						throw new ChessException(ChessExceptionType.FEN_LOAD, "The name " + c + " is not a legal chess piece name");
				}
				else
					throw new ChessException(ChessExceptionType.FEN_LOAD, "The only allowed characters are letters, digits and the char '/'");
			}
			else
				throw new ChessException(ChessExceptionType.FEN_LOAD, "The Size of allowed columns are 8");
		}
		if(rank != RANK-1 && file != FILE)
			throw new ChessException(ChessExceptionType.FEN_LOAD, "The size of the givn FEN string board is not 8x8");
	}
	
	public boolean containsMove(ChessMove move) {
		String name = move.getUpdates();
		for(ChessMove moved : history) {
			String named = moved.getUpdates();
			if(named.equals(name))
				return true;
		}
		return false;
	}
	
	/*public String getPGN() {
		int count = 2;
		int number = 0;
		String str = "";
		for(ChessMove move : history) {
			if(count == 2) {
				if(number != 0)
					str+= " ";
				number++;
				str += number + ".";
				count = 0;
			}
			String pgn = move.getPGN();
			str += " " + pgn;
			count++;
		}
		return str;
	}*/
	
	
	public boolean isGameActivate() {	
		//System.out.println(this.blackKing.getPosition());
		return this.isGameActivate; /*&& this.blackKing.getPosition() != null;*/
	}
	
	public ChessMove getLastMove() {
		int lastIndex = this.history.size()-1;
		if(lastIndex >= 0)
			return this.history.get(lastIndex);
		return null;
	}
	
	public TurnColor getWinner() {
		if(this.isGameActivate) return TurnColor.UNKOWN;
		return ChessData.getOppositeColor(this.currentTurn);
	}
	
	public void switchTurn() {
		switchTurn(currentTurn);
	}
	
	public void switchTurn(TurnColor color) {
		if(!this.isGameActivate) return;
		updateColorInCheck(color);
		if(isColorInCheck(color) && isMate(color))
			this.isGameActivate = false;
		if(this.isGameActivate) {
			boolean isDraw = true;
			for(int rank = 0;rank < RANK && isDraw; rank++) {
				for(int file = 0;file < FILE && isDraw; file++) {
					ChessPiece piece = getChessPiece(rank, file);
					if(piece != null && piece.getType() != Piece.KING)
						isDraw = false;
				}
			}
			if(isDraw) {
				this.isGameActivate = false;
				this.currentTurn = TurnColor.DRAW;
			}
		}
		if(this.isGameActivate)
			this.currentTurn = ChessData.getOppositeColor(color);
	}
	
	public boolean isChecked(TurnColor color) {
		ChessPiece kingPiece = getKingPiece(color);
		if(kingPiece == null) return false;
		ChessPosition kingPosition = kingPiece.getPosition();
		if(kingPosition == null || kingPiece.getType() != Piece.KING) return false;
		for(int rank = 0;rank < RANK; rank++) {
			for(int file = 0;file < FILE; file++) {
				ChessPosition position = getChessPosition(rank, file);
				if(position != null && !position.equals(kingPosition)) {
					ChessPiece piece = position.getChessPiece();
					if(piece != null && piece.getColor() != color) {
						ChessMoves moves = new ChessMoves(this, position);
						if(moves.isPossibleCheck(kingPosition))
							return true;
					}
				}
			}
		}
		return false;
	}
	
	private boolean isMate(TurnColor color) {
		ChessPiece kingPiece = getKingPiece(color);
		if(kingPiece == null) return false;
		ChessPosition kingPosition = kingPiece.getPosition();
		if(kingPosition == null || kingPiece.getType() != Piece.KING) return false;
		ChessMoves kingMoves = new LegalChessMoves(this, kingPosition);
		for(int rank = 0;rank < RANK; rank++) {
			for(int file = 0;file < FILE; file++) {
				ChessPosition position = getChessPosition(rank, file);
				if(position != null && !position.equals(kingPosition)) {
					ChessPiece piece = position.getChessPiece();
					if(piece != null && piece.getColor() != color) {
						ChessMoves moves = new LegalChessMoves(this, position);	
						if(moves.canCheck()) {
							if(!canBeCaptured(position) && !kingMoves.canAvoidCheck(moves))
								return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	private boolean canBeCaptured(ChessPosition toPosition) {
		if(toPosition == null) return false;
		ChessPiece toPiece = toPosition.getChessPiece();
		if(toPiece == null) return false;
		final TurnColor color = toPiece.getColor();
		for(int rank = 0;rank < RANK; rank++) {
			for(int file = 0;file < FILE; file++) {
				ChessPosition position = this.getChessPosition(rank, file);
				if(position != null) {
					ChessPiece piece = position.getChessPiece();
					if(piece != null && piece.getColor() != color) {
						ChessMoves moves = new LegalChessMoves(this, position);
						if(moves.isPossibleMove(toPosition))
							return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean isColorInCheck(TurnColor color) {
		if(color == TurnColor.WHITE) return this.isWhiteInCheck;
		if(color == TurnColor.BLACK) return this.isBlackInCheck;
		return false;
	}
	
	public void updateColorInCheck(TurnColor color) {
		this.isWhiteInCheck = color == TurnColor.WHITE ? isChecked(TurnColor.WHITE) : this.isWhiteInCheck;
		this.isBlackInCheck = color == TurnColor.BLACK ? isChecked(TurnColor.BLACK) : this.isBlackInCheck;
	}
	
	private ChessPiece getKingPiece(TurnColor color) {
		return color == TurnColor.WHITE ? this.whiteKing : color == TurnColor.BLACK ?  this.blackKing : null;
	}
	
	public String getFEN() {
		String str = "";
		int count = 0;
		for(int rank = 0;rank < RANK; rank++) {
			for(int file = 0;file < FILE; file++) {
				ChessPiece piece = getChessPiece(rank, file);
				if(piece != null) {
					if(count != 0) str += count;
					count = 0;
					TurnColor color = piece.getColor();
					Piece type = piece.getType();
					char c = ChessData.getChessPieceName(type);
					if(c != ' ') {
						if(color == TurnColor.WHITE)
							c = Character.toUpperCase(c);
						str += c;
					}
				}
				else count++;
			}
			if(count != 0)
				str += count;
			count = 0;
			if(rank < RANK-1)
				str+='/';
		}
		return str;
	}
	
	public String getPGN() {
		String str = "[FEN \"" + this.startingFEN + " b KQkq - 0 1\"]\n";
		int idx = 1;
		int count = 0;
		for(ChessMove move : history) {
			if(count == 0) {
				str += idx + ". ";
				count++;
			}
			else {
				count = 0;
				idx++;
			}
			str += move.getPGN() + " ";
		}
		return str;
	}
	
	public boolean checkPosible(ChessPosition currentPosition, ChessPosition moveToPosition) {
		ChessMove move = new LegalChessMove(this, currentPosition, moveToPosition/*, false*/ );
		return move.isLegal();
	}
	
	public ChessMoves getPossibleMoves(ChessPosition currentPosition) {
		return new LegalChessMoves(this, currentPosition);
	}
	
	@Override
	public String toString() {
		return getFEN();
	}
}