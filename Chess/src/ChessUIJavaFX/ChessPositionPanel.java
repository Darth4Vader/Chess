package ChessUIJavaFX;

import java.util.List;

import ChessDataTypes.ChessData.TurnColor;
import ChessDataTypes.ChessMove;
import ChessDataTypes.ChessMoves;
import ChessDataTypes.ChessPiece;
import ChessDataTypes.ChessPosition;
import javafx.collections.ListChangeListener;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;

public class ChessPositionPanel extends BorderPaneCanvasPane {
	
	private ChessMain board;
	private final ChessPosition position;
	private ChessPiecePanel piece;
	private boolean isPossible;
	
	public ChessPositionPanel(ChessPosition position, ChessMain board) {
		this.position = position;
		this.isPossible = false;
		this.board = board;
		this.setBackground(Background.fill(position.getBackgroundColor() == TurnColor.WHITE 
												? board.getWhiteColor()
												: board.getBlackColor()));
		this.setBorder(Border.stroke(Color.GRAY));
		this.setOnMousePressed(e -> {
            if(piece != null) {
                if(e.getButton() == MouseButton.SECONDARY)
                    piece.requestFocus();
            }
            else
                this.requestFocus();
        });
		this.getBorderPane().getChildren().addListener((ListChangeListener<javafx.scene.Node>) c -> {
            while(c.next()) {
                if(c.wasAdded()) {
            		if(!board.getChessBoard().isGameActivate())
            			board.addVictoryPanel();
                }
            }
		});
		this.setPickOnBounds(false);
		initiateChessPiecePanel();
		
		this.setOnMouseDragEntered(e -> {
			Object obj = e.getGestureSource();
			if(obj instanceof ChessPiecePanel) {
				ChessPiecePanel piece = (ChessPiecePanel)obj;
				ChessPositionPanel source = piece.getChessPositionPanel();
				if(!this.equals(source)) {
					prevBorder = this.getBorder();
					this.setBorder(Border.stroke(Color.DARKGRAY));
				}
			}
		});
		this.setOnMouseDragExited(e -> {
			if(prevBorder != null)
				this.setBorder(prevBorder);
		});
		this.setOnMouseDragReleased(e -> {
			Object obj = e.getGestureSource();
			if(obj instanceof ChessPiecePanel) {
				ChessPiecePanel piece = (ChessPiecePanel)obj;
				ChessMoves possibleMoves = piece.getPossibleMoves();
				if(possibleMoves != null) {
					List<ChessMove> list = possibleMoves.getPossibleMoves();
					if(list.size() != 0) {
						for(ChessMove move : list) {
							if(this.position.equals(move.getMoveToPosition())) {
								this.board.switchTurn(move);
								piece.checkUpdates();
								break;
							}
						}
						this.requestFocus();
					}
				}
			}
		});
	}
	
	private Border prevBorder;
	
	public void setIfPossible(boolean isPossible) {
		this.isPossible = isPossible;
		paintComponent();
	}
	
	@Override 
	protected void paintComponent() {
		super.paintComponent();
		GraphicsContext gc = getCanvas().getGraphicsContext2D();
		gc.clearRect(0, 0, getWidth(), getHeight());
		if(isPossible) {
			gc.setStroke(Color.rgb(128, 128, 128, 150/255.0));
			gc.setFill(Color.rgb(128, 128, 128, 150/255.0));
			if(piece == null) {
				int width = (int)(getWidth()*0.3);
				int height = (int)(getHeight()*0.3);
				int x = (int)((getWidth() - width)*0.5);
				int y = (int)((getHeight() - height)*0.5);
				gc.strokeOval(x, y, width, height);
				gc.fillOval(x, y, width, height);
			}
			else {
				gc.strokeOval(0, 0, getWidth(), getHeight());
			}
		}
	}
	
	private void initiateChessPiecePanel() {
		ChessPiece piece = position.getChessPiece();
		if(piece != null) {
			this.piece = new ChessPiecePanel(piece, board);
			this.piece.setChessPositionPanel(this);
			this.setCenter(this.piece);
		}
		else
			this.piece = null;
	}
	
	public void setChessPiecePanel(ChessPiecePanel piece) {
		this.piece = piece;
		this.setCenter(this.piece);
	}
	
	public ChessPosition getPosition() {
		return position;
	}
	
	public boolean isEmpty() {
		return this.piece == null;
	}
	
	public boolean equals(ChessPositionPanel chessSquare) {
		return getPosition().equals(chessSquare.getPosition());
	}
}