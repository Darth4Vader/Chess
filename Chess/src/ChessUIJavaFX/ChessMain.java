package ChessUIJavaFX;
import java.util.Arrays;
import java.util.List;

import ChessDataTypes.Chess;
import ChessDataTypes.ChessData;
import ChessDataTypes.ChessException;
import ChessDataTypes.ChessMove;
import ChessDataTypes.ChessMove.PromotionChooseException;
import ChessDataTypes.ChessPiece;
import ChessDataTypes.ChessPosition;
import JavaFXUtilities.JavaFXUtils;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ChessMain extends GridPane implements ChessData {
	
	private final Chess chess;
	private final ChessPositionPanel[][] arr;
	private final Color whiteColor, blackColor;
	
	public ChessMain() throws ChessException {
		this(new Chess());
	}
	
	public ChessMain(String FEN) throws ChessException {
		this(new Chess(FEN));
	}
	
	public ChessMain(Chess chess) {
		this.chess = chess;
		this.arr = new ChessPositionPanel[RANK][FILE];
		this.whiteColor = Color.rgb(255, 229, 204);
		this.blackColor = Color.rgb(255, 178, 102);
		this.loadChessBoard();
		this.setVisible(true);
	}
	
	public Color getWhiteColor() {
		return this.whiteColor;
	}
	
	public Color getBlackColor() {
		return this.blackColor;
	}
	
	public Chess getChessBoard() {
		return this.chess;
	}
	
	public ChessPositionPanel[][] getArr() {
		return this.arr;
	}
	
	public void loadChessBoard() {
		RowConstraints rc = new RowConstraints();
		rc.setPercentHeight(100d / RANK);

		for (int i = 0; i < RANK; i++) {
		    this.getRowConstraints().add(rc);
		}

		ColumnConstraints cc = new ColumnConstraints();
		cc.setPercentWidth(100d / FILE);

		for (int i = 0; i < RANK; i++) {
		    this.getColumnConstraints().add(cc);
		}
		for(int rank = 0;rank < RANK; rank++) {
			for(int file = 0;file < FILE; file++) {
				this.arr[rank][file] = new ChessPositionPanel(chess.getChessPosition(rank, file), this);
				this.add(this.arr[rank][file], file, rank);
			}
		}
	}
	
	public void switchTurn(ChessMove move) {
		try {
			move.initiateChange();
			switchTurn();
		} catch (PromotionChooseException promotionException) {
			List<Piece> list = Arrays.asList(Piece.QUEEN, Piece.BISHOP, Piece.KNIGHT, Piece.ROOK);
            final Stage dialog = new Stage();
			HBox panel = new HBox();
			ChessPositionPanel pane = getChessPositionPanel(0, 0);
			panel.prefWidthProperty().bind(pane.widthProperty().multiply(list.size()));
			panel.prefHeightProperty().bind(pane.heightProperty());
			list.stream()
				.map((type) -> ChessData.getChessPieceClass(type))
				.map((piece) -> ChessPiece.newInstance(piece, move.getChess().getCurrentTurn()))
				.map((piece) -> new ChessPieceImage(piece))
				.forEach((chessPieceImage) -> {
					chessPieceImage.setCursor(Cursor.HAND);
					chessPieceImage.setOnMouseClicked(e -> {
						promotionException.setPromotion(chessPieceImage.getType());
						switchTurn();
						dialog.close();
					});
					HBox.setHgrow(chessPieceImage, Priority.ALWAYS);
					panel.getChildren().add(chessPieceImage);
			});
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(this.getScene().getWindow());
			dialog.setScene(new Scene(panel));
			dialog.setOnCloseRequest(e -> e.consume());
			dialog.show();
		}
	}
	
	private void switchTurn() {
		this.chess.switchTurn();
		if(!this.chess.isGameActivate())
			addVictoryPanel();
	}
	
	public void addVictoryPanel() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		String str = "";
		TurnColor winner = chess.getWinner();
		if(winner == TurnColor.DRAW) 
			str = "Draw";
		else {
			if(winner == TurnColor.WHITE)
				str = "White";
			else
				if(winner == TurnColor.BLACK)
					str = "Black";
			str += " Won";
		}
		alert.setContentText(str);
		alert.show();
	}
	
	public ChessPositionPanel getChessPositionPanel(ChessPosition position) {
		return getChessPositionPanel(position.getRank(), position.getFile());
	}
	
	public ChessPositionPanel getChessPositionPanel(int rank, int file) {
		if(0 <= rank && rank < RANK && 0 <= file && file < FILE)
			return arr[rank][file];
		return null;
	}
	
	public static Image loadImage(String path) {
		return JavaFXUtils.getImageResource(ChessMain.class, path, false);
	}
}
