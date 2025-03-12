package gui.components.assembly.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.function.Consumer;

public class DelegationOverlay extends Parent {
    private Delegation delegation;
    private Consumer<MouseEvent> onDelegationClicked;
    @FXML
    private BorderPane container;
    @FXML
    private FontIcon icon;

    public DelegationOverlay() {

    }

    public static DelegationOverlay fromDelegation(Delegation delegation) {
        FXMLLoader loader = new FXMLLoader(DelegationOverlay.class.getResource("/gui/DelegationOverlay.fxml"));
        try {
            Node contents = loader.load();
            DelegationOverlay controller = loader.getController();
            controller.setDelegation(delegation);
            controller.getChildren().add(contents);
            return controller;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setOnDelegationClicked(Consumer<MouseEvent> onDelegationClicked) {
        this.onDelegationClicked = onDelegationClicked;
    }

    public void reposition() {
        Point2D origin = new Point2D(delegation.getLayoutX(), delegation.getLayoutY());
        this.setLayoutX(origin.getX() - ((40 - delegation.getWidth()) / 2));
        this.setLayoutY(origin.getY() - ((40 - delegation.getHeight()) / 2));
    }

    public Delegation getDelegation() {
        return delegation;
    }

    public void setDelegation(Delegation delegation) {
        this.delegation = delegation;
        this.setPickOnBounds(false);
        icon.setOnMouseClicked(event -> {
            if (onDelegationClicked != null) {
                onDelegationClicked.accept(event);
            }
        });
        resize();
        reposition();
        rotate();
    }

    public void resize() {
        reposition();
        container.setPickOnBounds(false);
    }

    private void rotate() {
        if (delegation.getSide() == null) {
            return;
        }
        switch (getArrowDirection()) {
            case LEFT:
                    icon.setRotate(270);
                break;
            case RIGHT:
                icon.setRotate(90);
                break;
            case TOP:
                icon.setRotate(0);
                break;
            case BOTTOM:
                icon.setRotate(180);
                break;
        }
    }

    private Side getArrowDirection() {
        Side side = delegation.getSide();
        boolean providingComponentMissing = delegation.getRequiresFromComponent() == null;
        if (providingComponentMissing) {
            if (delegation.providesOutwards) {
                return reverseSide(side);
            }
            return side;
        }
        if (delegation.providesOutwards) {
            return side;
        }
        return reverseSide(side);
    }

    private Side reverseSide(Side side) {
        return switch (side) {
            case LEFT -> Side.RIGHT;
            case RIGHT -> Side.LEFT;
            case TOP -> Side.BOTTOM;
            case BOTTOM -> Side.TOP;
            default -> Side.TOP;
        };
    }
}
