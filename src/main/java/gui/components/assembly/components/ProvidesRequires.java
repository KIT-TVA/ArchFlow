package gui.components.assembly.components;

import gui.components.assembly.Assemblable;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Transform;

import java.io.IOException;

public class ProvidesRequires extends Parent implements Assemblable {
    private Assemblable start;
    private Assemblable end;
    private AssemblyLine provides;
    private AssemblyLine requires;
    private Pane connection;
    private final Group group = new Group();
    private Point2D startPoint;
    private Point2D endPoint;

    public ProvidesRequires(Assemblable start, Assemblable end) {
        initialize(start.getEndPoint(), end.getStartPoint());
        this.start = start;
        this.end = end;
    }

    public ProvidesRequires(Assemblable start, Point2D endPoint) {
        this.start = start;
        initialize(start.getEndPoint(), endPoint);
    }

    private void initialize(Point2D startPoint, Point2D endPoint) {
        super.getChildren().add(group);
        group.setMouseTransparent(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Connection.fxml"));
        try {
            connection = loader.load();
            group.getChildren().add(connection);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        Point2D midpoint = startPoint.midpoint(endPoint);
        provides = new AssemblyLine(start, midpoint);
        requires = new AssemblyLine(midpoint, endPoint);
        reAdjustPosition();
        super.getChildren().add(provides);
        super.getChildren().add(requires);
    }

    private void reAdjustPosition() {
        double angle = Math.toDegrees(Math.atan2(endPoint.getY() - startPoint.getY(), endPoint.getX() - startPoint.getX()));;
        Point2D midPoint = startPoint.midpoint(endPoint);
        group.setLayoutX(startPoint.getX() + startPoint.distance(midPoint));
        group.setLayoutY(startPoint.getY() - connection.getHeight() / 2);
        group.getTransforms().setAll(Transform.rotate(angle, - startPoint.distance(midPoint), connection.getHeight() / 2));
    }

    @Override
    public void setStart(Assemblable assembly, Point2D point) {
        start = assembly;
        Point2D midpoint = point.midpoint(endPoint);
        Point2D normalizedVector = endPoint.subtract(point).normalize();
        Point2D midpointEnd = midpoint.add(normalizedVector.multiply(connection.getWidth()));
        provides.setStart(assembly, point);
        provides.setEnd(requires, midpoint);
        requires.setStart(provides, midpointEnd);
        startPoint = point;
        reAdjustPosition();
    }

    @Override
    public void setEnd(Assemblable assembly, Point2D point) {
        end = assembly;
        endPoint = point;
        Point2D midpoint = point.midpoint(startPoint);
        Point2D normalizedVector = point.subtract(startPoint).normalize();
        Point2D midpointEnd = midpoint.add(normalizedVector.multiply(connection.getWidth()));
        requires.setEnd(assembly, point);
        requires.setStart(provides, midpointEnd);
        provides.setEnd(requires, midpoint);
        reAdjustPosition();
    }

    @Override
    public Point2D getStartPoint() {
        return provides.getStartPoint();
    }

    @Override
    public Point2D getEndPoint() {
        return requires.getEndPoint();
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

    }

    @Override
    public void moveRecursive(Assemblable assembly, Point2D point) {

    }
}
