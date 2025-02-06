package gui.components.util;

import gui.components.Component;
import gui.components.CompositeComponent;
import javafx.geometry.Point2D;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

public class ComponentTraverser {
    public static Optional<CompositeComponent> findDeepestIntersectingComponent(Component originalComponent, Collection<Component> components) {
        Optional<Component> firstIntersectingComponent = components.stream().filter(component -> component instanceof CompositeComponent && component != originalComponent && originalComponent.getRectangle().localToScene(originalComponent.getRectangle().getBoundsInLocal()).intersects(component.getRectangle().localToScene(component.getRectangle().getBoundsInLocal()))).findFirst();
        if (firstIntersectingComponent.isPresent()) {
            Optional<CompositeComponent> nextIteration = findDeepestIntersectingComponent(originalComponent, ((CompositeComponent) firstIntersectingComponent.get()).getChildComponents());
            if (nextIteration.isPresent()) {
                return nextIteration;
            } else {
                return Optional.of((CompositeComponent) firstIntersectingComponent.get());
            }
        } else {
            return Optional.empty();
        }
    }

    public static CompositeComponent findDeepestCommonComposite(Component component1, Component component2) {
        LinkedList<CompositeComponent> component1Parents = new LinkedList<>();
        LinkedList<CompositeComponent> component2Parents = new LinkedList<>();
        if (component1 instanceof CompositeComponent) {
            component1Parents.addFirst((CompositeComponent) component1);
        }
        if (component2 instanceof CompositeComponent) {
            component2Parents.addFirst((CompositeComponent) component2);
        }
        if (component1 != null) {

            for (CompositeComponent i = component1.getParentComponent(); i != null; i = i.getParentComponent()) {
                component1Parents.addFirst(i);
            }
        }
        if (component2 != null) {

            for (CompositeComponent i = component2.getParentComponent(); i != null; i = i.getParentComponent()) {
                component2Parents.addFirst(i);
            }
        }
        int lowestPossibleDepth = Math.min(component1Parents.size(), component2Parents.size());

        for (int i = lowestPossibleDepth - 1; i >= 0; i--) {
            if (component1Parents.get(i) == component2Parents.get(i)) {
                return component1Parents.get(i);
            }
        }
        return null;
    }

    public static void switchCoordinateSystem(Component component, CompositeComponent target) {
        Point2D newCoordinates = getSwitchedCoordinates(component, target);
        component.getContainer().setLayoutX(newCoordinates.getX());
        component.getContainer().setLayoutY(newCoordinates.getY());
    }

    public static Point2D getSwitchedCoordinates(Component component, CompositeComponent target) {
        CompositeComponent deepestCommonParent = findDeepestCommonComposite(component, target);
        double offsetXTarget = 0;
        double offsetYTarget = 0;
        for (Component i = target; i != deepestCommonParent; i = i.getParentComponent()) {
            offsetXTarget += i.getContainer().getLayoutX();
            offsetYTarget += i.getContainer().getLayoutY();
        }
        double offsetXComponent = 0;
        double offsetYComponent = 0;
        for (Component i = component; i != deepestCommonParent; i = i.getParentComponent()) {
            offsetXComponent += i.getContainer().getLayoutX();
            offsetYComponent += i.getContainer().getLayoutY();
        }
        return new Point2D(offsetXComponent - offsetXTarget, offsetYComponent - offsetYTarget);
    }

    public static void changeParentComponent(Component component, CompositeComponent target) {
        switchCoordinateSystem(component, target);
        if (component.getParentComponent() != null) {
            component.getParentComponent().removeChildComponent(component);
        }
        component.setParentComponent(target);
        if (target != null) {
            target.addChildComponent(component);
        }
    }
}
