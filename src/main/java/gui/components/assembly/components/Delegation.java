package gui.components.assembly.components;

import gui.components.Component;
import gui.components.assembly.Assemblable;
import gui.components.util.ComponentTraverser;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Delegation extends Rectangle implements Assemblable {
    private static final double DELEGATES_BOX_SIZE = 20;

    private final Component component;
    private Component requiresFromComponent;
    private Component providesToComponent;
    private Point2D startPoint;
    private Point2D endPoint;
    private Assemblable start;
    private Assemblable end;
    private Side side;
    public boolean providesOutwards = false;
    public Delegation(Component component, Point2D position, Side side) {
        this.component = component;
        this.side = side;
        setWidth(DELEGATES_BOX_SIZE);
        setHeight(DELEGATES_BOX_SIZE);
        setLayoutX(position.getX() - DELEGATES_BOX_SIZE / 2);
        setLayoutY(position.getY() - DELEGATES_BOX_SIZE / 2);
        calculateStartAndEndPoint();
        this.setFill(Paint.valueOf("#FFFFFF"));
        this.setStroke(Paint.valueOf("#000000"));
        this.setStrokeWidth(1);
    }
    public Delegation(Component component, Side side) {
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
        providesOutwards = true;
        calculateStartAndEndPoint();
        this.setFill(Paint.valueOf("#FFFFFF"));
        this.setStroke(Paint.valueOf("#000000"));
        this.setStrokeWidth(1);
    }

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

    private void calculateStartAndEndPoint() {
        switch (side) {
            case TOP:
                startPoint = new Point2D(this.getLayoutX() + DELEGATES_BOX_SIZE / 2, this.getLayoutY());
                endPoint = new Point2D(this.getLayoutX() + DELEGATES_BOX_SIZE / 2, this.getLayoutY() + DELEGATES_BOX_SIZE);
                break;
            case BOTTOM:
                startPoint = new Point2D(this.getLayoutX() + DELEGATES_BOX_SIZE / 2, this.getLayoutY() + DELEGATES_BOX_SIZE);
                endPoint = new Point2D(this.getLayoutX() + DELEGATES_BOX_SIZE / 2, this.getLayoutY());
                break;
            case LEFT:
                startPoint = new Point2D(this.getLayoutX(), this.getLayoutY() + DELEGATES_BOX_SIZE / 2);
                endPoint = new Point2D(this.getLayoutX() + DELEGATES_BOX_SIZE, this.getLayoutY() + DELEGATES_BOX_SIZE / 2);
                break;
            case RIGHT:
                startPoint = new Point2D(this.getLayoutX() + DELEGATES_BOX_SIZE, this.getLayoutY() + DELEGATES_BOX_SIZE / 2);
                endPoint = new Point2D(this.getLayoutX(), this.getLayoutY() + DELEGATES_BOX_SIZE / 2);
                break;
            default:
                startPoint = new Point2D(this.getLayoutX(), this.getLayoutY());
                endPoint = new Point2D(this.getLayoutX(), this.getLayoutY());
        }
        if (providesOutwards) {
            Point2D temp = startPoint;
            startPoint = endPoint;
            endPoint = temp;
        }
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
        calculateStartAndEndPoint();
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
