import ChessUIJavaFX.ChessMain;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavaFXMain extends Application {
	
	//--module-path "C:\JavaFX_22.02\lib" --add-modules javafx.controls,javafx.fxml

	public static void main(String[] args) {
        launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		ChessMain main = new ChessMain();
		Scene scene = new Scene(main);
		primaryStage.setScene(scene);
		primaryStage.setWidth(400);
		primaryStage.setHeight(500);
		primaryStage.show();
	}

}
