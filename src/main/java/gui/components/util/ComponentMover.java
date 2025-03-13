package gui.components.util;

import gui.components.Component;
import gui.components.CompositeComponent;
import gui.views.ComponentCanvas;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.Collection;
import java.util.Optional;


public class ComponentMover {
    private final ComponentCanvas canvas;
    private final Collection<Component> rootComponents;
    private boolean resizeMode = false;
    private Side direction;
    private boolean first_click = true;
    private boolean firstMove;
    double scene_x;
    double scene_y;

    private Component component;

    public ComponentMover(ComponentCanvas canvas, Collection<Component> rootComponents) {
        this.canvas = canvas;
        this.rootComponents = rootComponents;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public void mouseDownResizeComponent(MouseEvent event, Side side) {
        resizeMode = true;
        direction = side;

        scene_x = event.getX();
        scene_y = event.getY();
        firstMove = true;
        first_click = !first_click;
    }

    public void mouseDownComponent(MouseEvent event) {
        Pane rect = component.getContainer();
        resizeMode = false;
        rect.toFront();
        saveLastPoint(event);
        first_click = !first_click;
    }

    public void mouseDraggedComponent(MouseEvent event) {
        if (resizeMode) {
            resizeComponent(event, component);
        } else {
            dragComponent(event, component);
        }
    }

    public void mouseMovedComponent(Component component, MouseEvent event) {
    }

    public void mouseReleasedComponent(MouseEvent event) {
        removeFromParentIfOutOfBounds(component);
        Optional<CompositeComponent> intersectingComposite;
        if (component.getParentComponent() != null) {
            intersectingComposite = ComponentTraverser.findDeepestIntersectingComponent(component, component.getParentComponent().getChildComponents());
        } else {
            intersectingComposite = ComponentTraverser.findDeepestIntersectingComponent(component, rootComponents.stream().filter(c -> c.getParentComponent() == null).toList());
        }
        intersectingComposite.ifPresent(value -> {
            canvas.remove(component.getContainer());
            ComponentTraverser.changeParentComponent(component, value);
            value.addChildComponent(component);
        });
        // This forces a resize of the parent component
        if (component.getParentComponent() != null) {
            component.getParentComponent().addChildComponent(component);
        }
    }

    private void resizeComponent(MouseEvent e, Component component) {
        Pane container = component.getContainer();
        Rectangle rect = component.getRectangle();
        if (firstMove) {
            saveLastPoint(e);
            firstMove = false;
        }
        switch (direction) {
            case LEFT:
                container.setLayoutX(getNearestGridPosition(container.getLayoutX() + (e.getSceneX() - scene_x)));
                rect.setWidth(getNearestGridPosition(rect.getWidth() - (e.getSceneX() - scene_x)));
                break;
            case RIGHT:
                rect.setWidth(getNearestGridPosition(rect.getWidth() + e.getSceneX() - scene_x));
                break;
            case TOP:
                container.setLayoutY(getNearestGridPosition(container.getLayoutY() + (e.getSceneY() - scene_y)));
                rect.setHeight(getNearestGridPosition(rect.getHeight() - (e.getSceneY() - scene_y)));
                break;
            case BOTTOM:
                rect.setHeight(getNearestGridPosition(rect.getHeight() + e.getSceneY() - scene_y));
                break;
        }
        saveLastPoint(e);
    }

    private void saveLastPoint(MouseEvent e) {
        if (getNearestGridPosition(e.getSceneX() - scene_x) != 0) {
            scene_x = getNearestGridPosition(e.getSceneX());
        }
        if (getNearestGridPosition(e.getSceneY() - scene_y) != 0) {
            scene_y = getNearestGridPosition(e.getSceneY());
        }
    }

    private void dragComponent(MouseEvent e, Component component) {
        component.getContainer().setLayoutX(getNearestGridPosition(component.getContainer().getLayoutX() + e.getSceneX() - scene_x));
        component.getContainer().setLayoutY(getNearestGridPosition(component.getContainer().getLayoutY() + e.getSceneY() - scene_y));
        moveAssemblables(component, getNearestGridPosition(new Point2D(e.getSceneX() - scene_x, e.getSceneY() - scene_y)));
        saveLastPoint(e);
    }

    private void moveAssemblables(Component component, Point2D vector) {
        component.getAssemblies().forEach(assemblable -> {
            assemblable.moveSimple(vector);
        });
        if (component instanceof CompositeComponent) {
            ((CompositeComponent) component).getChildComponents().forEach(c -> {
                moveAssemblables(c, vector);
            });
        }
    }

    private void removeFromParentIfOutOfBounds(Component component) {
        Pane rect = component.getContainer();
        if (component.getParentComponent() != null) {
            if (!rect.getBoundsInParent().intersects(component.getParentComponent().getRectangle().getLayoutBounds())) {
                CompositeComponent newParent = component.getParentComponent().getParentComponent();
                if (newParent != null) {
                    ComponentTraverser.changeParentComponent(component, newParent);
                    //In case it was dragged out of this parent as well
                    removeFromParentIfOutOfBounds(component);
                } else {
                    // Redraw in main canvas. Would be easier if Canvas were itself a composite.
                    ComponentTraverser.changeParentComponent(component, null);
                    canvas.draw(component);
                }
            }
        }
    }

    private Point2D getNearestGridPosition(Point2D point) {
        Grid grid = new Grid(10);
        return grid.getNearestGridPosition(point);
    }

    private double getNearestGridPosition(double value) {
        Grid grid = new Grid(10);
        return grid.getNearestGridPosition(value);
    }
}
