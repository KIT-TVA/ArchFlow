package gui.components;

import gui.components.assembly.Assemblable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Collection;

public abstract class BaseComponent extends Pane implements Component {
    @FXML
    protected Label name;

    private CompositeComponent parent;

    private final ArrayList<Assemblable> assemblables = new ArrayList<>();

    @FXML
    protected Rectangle rectangle;

    @FXML
    protected Pane container;

    @Override
    public String getName() {
        return name.getText();
    }

    @Override
    public void setName(String name){
        this.name.setText(name);
    }

    @Override
    public Rectangle getRectangle() {
        return rectangle;
    }

    @Override
    public Pane getContainer() {
        return this;
    }

    @Override
    public void setColor(Color c) {
        rectangle.setStroke(c);
    }

    @Override
    public void setParentComponent(CompositeComponent component) {
        parent = component;
    }

    @Override
    public CompositeComponent getParentComponent() {
        return parent;
    }

    @Override
    public Collection<Assemblable> getAssemblies() {
        return assemblables;
    }

}
