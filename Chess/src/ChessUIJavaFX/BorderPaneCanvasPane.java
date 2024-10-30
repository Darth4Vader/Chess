package ChessUIJavaFX;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public class BorderPaneCanvasPane extends CanvasPane {

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
