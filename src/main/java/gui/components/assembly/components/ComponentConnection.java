package gui.components.assembly.components;

import gui.components.Component;
import gui.components.assembly.Assemblable;
import gui.components.util.ComponentTraverser;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.geometry.Side;


public class ComponentConnection extends Group implements Assemblable{
    private Assemblable assembly;
    private Component component;
    private Point2D position;
    private InterfaceType interfaceType;
    private Side side;
    public ComponentConnection(Component component, Side side, Point2D point, InterfaceType interfaceType) {
        this.side = side;
        this.component = component;
        this.interfaceType = interfaceType;
        this.position = point;
    }

    public ComponentConnection(Component component, Side side, InterfaceType interfaceType) {
        this.component = component;
        this.interfaceType = interfaceType;
        this.side = side;
        Point2D componentOrigin = ComponentTraverser.getSwitchedCoordinates(component, null);
        double width = component.getContainer().getWidth();
        double height = component.getContainer().getHeight();
        switch(side) {
            case TOP:
                position = componentOrigin.add(width / 2, 0);
                break;
            case BOTTOM:
                position = componentOrigin.add(width / 2, height);
                break;
            case LEFT:
                position = componentOrigin.add(0, height / 2);
                break;
            case RIGHT:
                position = componentOrigin.add(width, height / 2);
        }
    }

    @Override
    public void setStart(Assemblable assembly, Point2D point) {
        this.assembly = assembly;
        this.position = point;
    }

    @Override
    public void setEnd(Assemblable assembly, Point2D point) {
        setStart(assembly, point);
    }

    @Override
    public Point2D getStartPoint() {
        return position;
    }

    @Override
    public Point2D getEndPoint() {
        return position;
    }

    @Override
    public Assemblable getStart() {
        if (interfaceType == InterfaceType.REQUIRES) {
            return assembly;
        }
        return null;
    }

    @Override
    public Assemblable getEnd() {
        if (interfaceType == InterfaceType.PROVIDES) {
            return assembly;
        }
        return null;
    }

    @Override
    public void moveSimple(Point2D point) {
        position = position.add(point);
        if (interfaceType == InterfaceType.REQUIRES) {
            assembly.setEnd(this, position);
        } else if (interfaceType == InterfaceType.PROVIDES) {
            assembly.setStart(this, position);
        }
    }
    @Override
    public void moveRecursive(Assemblable assembly, Point2D point) {

    }

    public Side getSide() {
        return side;
    }

    public Component getComponent() {
        return component;
    }
}
