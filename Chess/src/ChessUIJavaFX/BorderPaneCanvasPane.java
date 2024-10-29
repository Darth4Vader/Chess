package ChessUIJavaFX;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class BorderPaneCanvasPane extends CanvasPane {
	
    /*public final Canvas canvas;

	public BorderPaneCanvasPane() {
        canvas = new Canvas();
        getChildren().add(canvas);
        canvas.widthProperty().bind(this.widthProperty()); // Change this so this canvas does not scale with the pane, and its size is constant.
        canvas.heightProperty().bind(this.heightProperty());
		this.widthProperty().addListener((obs, oldVal, newVal) -> {
			System.out.println("Shi");
			paintComponent();
		});
		this.heightProperty().addListener((obs, oldVal, newVal) -> {
			paintComponent();
		});
		//canvas.setMouseTransparent(true);
	}
	
	protected void paintComponent() {
	}
	
	public Canvas getCanvas() {
		return canvas;
	}*/

	private BorderPane borderPane;
	
	public BorderPaneCanvasPane() {
		borderPane = new BorderPane();
        getChildren().add(borderPane);
        borderPane.prefWidthProperty().bind(this.widthProperty());
        borderPane.prefHeightProperty().bind(this.heightProperty());
	}
	
	public BorderPane getBorderPane() {
		return borderPane;
	}
	
	public void setCenter(Node node) {
		borderPane.setCenter(node);
	}

}
