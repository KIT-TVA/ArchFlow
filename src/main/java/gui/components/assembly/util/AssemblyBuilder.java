package gui.components.assembly.util;

import gui.components.Component;
import gui.components.CompositeComponent;
import gui.components.assembly.Assemblable;
import gui.components.assembly.components.*;
import gui.components.util.ComponentTraverser;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.geometry.Side;
import javafx.scene.Node;

import java.util.LinkedList;

public class AssemblyBuilder {
    private final Assembly assembly = new Assembly();
    private final Component startingComponent;
    private Component lastValidComponent;
    private Runnable onEnd;
    private final LinkedList<Assemblable> assemblyStack = new LinkedList<>();
    private Component currentHoverComponent;


    public AssemblyBuilder(Delegates delegates) {
        assert delegates != null;
        assert delegates.getComponent() != null;
        assert delegates.getEnd() == null || delegates.getStart() == null;
        this.startingComponent = delegates.getComponent();
        assemblyStack.add(delegates);
        AssemblyLine startingLine;
        if (delegates.getEnd() == null) {
            startingLine = new AssemblyLine(delegates, delegates.getEndPoint());
            delegates.setEnd(startingLine, startingLine.getStartPoint());
        } else {
            startingLine = new AssemblyLine(delegates, delegates.getStartPoint());
            delegates.setStart(startingLine, startingLine.getStartPoint());
        }
        assemblyStack.add(startingLine);
        assembly.getChildren().add(startingLine);
        assembly.setProvidingComponent(startingComponent);
        assembly.setViewOrder(3);
    }

    public AssemblyBuilder(Component startingComponent, Side side) {
        this.startingComponent = startingComponent;
        Assemblable start;
        if (startingComponent instanceof CompositeComponent) {
            start = new Delegates(startingComponent, side);
        } else {
            start = new ComponentConnection(startingComponent, side, InterfaceType.PROVIDES);
        }
        assembly.getChildren().add((Node) start);
        assemblyStack.add(start);
        AssemblyLine startingLine = new AssemblyLine(start, start.getEndPoint());
        start.setEnd(startingLine, startingLine.getStartPoint());
        assemblyStack.add(startingLine);
        assembly.getChildren().add(startingLine);
        assembly.setProvidingComponent(startingComponent);
        assembly.setViewOrder(3);
    }

    public void setOnEnd(Runnable onEnd) {
        this.onEnd = onEnd;
    }

    public void mouseoverComponent(Component component, Point2D mousePosition) {
        Point2D snappedPoint = getNearestGridPosition(mousePosition);

        assert (assemblyStack.getLast() instanceof AssemblyLine);
        if (currentHoverComponent != component) {
            currentHoverComponent = component;
        }
        if (isValidConnectingComponent(currentHoverComponent)) {
            lastValidComponent = currentHoverComponent;
        }
        assemblyStack.getLast().setEnd(null, snappedPoint);
    }

    private boolean isValidConnectingComponent(Component component) {
        if (component == null) {
            return false;
        }
        return component == startingComponent.getParentComponent() || component.getParentComponent() == startingComponent.getParentComponent() || component.getParentComponent() == startingComponent;
    }

    public void clickComponent(Component component, Point2D mousePosition) {
        Point2D snappedPoint = getNearestGridPosition(mousePosition);
        if (component != currentHoverComponent) {
            mouseoverComponent(component, snappedPoint);
        }
        if (component == startingComponent || component == startingComponent.getParentComponent()) {
            makeJoint(snappedPoint);
        } else if (lastValidComponent != null) {
            end(snappedPoint);
        } else {
            // Should be a can't happen
            System.out.printf("Something is wrong: Component %s was clicked, which doesn't border the starting component %s, but no intermediate component was was touched between them.", component.getName(), startingComponent.getName());
        }
    }

    private void makeJoint(Point2D mousePosition) {
        Point2D snappedPoint = getNearestGridPosition(mousePosition);

        AssemblyLine newLine = new AssemblyLine(assemblyStack.getLast(), snappedPoint);
        assemblyStack.getLast().setEnd(newLine, snappedPoint);
        assemblyStack.addLast(newLine);
        assembly.getChildren().add(newLine);
    }

    private void makeEndingDelegates(Point2D mousePosition) {

        Point2D intersection = getNearestGridPosition(getComponentIntersection(lastValidComponent, assemblyStack.getLast().getStartPoint(), mousePosition));
        Delegates endingDelegates = new Delegates(lastValidComponent, intersection, calculateSideOfPoint(lastValidComponent, intersection));
        assemblyStack.getLast().setEnd(endingDelegates, endingDelegates.getStartPoint());
        endingDelegates.setStart(assemblyStack.getLast(), endingDelegates.getStartPoint());
        assemblyStack.addLast(endingDelegates);
        assembly.getChildren().add(endingDelegates);
    }

