package ChessUIJavaFX;

import ChessDataTypes.ChessData.Piece;
import ChessDataTypes.ChessData.TurnColor;
import ChessDataTypes.ChessPiece;
import ChessDataTypes.ChessPosition;
import ChessDataTypes.Knight;
import ChessDataTypes.Pawn;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class TestJavaFX extends Application {
	
	//--module-path "C:\JavaFX_22.02\lib" --add-modules javafx.controls,javafx.fxml

	public static void main(String[] args) {
        launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		ChessPosition position = new ChessPosition(TurnColor.BLACK,0,0);
		Pawn piece = new Pawn(TurnColor.BLACK);
		Knight piece2 = new Knight(TurnColor.WHITE);
		
		ChessPiecePanel pawn = new ChessPiecePanel(piece, null);
		ChessPiecePanel knight = new ChessPiecePanel(piece2, null);
		
		pawn.setPrefWidth(40);
		pawn.setPrefHeight(40);
		
		knight.setPrefWidth(40);
		knight.setPrefHeight(40);
		
		Pane pane = new Pane();
		
		pane.getChildren().addAll(pawn, knight);
		
		
		Scene scene = new Scene(pane);
		primaryStage.setScene(scene);
		primaryStage.setWidth(400);
		primaryStage.setHeight(500);
		primaryStage.show();
	}

}
