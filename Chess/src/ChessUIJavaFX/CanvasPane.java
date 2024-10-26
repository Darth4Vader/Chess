package ChessUIJavaFX;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

public class CanvasPane extends Pane {
	
    public final Canvas canvas;

	public CanvasPane() {
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
		canvas.setMouseTransparent(false);
	}
	
	protected void paintComponent() {
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
	
}
