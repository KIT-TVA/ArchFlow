package gui.components;

import gui.components.util.ComponentTraverser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SelectedComponentOverlay extends Parent {
    private Component component;
    private Consumer<Side> arrowConsumer;
    private BiConsumer<MouseEvent, Side> edgePressedConsumer;
    private BiConsumer<MouseEvent, Side> edgeDraggedConsumer;
    private BiConsumer<MouseEvent, Side> edgeReleasedConsumer;
    @FXML
    private Rectangle topLine;
    @FXML
    private Rectangle bottomLine;
    @FXML
    private Rectangle leftLine;
    @FXML
    private Rectangle rightLine;

    @FXML
    private BorderPane topContainer;

    public SelectedComponentOverlay() {

    }

    public static SelectedComponentOverlay fromComponent(Component component) {
        FXMLLoader loader = new FXMLLoader(SelectedComponentOverlay.class.getResource("/gui/SelectedComponentOverlay.fxml"));
        try {
            Node contents = loader.load();
            SelectedComponentOverlay controller = loader.getController();
            controller.setComponent(component);
            controller.getChildren().add(contents);
            return controller;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void topLinePressed(MouseEvent event) {
        edgePressedConsumer.accept(event, Side.TOP);
    }

    @FXML
    private void bottomLinePressed(MouseEvent event) {
        edgePressedConsumer.accept(event, Side.BOTTOM);
    }

    @FXML
    private void leftLinePressed(MouseEvent event) {
        edgePressedConsumer.accept(event, Side.LEFT);
    }

    @FXML
    private void rightLinePressed(MouseEvent event) {
        edgePressedConsumer.accept(event, Side.RIGHT);
    }

    @FXML
    private void topLineDragged(MouseEvent event) {
        edgeDraggedConsumer.accept(event, Side.TOP);
    }

    @FXML
    private void bottomLineDragged(MouseEvent event) {
        edgeDraggedConsumer.accept(event, Side.BOTTOM);
    }

    @FXML
    private void leftLineDragged(MouseEvent event) {
        edgeDraggedConsumer.accept(event, Side.LEFT);
    }

    @FXML
    private void rightLineDragged(MouseEvent event) {
        edgeDraggedConsumer.accept(event, Side.RIGHT);
    }

    @FXML
    private void topLineReleased(MouseEvent event) {
        edgeReleasedConsumer.accept(event, Side.TOP);
    }

    @FXML
    private void bottomLineReleased(MouseEvent event) {
        edgeReleasedConsumer.accept(event, Side.BOTTOM);
    }

    @FXML
    private void leftLineReleased(MouseEvent event) {
        edgeReleasedConsumer.accept(event, Side.LEFT);
    }

    @FXML
    private void rightLineReleased(MouseEvent event) {
        edgeReleasedConsumer.accept(event, Side.RIGHT);
    }

    @FXML
    private void topArrowClicked(MouseEvent event) {
        if (arrowConsumer != null) {
            event.consume();
            arrowConsumer.accept(Side.TOP);
        }
    }

    @FXML
    private void bottomArrowClicked(MouseEvent event) {
        if (arrowConsumer != null) {
            event.consume();
            arrowConsumer.accept(Side.BOTTOM);
        }
    }

    @FXML
    private void leftArrowClicked(MouseEvent event) {
        if (arrowConsumer != null) {
            event.consume();
            arrowConsumer.accept(Side.LEFT);
        }
    }

    @FXML
    private void rightArrowClicked(MouseEvent event) {
        if (arrowConsumer != null) {
            event.consume();
            arrowConsumer.accept(Side.RIGHT);
        }
    }

    public void reposition() {
        Point2D origin = ComponentTraverser.getSwitchedCoordinates(component, null);
        this.setLayoutX(origin.getX());
        this.setLayoutY(origin.getY());
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
        this.setPickOnBounds(false);
        reposition();
        resize();
    }

    public void resize() {
        topLine.setWidth(component.getContainer().getWidth());
        bottomLine.setWidth(component.getContainer().getWidth());
        leftLine.setHeight(component.getContainer().getHeight() - 10);
        rightLine.setHeight(component.getContainer().getHeight() - 10);
        reposition();
        topContainer.setLayoutX(-50);
        topContainer.setLayoutY(-50);
        topContainer.setPrefSize(component.getContainer().getWidth() + 2 * 50, component.getContainer().getHeight() + 2 * 50);
        topContainer.setPickOnBounds(false);
    }

    public void setOnArrowClicked(Consumer<Side> consumer) {
        arrowConsumer = consumer;
    }

    public void setOnEdgePressed(BiConsumer<MouseEvent, Side> consumer) {
        edgePressedConsumer = consumer;
    }

    public void setOnEdgeDragged(BiConsumer<MouseEvent, Side> consumer) {
        edgeDraggedConsumer = consumer;
    }

    public void setOnEdgeReleased(BiConsumer<MouseEvent, Side> consumer) {
        edgeReleasedConsumer = consumer;
    }
}
