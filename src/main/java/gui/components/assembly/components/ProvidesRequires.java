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
        double adjustedAngle = 0 <= angle && angle <= 180? angle : 360 - angle;
        Point2D newConnectionPoint = Transform.rotate(angle, midPoint.getX(), midPoint.getY()).transform(midPoint.getX() - 0.5 * connection.getWidth(), midPoint.getY() - 0.5 * connection.getHeight());
        Point2D offset = new Point2D(Math.sin(Math.toRadians(adjustedAngle)) * connection.getWidth() / 2, Math.cos(Math.toRadians(adjustedAngle)) * connection.getHeight() / 2);
        group.setLayoutX(newConnectionPoint.getX());
        group.setLayoutY(newConnectionPoint.getY());
        group.setRotate(angle);
    }

    @Override
    public void setStart(Assemblable assembly, Point2D point) {
        start = assembly;
        provides.setStart(assembly, point);
        startPoint = point;
        reAdjustPosition();
    }

    @Override
    public void setEnd(Assemblable assembly, Point2D point) {
        end = assembly;
        endPoint = point;
        requires.setEnd(assembly, point);
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
