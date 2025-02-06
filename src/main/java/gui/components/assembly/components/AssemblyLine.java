package gui.components.assembly.components;

import gui.components.assembly.Assemblable;
import javafx.geometry.Point2D;
import javafx.scene.shape.Line;

public class AssemblyLine extends Line implements Assemblable {
    private Assemblable start;
    private Assemblable end;
    private Point2D startPoint;
    private Point2D endPoint;

    public AssemblyLine(Assemblable start, Point2D endPoint) {
        this.start = start;
        this.startPoint = start.getEndPoint();
        this.endPoint = endPoint;
        setStartX(start.getEndPoint().getX());
        setStartY(start.getEndPoint().getY());
        setEndX(endPoint.getX());
        setEndY(endPoint.getY());
    }

    public AssemblyLine(Point2D startPoint, Assemblable end) {
        this.end = end;
        this.endPoint = end.getStartPoint();
        this.startPoint = startPoint;
        setEndX(end.getStartPoint().getX());
        setEndY(end.getStartPoint().getY());
        setStartX(startPoint.getX());
        setStartY(startPoint.getY());
    }

    public AssemblyLine(Assemblable start, Assemblable end) {
        this.start = start;
        this.startPoint = start.getEndPoint();
        this.end = end;
        this.endPoint = end.getStartPoint();
        setStartX(start.getEndPoint().getX());
        setStartY(start.getEndPoint().getY());
        setEndX(endPoint.getX());
        setEndY(endPoint.getY());
    }

    @Override
    public void setStart(Assemblable assembly, Point2D point) {
        start = assembly;
        startPoint = point;
        setStartX(point.getX());
        setStartY(point.getY());
    }

    @Override
    public void setEnd(Assemblable assembly, Point2D point) {
        end = assembly;
        endPoint = point;
        setEndX(point.getX());
        setEndY(point.getY());
    }

    @Override
    public Point2D getStartPoint() {
        startPoint = new Point2D(getStartX(), getStartY());
        return startPoint;
    }

    @Override
    public Point2D getEndPoint() {
        endPoint = new Point2D(getEndX(), getEndY());
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
    }

    @Override
    public void moveRecursive(Assemblable assembly, Point2D point) {

    }
}
