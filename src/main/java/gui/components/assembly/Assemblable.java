package gui.components.assembly;

import javafx.geometry.Point2D;

public interface Assemblable {
    public void setStart(Assemblable assembly, Point2D point);
    public void setEnd(Assemblable assembly, Point2D point);
    public Point2D getStartPoint();
    public Point2D getEndPoint();
    public Assemblable getStart();
    public Assemblable getEnd();
    public void moveSimple(Point2D point);
    public void moveRecursive(Assemblable assembly, Point2D point);
}
