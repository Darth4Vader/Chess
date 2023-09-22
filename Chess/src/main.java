import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ChessDataTypes.Chess;
import ChessDataTypes.ChessData;
import ChessDataTypes.ChessException;
import ChessDataTypes.ChessMove;
import ChessDataTypes.ChessMoves;
import ChessDataTypes.ChessPiece;
import ChessDataTypes.ChessPosition;
import ChessDataTypes.LegalChessMoves;
import ChessDataTypes.ChessData.TurnColor;
import ChessUI.ChessMain;

public class main implements ChessData {
	
	public static Scanner input = new Scanner(System.in);

	public static void main(String[] args) {
		/*byte b = (byte) 1;
		b = (byte) (~b);
		System.out.println(b);
		System.out.println(Integer.toBinaryString(~~b));*/
		//new ChessMain();
		//new ChessMain("4k2r/6r1/8/8/8/8/3R4/R3K3");
		
		/*List<String> list = new ArrayList<>();
		list.add("rnbqkbnr/ppppp2p/5p2/6pQ/3PP3/8/PPP2PPP/RNB1KBNR");
		list.add("r1b1k1nr/pppp1ppp/2n5/4P3/8/2Q2N2/P1P1PPPP/RNq1KB1R");
		list.add("3Q4/p2R3p/1pk2Kq1/2p2p2/P7/1P4P1/5P1P/4r3");
		for(String str : list) {
			System.out.println(str);
			Chess chess = new Chess(str);
			if(chess.isCheckedMate(Chess.WHITE))
				System.out.println("Black Won");
			else if(chess.isCheckedMate(Chess.BLACK))
				System.out.println("White Won");
			else new ChessMain(str);
		}
		new ChessMain(list.get(2));*/
		
		try {
			String FEN = "1K6/8/3k4/8/4B3/8/8/1q4NR";
			
			FEN = "rn2k1nr/pp1q1pbp/8/1b4p1/8/8/PPP1QPPP/RNB1KBNR"; // Check
			
			
			//FEN = "4k1Q1/7p/np1rp3/1b6/1P6/8/6PP/RNB1KBNR";
			
			//FEN = "rn2k2r/pp1q2bp/8/1b1Q4/8/8/PPP1Q1PP/RNB1KBNR";
			
			//Chess chess = new Chess();
			frame = new ChessMain(FEN);
			Chess chess = frame.getChessBoard();
			chess.switchTurn(TurnColor.WHITE);
			TurnColor color = TurnColor.BLACK;
			
			
			/*while(chess.isGameActivate()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				frame.revalidate();frame.repaint();
				ChessMove nextMove = ChessBot.getNextBestMove(chess, chess.getCurrentTurn());
				frame.switchTurn(nextMove);
				
				//nextMove.initiateChange();
				//chess.switchTurn(chess.getCurrentTurn());
			}*/
			
			boolean b = getBestPossibleMove(frame.getChessBoard(), color);
			
			//System.out.println(chess.getLastMove().getPGN());
			
			System.out.println(chess.getPGN());
			
			
			int c = 0;
			for(int rank = 0;rank < RANK; rank++) {
				for(int file = 0;file < FILE; file++) {
					if(frame.getChessBoard().getChessPiece(rank, file) != null) {
						//System.out.println(rank + "  " + file);
						//System.out.println(frame.getChessBoard().getChessPiece(rank, file).getColor());
						//System.out.println(frame.getChessBoard().getChessPiece(rank, file).getType());
						c++;
					}
				}
			}
			
			System.out.println(chess.getFEN());
			
			System.out.println(c);
			
		} catch (ChessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println(getBestPossibleMove(chess, Chess.BLACK, Chess.BLACK));
		
		
		
		/* this is the bot */
		
		/*while(chess.isGameActivate()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			frame.revalidate();frame.repaint();
			ChessMove nextMove = ChessBot.getNextBestMove(chess, chess.getCurrentTurn());
			nextMove.initiateChange();
			chess.switchTurn(chess.getCurrentTurn());
		}*/
		
		
		
		
		
		int count = 1;
		/*while(chess.isGameActivate()) {
			
			//count++;
			
			/*ChessBot bot = new ChessBot(chess, color);
			System.out.println(bot.getBestPossibleMove());
			bot.getNextMove().initiateChange();*/
			//boolean b = getBestPossibleMove(chess, color);
			/*
			
			
			color = -color + 1;
		}*/
		
		/*System.out.println(color);
		System.out.println(chess.getPGN());
		System.out.println(chess.getFEN());*/
		
		
		//This is the bot
		
		
		
		
		/*while(true) {
			System.out.println("Enter history index Number: ");
			int index = input.nextInt();
			chess.loadFEN(index);
			frame.revalidate();frame.repaint();
		}*/
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		//System.out.println(chess);
		
		/*ChessBot bot = new ChessBot(chess, Chess.WHITE);
		System.out.println(bot.getBestPossibleMove());
		bot.getNextMove().initiateChange();*/
		//new ChessMain("rnbqkbnr/ppppp2p/5p2/6pQ/3PP3/8/PPP2PPP/RNB1KBNR");
	}
	
	public static ChessMain frame;
	
	public static boolean getBestPossibleMove(Chess chess, TurnColor color) {
		return getBestPossibleMove(chess, color, color);
	}
	
	public static boolean getBestPossibleMove(Chess chess, TurnColor color, TurnColor firstColor) {
		try {
			if(!chess.isGameActivate())
				return false;
			//frame.revalidate();frame.repaint();
			//Thread.sleep(100);
			
			
			/*ChessMove mov = chess.getLastMove();
			if(mov != null &&  mov.getPGN().equals("g4")) {
				
				Thread.sleep(100000);
			}*/
			
			
			
			ChessPosition position;ChessPiece piece;
			boolean inside = false;
			for(int rank = 0;rank < Chess.RANK && chess.isGameActivate(); rank++) {
				for(int file = 0;file < Chess.FILE && chess.isGameActivate(); file++) {
					position = chess.getChessPosition(rank, file);
					if(position != null) {
						piece = position.getChessPiece();
						if(piece != null && piece.getColor() == color) {
							//Chess newChess = new Chess(chess);
							//ChessMove newMove = new ChessMove(move, newChess);
							
							//ChessMoves moves = new LegalChessMoves(newChess.getChessPosition(rank, file), newChess, true);
							
							
							ChessMoves moves = new LegalChessMoves(chess, position);
							for(ChessMove move : moves.getPossibleMoves()) {
								if(!chess.isGameActivate()) {
									//System.out.println("Over ");
									if(chess.getWinner() == firstColor) {
										return true;
										//frame.addVictoryPanel();
									}
									else
										return false;
								}
								if(chess.isChecked(color) || chess.containsMove(move) != true) {
								//if(!piece.containsMove(move)) {
								
								
									//if(move.getUpdates().equals())
									//move.initiateChange();move.getNextMoveChess()
									//System.out.println(move.getUpdates());
									if(move.isLegal()) {
										frame.switchTurn(move);
										inside = true;
										//move.initiateChange();
										//chess.switchTurn(color);
										boolean nextMove = getBestPossibleMove(chess, ChessData.getOppositeColor(color), firstColor);
										break;
									}
									
									
									//win = win || nextMove;
									
									
									
									
								//}
								}
									
									
									
									 
								//ChessBot bot = new ChessBot(move.getNextMoveChess(), this.getNextColor());
								/*if(bot.chess.getWinner() == color)
									list.add(move);*/
							}
							//moves = null;
						}
					}
					/*if(kingPosition != null) {
						ChessPiece kingPiece = kingPosition.getChessPiece();
						if(kingPiece != null && kingPiece.getType() == KING && kingPiece.getColor() == color) {
							b = true;
							return kingPosition;
						}
					}*/
				}
				if(inside)
					break;
			}
			/*if(!inside) {
				System.out.println("Prob");
				System.out.println(color);
				Thread.sleep(10000);
			}*/
		}
		catch(Exception exp) {
			exp.printStackTrace();
			System.out.println(chess.getPGN());
		}//System.out.println("dub");}
		return false;
	}

}
