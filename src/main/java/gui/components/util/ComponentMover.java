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

    public void mouseDownComponentResized(MouseEvent event, Side side) {
        resizeMode = true;
        direction = side;

        scene_x = event.getX();
        scene_y = event.getY();
        firstMove = true;
        if (first_click) {
            first_click = false;
        } else {
            first_click = true;
        }
    }

    public void mouseDownComponent(MouseEvent event) {
        Pane rect = component.getContainer();
        resizeMode = false;
        rect.toFront();
        scene_x = event.getSceneX();
        scene_y = event.getSceneY();
        if (first_click) {
            first_click = false;
        } else {
            first_click = true;
        }
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
            scene_x = e.getSceneX();
            scene_y = e.getSceneY();
            firstMove = false;
        }
        switch (direction) {
            case LEFT:
                container.setLayoutX(container.getLayoutX() + (e.getSceneX() - scene_x));
                rect.setWidth(rect.getWidth() - (e.getSceneX() - scene_x));
                break;
            case RIGHT:
                rect.setWidth(rect.getWidth() + e.getSceneX() - scene_x);
                break;
            case TOP:
                container.setLayoutY(container.getLayoutY() + (e.getSceneY() - scene_y));
                rect.setHeight(rect.getHeight() - (e.getSceneY() - scene_y));
                break;
            case BOTTOM:
                rect.setHeight(rect.getHeight() + e.getSceneY() - scene_y);
                break;
        }
        scene_x = e.getSceneX();
        scene_y = e.getSceneY();
    }

    private void dragComponent(MouseEvent e, Component component) {
        component.getContainer().setLayoutX(component.getContainer().getLayoutX() + e.getSceneX() - scene_x);
        component.getContainer().setLayoutY(component.getContainer().getLayoutY() + e.getSceneY() - scene_y);
        moveAssemblables(component, new Point2D(e.getSceneX() - scene_x, e.getSceneY() - scene_y));
        scene_x = e.getSceneX();
        scene_y = e.getSceneY();
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
}
