package gui.components.util;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;

public class Grid {
    private final int GRID_SIZE;

    public Grid(int GRID_SIZE) {
        this.GRID_SIZE = GRID_SIZE;
    }

    public Point2D getNearestGridPosition(Point2D point) {
        double snappedX = getNearestGridPosition(point.getX());
        double snappedY = getNearestGridPosition(point.getY());
        return new Point2D(snappedX, snappedY);
    }

    public double getNearestGridPosition(double value) {
        return ((int) Math.round(value / GRID_SIZE)) * GRID_SIZE;
    }

    public Point2D getNearestGridPosition(MouseEvent event) {
        return getNearestGridPosition(new Point2D(event.getX(), event.getY()));
    }
}
