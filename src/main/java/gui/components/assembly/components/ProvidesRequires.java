package gui.components.assembly.components;

import gui.components.assembly.Assemblable;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ProvidesRequires extends Group implements Assemblable {
    private Assemblable start;
    private Assemblable end;
    private AssemblyLine provides;
    private AssemblyLine requires;
    private Pane connection;
    public ProvidesRequires(Assemblable start, Assemblable end) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Connection.fxml"));
        try {
            connection = loader.load();
            super.getChildren().add(connection);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.setMouseTransparent(true);
        this.start = start;
        this.end = end;
        Point2D midpoint = start.getEndPoint().midpoint(end.getStartPoint());
        provides = new AssemblyLine(start, midpoint);
        requires = new AssemblyLine(midpoint, end);
        double angle = start.getEndPoint().angle(end.getEndPoint());
        Point2D offset = new Point2D(Math.sin(Math.toRadians(angle)) * connection.getWidth() / 2, Math.sin(Math.toRadians(angle)) * connection.getHeight() / 2);
        connection.setLayoutX(midpoint.getX() - offset.getX());
        connection.setLayoutY(midpoint.getY() - offset.getY());
        connection.setRotate(- new Point2D(1, 0).angle(end.getStartPoint().subtract(start.getEndPoint())));
        super.getChildren().add(provides);
        super.getChildren().add(requires);
    }

    @Override
    public void setStart(Assemblable assembly, Point2D point) {
        start = assembly;
        provides.setStart(assembly, point);
    }

    @Override
    public void setEnd(Assemblable assembly, Point2D point) {
        end = assembly;
        requires.setEnd(assembly, point);
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
