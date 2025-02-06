package gui.components.assembly.components;

import gui.components.Component;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;


public class Assembly extends Parent {
    private Component providingComponent;
    private Component requiringComponent;
    private final Group group = new Group();
    public Assembly() {
        super();
        super.getChildren().add(group);
    }
    @Override
    public ObservableList<Node> getChildren() {
        return group.getChildren();
    }

    public void setProvidingComponent(Component providingComponent) {
        this.providingComponent = providingComponent;
    }

    public void setRequiringComponent(Component requiringComponent) {
        this.requiringComponent = requiringComponent;
    }

    public Component getProvidingComponent() {
        return providingComponent;
    }

    public Component getRequiringComponent() {
        return requiringComponent;
    }

    public boolean isComplete() {
        return requiringComponent != null && providingComponent != null;
    }
}
