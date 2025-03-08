package gui.controllers.interaction;

import gui.components.Component;
import gui.views.ComponentCanvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public interface InteractionStrategy {
    boolean isEnabled();
    void disable();
    void enable();

    default void componentClicked(Component component, MouseEvent mouseEvent) {
    }

    default void componentDragged(Component component, MouseEvent mouseEvent) {
    }

    default void componentMouseDown(Component component, MouseEvent mouseEvent) {
    }

    default void componentMouseUp(Component component, MouseEvent mouseEvent) {
    }

    default void componentMouseOver(Component component, MouseEvent mouseEvent) {
    }

    default void canvasClicked(ComponentCanvas canvas, MouseEvent mouseEvent) {
    }

    default void canvasDragged(ComponentCanvas canvas, MouseEvent mouseEvent) {
    }

    default void canvasMouseDown(ComponentCanvas canvas, MouseEvent mouseEvent) {
    }

    default void canvasMouseUp(ComponentCanvas canvas, MouseEvent mouseEvent) {
    }

    default void canvasMouseOver(ComponentCanvas canvas, MouseEvent mouseEvent) {
    }

    default void keyPressed(KeyEvent keyEvent) {}
}