    private void makeEndingComponentConnection(Point2D mousePosition) {
        Point2D intersection = getNearestGridPosition(getComponentIntersection(lastValidComponent, assemblyStack.getLast().getStartPoint(), mousePosition));
        ComponentConnection endingComponentConnection = new ComponentConnection(lastValidComponent, calculateSideOfPoint(lastValidComponent, intersection), intersection, InterfaceType.REQUIRES);
        endingComponentConnection.setStart(assemblyStack.getLast(), endingComponentConnection.getStartPoint());
        assemblyStack.getLast().setEnd(endingComponentConnection, endingComponentConnection.getStartPoint());
        assemblyStack.addLast(endingComponentConnection);
        assembly.getChildren().add(endingComponentConnection);
    }

    private void end(Point2D mousePosition) {
        if (lastValidComponent instanceof CompositeComponent) {
            makeEndingDelegates(mousePosition);
        } else {
            makeEndingComponentConnection(mousePosition);
        }
        assembly.setRequiringComponent(lastValidComponent);
        onEnd.run();
    }

    public Assembly getAssembly() {
        return assembly;
    }

    private Point2D getComponentIntersection(Component component, Point2D from, Point2D to) {
        Point2D componentTopLeft = ComponentTraverser.getSwitchedCoordinates(component, null);
        Rectangle2D componentRectangle = new Rectangle2D(componentTopLeft.getX(), componentTopLeft.getY(), component.getContainer().getWidth(), component.getContainer().getHeight());
        return getIntersection(componentRectangle, from, to);
    }

    private Side calculateSideOfPoint(Component component, Point2D point) {
        Point2D origin = ComponentTraverser.getSwitchedCoordinates(component, null);
        double EPSILON = 0.1;
        if (component.getRectangle().getWidth() + origin.getX() - EPSILON < point.getX() && point.getX() < component.getRectangle().getWidth() + origin.getX() + EPSILON) {
            return Side.RIGHT;
        }
        if (origin.getX() - EPSILON < point.getX() && point.getX() < origin.getX() + EPSILON) {
            return Side.LEFT;
        }
        if (origin.getY() - EPSILON < point.getY() && point.getY() < origin.getY() + EPSILON) {
            return Side.TOP;
        }
        if (origin.getY() + component.getRectangle().getHeight() - EPSILON < point.getY() && point.getY() < origin.getY() + component.getRectangle().getHeight() + EPSILON) {
            return Side.BOTTOM;
        } else {
            return null;
        }
    }

    private static Point2D getNearestGridPosition(Point2D point) {
        final int GRID_SIZE = 10;
        double snappedX = ((int) Math.round(point.getX() / GRID_SIZE)) * GRID_SIZE;
        double snappedY = ((int) Math.round(point.getY() / GRID_SIZE)) * GRID_SIZE;
        return new Point2D(snappedX, snappedY);
    }

    //The next three math intensive methods are AI generated.
    private static Point2D getIntersection(Rectangle2D rect, Point2D lineStart, Point2D lineEnd) {
        // Define the rectangle edges
        Point2D[] rectCorners = {new Point2D(rect.getMinX(), rect.getMinY()), new Point2D(rect.getMinX() + rect.getWidth(), rect.getMinY()), new Point2D(rect.getMinX() + rect.getWidth(), rect.getMinY() + rect.getHeight()), new Point2D(rect.getMinX(), rect.getMinY() + rect.getHeight())};

        // Check intersection with each edge of the rectangle
        for (int i = 0; i < rectCorners.length; i++) {
            Point2D p1 = rectCorners[i];
            Point2D p2 = rectCorners[(i + 1) % rectCorners.length];
            Point2D intersection = lineIntersection(lineStart, lineEnd, p1, p2);
            if (intersection != null) {
                return intersection;
            }
        }
        System.out.println("No intersection found");
        return new Point2D(rect.getMinX(), rect.getMinY()); // No intersection found
    }

    private static Point2D lineIntersection(Point2D p1, Point2D p2, Point2D p3, Point2D p4) {
        double A1 = p2.getY() - p1.getY();
        double B1 = p1.getX() - p2.getX();
        double C1 = A1 * p1.getX() + B1 * p1.getY();

        double A2 = p4.getY() - p3.getY();
        double B2 = p3.getX() - p4.getX();
        double C2 = A2 * p3.getX() + B2 * p3.getY();

        double determinant = A1 * B2 - A2 * B1;

        if (determinant == 0) {
            return null; // Lines are parallel
        } else {
            double x = (B2 * C1 - B1 * C2) / determinant;
            double y = (A1 * C2 - A2 * C1) / determinant;

            // Check if the intersection point is within the line segments
            if (isPointOnSegment(p1, p2, new Point2D(x, y)) && isPointOnSegment(p3, p4, new Point2D(x, y))) {
                return new Point2D(x, y);
            }
        }
        return null; // No intersection within the segments
    }

    private static boolean isPointOnSegment(Point2D p1, Point2D p2, Point2D p) {
        return (Math.min(p1.getX(), p2.getX()) <= p.getX() && p.getX() <= Math.max(p1.getX(), p2.getX())) && (Math.min(p1.getY(), p2.getY()) <= p.getY() && p.getY() <= Math.max(p1.getY(), p2.getY()));
    }


}
