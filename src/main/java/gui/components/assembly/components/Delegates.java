package gui.components.assembly.components;

import gui.components.Component;
import gui.components.assembly.Assemblable;
import gui.components.util.ComponentTraverser;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Delegates extends Rectangle implements Assemblable {
    private static final double DELEGATES_BOX_SIZE = 20;

    private final Component component;

    public Component getProvidesToComponent() {
        return providesToComponent;
    }

    public void setProvidesToComponent(Component providesToComponent) {
        this.providesToComponent = providesToComponent;
    }

    public Component getRequiresFromComponent() {
        return requiresFromComponent;
    }

    public void setRequiresFromComponent(Component requiresFromComponent) {
        this.requiresFromComponent = requiresFromComponent;
    }

    private Component requiresFromComponent;
    private Component providesToComponent;
    private Point2D startPoint;
    private Point2D endPoint;
    private Assemblable start;
    private Assemblable end;
    private Side side;

    public Delegates(Component component, Point2D position, Side side) {
        this.component = component;
        this.side = side;
        setWidth(DELEGATES_BOX_SIZE);
        setHeight(DELEGATES_BOX_SIZE);
        setLayoutX(position.getX() - DELEGATES_BOX_SIZE / 2);
        setLayoutY(position.getY() - DELEGATES_BOX_SIZE / 2);
        startPoint = position.subtract(DELEGATES_BOX_SIZE / 2, 0);
        endPoint = position.add(DELEGATES_BOX_SIZE / 2, 0);
        this.setFill(Paint.valueOf("#FFFFFF"));
        this.setStroke(Paint.valueOf("#000000"));
        this.setStrokeWidth(1);
    }

    public Delegates(Component component, Side side) {
        this.component = component;
        this.side = side;
        setWidth(DELEGATES_BOX_SIZE);
        setHeight(DELEGATES_BOX_SIZE);
        Point2D position;
        Point2D componentOrigin = ComponentTraverser.getSwitchedCoordinates(component, null);
        double width = component.getContainer().getWidth();
        double height = component.getContainer().getHeight();
        position = switch (side) {
            case TOP -> componentOrigin.add(width / 2, 0);
            case BOTTOM -> componentOrigin.add(width / 2, height);
            case LEFT -> componentOrigin.add(0, height / 2);
            case RIGHT -> componentOrigin.add(width, height / 2);
        };
        setLayoutX(position.getX() - DELEGATES_BOX_SIZE / 2);
        setLayoutY(position.getY() - DELEGATES_BOX_SIZE / 2);
        startPoint = position.subtract(DELEGATES_BOX_SIZE / 2, 0);
        endPoint = position.add(DELEGATES_BOX_SIZE / 2, 0);
        this.setFill(Paint.valueOf("#FFFFFF"));
        this.setStroke(Paint.valueOf("#000000"));
        this.setStrokeWidth(1);
    }

    @Override
    public void setStart(Assemblable assembly, Point2D point) {
        this.start = assembly;
    }

    @Override
    public void setEnd(Assemblable assembly, Point2D point) {
        this.end = assembly;
    }

    @Override
    public Point2D getStartPoint() {
        return startPoint;
    }

    @Override
    public Point2D getEndPoint() {
        return endPoint;

    }

    @Override
    public Assemblable getStart() {
        return start;
    }

    @Override
    public Assemblable getEnd() {
        return end;
    }

    @Override
    public void moveSimple(Point2D point) {
        setLayoutX(getLayoutX() + point.getX());
        setLayoutY(getLayoutY() + point.getY());
        startPoint = new Point2D(getLayoutX(), getLayoutY());
        endPoint = new Point2D(getLayoutX() + DELEGATES_BOX_SIZE, getLayoutY());
        if (start != null) {
            start.setEnd(this, startPoint);
        }
        if (end != null) {
            end.setStart(this, endPoint);
        }
    }

    public Side getSide() {
        return side;
    }

    @Override
    public void moveRecursive(Assemblable assembly, Point2D point) {

    }

    public Component getComponent() {
        return component;
    }
}
