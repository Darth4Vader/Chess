import ChessUIJavaFX.ChessMain;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class JavaFXMain2 extends Application {

	//--module-path "C:\JavaFX_22.02\lib" --add-modules javafx.controls,javafx.fxml

	public static void main(String[] args) {
        launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		CanvasBorderPane main = new CanvasBorderPane();
		main.setBackground(Background.fill(Color.ALICEBLUE));
		Label pane = new Label("Gooo");
		main.getChildren().add(pane);
		StackPane.setAlignment(pane, Pos.CENTER);
		Scene scene = new Scene(main);
		primaryStage.setScene(scene);
		primaryStage.setWidth(400);
		primaryStage.setHeight(500);
		primaryStage.show();
	}
	
	private class CanvasBorderPane extends StackPane {
		private final Canvas canvas;
		private final GraphicsContext gc;
	
		public CanvasBorderPane() {
	            this.canvas = new Canvas();
	            this.gc = this.canvas.getGraphicsContext2D();
	            canvas.widthProperty().bind(this.widthProperty()); // Change this so this canvas does not scale with the pane, and its size is constant.
	            canvas.heightProperty().bind(this.heightProperty());
	            this.getChildren().add(canvas);
	        }
	
		public GraphicsContext getGraphicsContext() {
			return this.gc;
		}
	}
}
