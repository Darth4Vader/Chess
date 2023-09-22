import java.util.ArrayList;
import java.util.List;

import ChessDataTypes.Chess;
import ChessDataTypes.ChessData;
import ChessDataTypes.ChessMove;
import ChessDataTypes.ChessMoves;
import ChessDataTypes.ChessPiece;
import ChessDataTypes.ChessPosition;
import ChessDataTypes.LegalChessMoves;

public class ChessBot implements ChessData {
	
	public static final int MAX_TURNS = 4;
	
	private Chess chess;
	private TurnColor color;
	private TurnColor firstColor;
	private ChessMove firstNextMove;
	
	private int movesNumber;
	
	private int isChecked;
	
	private int isChecking;
	
	private int captureGain;
	private int captureLost;
	
	private int nextMovePropability;
	
	
	public ChessBot(ChessBot bot, TurnColor color) {
		this.chess = new Chess(bot.chess);
		this.firstColor = color;
		this.movesNumber = bot.movesNumber;
		this.nextMovePropability = bot.nextMovePropability;
	}
	
	public ChessBot(Chess chess, TurnColor color) {
		this.chess = chess;
		this.firstColor = color;
		this.movesNumber = 0;
		this.nextMovePropability = 0;
	}
	
	public ChessBot(Chess chess, TurnColor color, ChessMove firstNextMove) {
		this(chess, color);
		this.firstNextMove = firstNextMove;
		this.movesNumber = 0;
		this.nextMovePropability = 0;
	}
	
	public static ChessMove getNextBestMove(Chess chess, TurnColor color) {
		ChessBot bot = new ChessBot(new Chess(chess), chess.getCurrentTurn());
		bot = bot.getBestPossibleMove(color, color);
		System.out.println(bot.captureGain);
		ChessMove move = new ChessMove(bot.firstNextMove, chess);
		return move;
	}
	
	public ChessBot getBestPossibleMove(TurnColor color, TurnColor firstColor) {
		ChessBot bot = this;
		if(chess.isGameActivate() && movesNumber < MAX_TURNS) try {
			movesNumber++;
			ChessPosition position;ChessPiece piece;
			for(int rank = 0;rank < Chess.RANK && chess.isGameActivate(); rank++) {
				for(int file = 0;file < Chess.FILE && chess.isGameActivate(); file++) {
					position = chess.getChessPosition(rank, file);
					if(position != null) {
						piece = position.getChessPiece();
						if(piece != null && piece.getColor() == color) {
							ChessMoves moves = new LegalChessMoves(chess, position);
							for(ChessMove move : moves.getPossibleMoves()) {
								if(!chess.isGameActivate()) {
									if(chess.getWinner() == firstColor)
										return bot;
								}
								if(chess.containsMove(move) != true) {
									if(move.isLegal()) {
										ChessBot newBot = new ChessBot(this, firstColor);
										ChessMove newMove = new ChessMove(move, newBot.chess);
										newMove.initiateChange();
										newBot.chess.switchTurn(color);
										
										newBot.addToBot(newMove);
										
										newBot.color = color;
										
										
										newBot.firstNextMove = move;
										
										
										newBot.nextMovePropability = moves.getPossibleMoves().size();
										
										ChessBot bestBot = newBot.getBestPossibleMove(ChessData.getOppositeColor(color), firstColor);
										
										newBot.updateBot(bestBot);
										
										bot = bot.getBestPossibbleBot(newBot);
									}
								}
							}
						}
					}
				}
			}
		}
		catch(Exception exp) {
			exp.printStackTrace();
		}
		return bot;
	}
	
	private void addToBot(ChessMove move) {
		Move type = move.getMoveName();
		if(type == Move.CAPTURE) {
			if(move.getColor() == this.firstColor)
				this.captureGain++;
			else
				this.captureLost++;
		}
		if(type == Move.CHECK) {
			if(move.getColor() == this.firstColor)
				this.isChecking++;
			else
				this.isChecked++;
		}
	}
	
	
	private void updateBot(ChessBot nextBot) {
		//this.movesNumber += nextBot.movesNumber;
		
		this.nextMovePropability += nextBot.nextMovePropability;
		
		this.captureGain += nextBot.captureGain;
		this.captureLost += nextBot.captureLost;
		this.isChecking += nextBot.isChecking;
		this.isChecked += nextBot.isChecked;
	}
	
	private double formula() {
		
		//return Math.max(this.captureGain, this.captureLost)
		
		//double d = (this.captureGain + this.isChecking) - (this.captureLost + this.isChecked);
		double d;
		if(this.color == this.firstColor)
			d = (this.captureGain + this.isChecking);
		else
			d = this.captureLost + this.isChecked;
		
		//d = (1/(this.nextMovePropability+1)) /* d */;
		
		return d;
	}
	
	
	public ChessBot getBestPossibbleBot(ChessBot bot) {
		/* Best Capture Game (Not Best Win) */
		
		
		if(this.firstNextMove == null)
			return bot;
		
		//int sum1 = (bot1.captureGain + bot1.isChecking) - (bot1.captureLost + bot1.isChecked);
		//int sum2 = (bot2.captureGain + bot2.isChecking) - (bot2.captureLost + bot2.isChecked);
		//if(bot1.captureGain > bot2.captureGain)
		
		ChessBot bot1 = this;
		ChessBot bot2 = bot;
		
		System.out.println(bot1.formula() + " > " + bot2.formula());
		if(this.color != this.firstColor) {
			if(bot1.formula() < bot2.formula())
				return bot1;
		}
		else
			if(bot1.formula() > bot2.formula())
				return bot1;
		return bot2;
	}
	
	public ChessMove getNextMove() {
		return this.firstNextMove;
	}
	
	/*private boolean won() {
		
	}*/
}
