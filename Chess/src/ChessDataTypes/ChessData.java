package ChessDataTypes;

import ChessDataTypes.ChessData.Piece;

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
		return switch (color) {
			case WHITE -> 'w';
			case BLACK -> 'b';
			default -> ' ';
		};
	}
	
	public static Piece getChessPieceNumber(char c) {
		return switch (c) {
			case 'p' -> Piece.PAWN;
			case 'r' -> Piece.ROOK;
			case 'n' -> Piece.KNIGHT;
			case 'b' -> Piece.BISHOP;
			case 'q' -> Piece.QUEEN;
			case 'k' -> Piece.KING;
			default -> Piece.PIECE_UNKOWN;
		};
	}
	
	public static char getChessPieceName(Piece type) {
		return switch (type) {
			case PAWN -> 'p';
			case ROOK -> 'r';
			case KNIGHT -> 'n';
			case BISHOP -> 'b';
			case QUEEN -> 'q';
			case KING -> 'k';
			default -> '?';
		};
	}
	
	public static Class<? extends ChessPiece> getChessPieceClass(Piece type) {
		return switch(type) {
            case PAWN -> Pawn.class;
            case ROOK -> Rook.class;
            case KNIGHT -> Knight.class;
            case BISHOP -> Bishop.class;
            case QUEEN -> Queen.class;
            case KING -> King.class;
            default -> null;
		};
	}
}