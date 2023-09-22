package ChessDataTypes;

public interface ChessData {
	
	public static final String START_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
	
	public static final int RANK = 8, FILE = 8;
	
	public static enum Piece {
		PAWN,
		BISHOP,
		KNIGHT,
		ROOK,
		QUEEN,
		KING,
		PIECE_UNKOWN
	}
	
	public static enum TurnColor {
		BLACK,
		WHITE,
		DRAW,
		UNKOWN
	}
	
	public static enum Move {
		MOVE,
		CAPTURE,
		EN_PASSANT,
		PROMOTION,
		CASTLING,
		PAWN_START,
		CHECK,
		IN_CHECK,
		MOVE_UNKOWN
		
	}
	
	public static TurnColor getOppositeColor(TurnColor turn) {
		if(turn == TurnColor.WHITE) return TurnColor.BLACK;
		if(turn == TurnColor.BLACK) return TurnColor.WHITE;
		if(turn == TurnColor.DRAW) return TurnColor.DRAW;
		return TurnColor.UNKOWN;
	}
	
	public static String getFileName(ChessPiece piece) {
		return piece != null ? getFileName(piece.getType(), piece.getColor()) : "";
	}
	
	public static String getFileName(Piece type, TurnColor color) {
		String str = "";
		if(type != Piece.PIECE_UNKOWN && color != TurnColor.UNKOWN) {
			char t = getChessPieceName(type);
			if(t != ' ') {
				char c = getColorName(color);
				if(c != ' ') {
					str = t + "_" + c;
				}
				else {
					
				}
			}
			else {
				
			}
		}
		else {
			
		}
		System.out.println(str);
		return str;
	}
	
	public static char getColorName(TurnColor color) {
		if(color == TurnColor.WHITE) return 'w';
		if(color == TurnColor.BLACK) return 'b';
		return ' ';
	}
	
	public static Piece getChessPieceNumber(char c) {
		if(c == 'p') return Piece.PAWN;
		else if(c == 'r') return Piece.ROOK;
		else if(c == 'n') return Piece.KNIGHT;
		else if(c == 'b') return Piece.BISHOP;
		else if(c == 'q') return Piece.QUEEN;
		else if(c == 'k') return Piece.KING;
		return Piece.PIECE_UNKOWN;
	}
	
	public static char getChessPieceName(Piece type) {
		if(type == Piece.PAWN) return 'p';
		else if(type == Piece.ROOK) return 'r';
		else if(type == Piece.KNIGHT) return 'n';
		else if(type == Piece.BISHOP) return 'b';
		else if(type == Piece.QUEEN) return 'q';
		else if(type == Piece.KING) return 'k';
		return '?';
	}
}