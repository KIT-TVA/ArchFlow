package gui.components.assembly.util;

import gui.components.assembly.Assemblable;
import gui.components.assembly.components.Assembly;
import gui.components.assembly.components.AssemblyLine;
import gui.components.assembly.components.ComponentConnection;
import gui.components.assembly.components.Delegation;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.LinkedList;
import java.util.List;

public class AssemblyEditor extends Group {
    private static final double HANDLE_WIDTH = 5;
    private final Assembly assembly;

    public AssemblyEditor(Assembly assembly) {
        this.assembly = assembly;
        resetMovablePoints();
    }

    public Assembly getAssembly() {
        return assembly;
    }

    private void resetMovablePoints() {
        resetMovablePoints(new MovablePoint(new Point2D(0, 0), null, null));
    }

    private List<MovablePoint> movablePointsInAssemblyLine(AssemblyLine line) {
        List<MovablePoint> movablePoints = new LinkedList<>();
        // If the previous Assemblable is Delegates or ComponentConnection, we want to move that instead.
        if (line.getStart() instanceof AssemblyLine) {
            // Move Startpoint
            movablePoints.add(new MovablePoint(line.getStartPoint(), HandleType.LINE_CONNECTION, line));
        }
        // Move Midpoint
        Point2D midPoint = line.getEndPoint().midpoint(line.getStartPoint());
        movablePoints.add(new MovablePoint(midPoint, HandleType.LINE_CENTER, line));
        return movablePoints;
    }

    private MovablePoint movablePointFromDelegates(Delegation delegation) {
        Point2D handlePosition = delegation.getEndPoint().midpoint(delegation.getStartPoint());
        return new MovablePoint(handlePosition, HandleType.DELEGATES, delegation);
    }

    private MovablePoint movablePointFromComponentConnection(ComponentConnection componentConnection) {
        return new MovablePoint(componentConnection.getEndPoint(), HandleType.COMPONENT_CONNECTION, componentConnection);
    }

    private class MovablePoint extends Circle {
        HandleType handleType;
        Point2D point;
        Assemblable assemblable;

        private Point2D lastPoint;

        MovablePoint(Point2D point, HandleType handleType, Assemblable assemblable) {
            this.point = point;
            this.assemblable = assemblable;
            this.handleType = handleType;
            super.setLayoutX(point.getX());
            super.setLayoutY(point.getY());
            super.setRadius(5);
            super.setStrokeWidth(2);
            super.setStroke(Paint.valueOf("#00FFFF"));
            this.setOnMousePressed(event -> {
                lastPoint = new Point2D(event.getSceneX(), event.getSceneY());
                event.consume();
                switch (this.handleType) {
                    case LINE_CONNECTION:
                        break;
                    case LINE_CENTER:
                        splitLine(this.assemblable, point);
                        this.handleType = HandleType.LINE_CONNECTION;
                        break;
                    case DELEGATES:
                        break;
                    case COMPONENT_CONNECTION:
                        break;
                    default:
                        break;
                }
            });
            this.setOnMouseDragged(event -> {
                event.consume();
                Point2D pointDelta = new Point2D(event.getSceneX() - lastPoint.getX(), event.getSceneY() - lastPoint.getY());
                lastPoint = new Point2D(event.getSceneX(), event.getSceneY());
                switch (this.handleType) {
                    case LINE_CONNECTION:
                        Point2D newPoint = this.assemblable.getStartPoint().add(pointDelta);
                        this.assemblable.setStart(this.assemblable.getStart(), newPoint);
                        this.assemblable.getStart().setEnd(this.assemblable, newPoint);
                        move(pointDelta);
                        break;
                    case LINE_CENTER:
                        // Should be a can't happen
                        break;
                    case DELEGATES:
                        Delegation delegation = (Delegation) this.assemblable;
                        Side side = delegation.getSide();
                        if (side == Side.LEFT || side == Side.RIGHT) {
                            delegation.moveSimple(new Point2D(0, pointDelta.getY()));
                            move(new Point2D(0, pointDelta.getY()));
                        } else {
                            delegation.moveSimple(new Point2D(pointDelta.getX(), 0));
                            move(new Point2D(pointDelta.getX(), 0));
                        }
                        break;
                    case COMPONENT_CONNECTION:
                        ComponentConnection componentConnection = (ComponentConnection) this.assemblable;
                        side = componentConnection.getSide();
                        if (side == Side.LEFT || side == Side.RIGHT) {
                            componentConnection.moveSimple(new Point2D(0, pointDelta.getY()));
                            move(new Point2D(0, pointDelta.getY()));
                        } else {
                            componentConnection.moveSimple(new Point2D(pointDelta.getX(), 0));
                            move(new Point2D(pointDelta.getX(), 0));
                        }
                        break;
                }
                resetMovablePoints(this);
            });
        }

        private void move(Point2D delta) {
            this.setLayoutX(this.getLayoutX() + delta.getX());
            this.setLayoutY(this.getLayoutY() + delta.getY());
        }

        private void splitLine(Assemblable line, Point2D midPoint) {
            AssemblyLine newLine = new AssemblyLine(midPoint, line.getEnd());
            assembly.getChildren().add(newLine);
            line.getEnd().setStart(newLine, line.getEndPoint());
            line.setEnd(newLine, newLine.getStartPoint());
            newLine.setStart(line, midPoint);
            this.assemblable = newLine;
        }
    }

    private void resetMovablePoints(MovablePoint movedPoint) {
        this.getChildren().removeIf(node -> node != movedPoint);
        this.assembly.getChildren().forEach(a -> {
            assert a instanceof Assemblable;
            if (a instanceof AssemblyLine) {
                getChildren().addAll(movablePointsInAssemblyLine((AssemblyLine) a));
            }
            if (a instanceof Delegation) {
                getChildren().add(movablePointFromDelegates((Delegation) a));
            }
            if (a instanceof ComponentConnection) {
                getChildren().add(movablePointFromComponentConnection((ComponentConnection) a));
            }
        });
    }

    private enum HandleType {
        LINE_CONNECTION,
        LINE_CENTER,
        DELEGATES,
        COMPONENT_CONNECTION
    }

}

