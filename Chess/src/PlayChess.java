import java.util.Scanner;

import ChessDataTypes.Chess;
import ChessDataTypes.ChessException;
import ChessDataTypes.ChessData.TurnColor;
import ChessUI.ChessMain;

public class PlayChess {

	public static Scanner input = new Scanner(System.in);

	public static void main(String[] args) throws ChessException {
		frame = new ChessMain();
		Chess chess = frame.getChessBoard();
	}
	
	public static ChessMain frame;

}
