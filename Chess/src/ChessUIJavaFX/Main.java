package ChessUIJavaFX;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        var holderPane = new Pane();
        holderPane.setBackground(Background.fill(Color.BLUE));
        holderPane.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        holderPane.setPrefSize(300, 175);

        var listenerPane = createListenerPane(holderPane);
        primaryStage.setScene(new Scene(listenerPane, 600, 400));
        primaryStage.show();
    }

    private Pane createListenerPane(Pane holderPane) {
        var listenerPane = new Pane(holderPane);

        var handler = new ListenerPaneMouseHandler(holderPane);
        listenerPane.setOnMousePressed(handler);
        listenerPane.setOnMouseDragged(handler);
        listenerPane.setOnMouseClicked(handler);

        listenerPane.setOnScroll(e -> {
            if (e.getDeltaY() != 0) {
                double factor = 1.5;

                double scale = holderPane.getScaleX();
                if (e.getDeltaY() < 0) {
                    scale /= factor;
                } else {
                    scale *= factor;
                }
                scale = Math.max(0.5, Math.min(4, scale)); // clamp scale to [0.5, 4]
                holderPane.setScaleX(scale);
                holderPane.setScaleY(scale);
            }
        });

        return listenerPane;
    }

    // Aggregate onPressed, onDragged, and onClicked handler
    private static class ListenerPaneMouseHandler implements EventHandler<MouseEvent> {

        private final Pane holderPane;

        private double dragOriginX;
        private double dragOriginY;
        private boolean wasDragged;

        ListenerPaneMouseHandler(Pane holderPane) {
            this.holderPane = holderPane;
        }

        @Override
        public void handle(MouseEvent event) {
            event.consume();
            if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                handlePressed(event);
            } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                handleDragged(event);
            } else if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                handleClicked(event);
            } else {
                throw new IllegalArgumentException("unsupported type: " + event.getEventType());
            }
        }

        private void handlePressed(MouseEvent event) {
            wasDragged = false;
            /*
             * Dragging 'holderPane' around does not require converting the
             * coordinates. Moving a non-transformed pane by 5 pixels and moving
             * a transformed pane by 5 pixels is the same thing.
             */
            dragOriginX = event.getX();
            dragOriginY = event.getY();
        }

        private void handleDragged(MouseEvent event) {
            wasDragged = true;

            double deltaX = event.getX() - dragOriginX;
            double deltaY = event.getY() - dragOriginY;
            dragOriginX += deltaX;
            dragOriginY += deltaY;

            holderPane.setTranslateX(holderPane.getTranslateX() + deltaX);
            holderPane.setTranslateY(holderPane.getTranslateY() + deltaY);
        }

        private void handleClicked(MouseEvent event) {
            if (!wasDragged && event.getClickCount() == 1) {
                switch (event.getButton()) {
                    case PRIMARY -> {
                        /*
                         * Convert coordinates to 'holderPane' space (takes into account all
                         * transformations). This only works because 'holderPane' is a direct
                         * child of the event source.
                         */
                        var location = holderPane.parentToLocal(event.getX(), event.getY());

                        var circle = new Circle(10);
                        circle.setCenterX(location.getX());
                        circle.setCenterY(location.getY());
                        holderPane.getChildren().add(circle);
                    }
                    case SECONDARY -> {
                        if (event.isAltDown()) {
                            holderPane.setTranslateX(0);
                            holderPane.setTranslateY(0);
                            holderPane.setScaleX(1);
                            holderPane.setScaleY(1);
                        }
                    }
                }
            }
        }
    }
}