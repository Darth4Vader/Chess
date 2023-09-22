package ChessDataTypes;

public class ChessException extends Exception {
	
	public static enum ChessExceptionType {
		FEN_LOAD,
		PGN_LOAD
	}
	
	private ChessExceptionType type;
	private String message;
	
	public ChessException(ChessExceptionType type, String message) {
		this.type = type;
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		String exception = "";
		switch(this.type) {
		case FEN_LOAD:
			exception = "Loading FEN string Exception: ";
			break;
		case PGN_LOAD:
			exception = "Loading PGN string Exception: ";
			break;
		}
		return exception + this.message;
	}
}
