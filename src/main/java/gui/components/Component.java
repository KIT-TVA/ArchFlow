package gui.components;

import antlr.cidlParser.ComponentContext;
import antlr.cidlParser.Method_headerContext;
import gui.components.assembly.Assemblable;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Collection;
import java.util.List;

public interface Component {

    String getName();
    void setName(String name);
    Pane getContainer();
    Rectangle getRectangle();
    void setColor(Color c);
    void setComponentContext(ComponentContext c);
    List<Method_headerContext> getProvidedMethods();
    List<Method_headerContext> getRequiredMethods();
    ComponentContext getComponentContext();
    String getSpec();
    void setSpec(String s);
    CompositeComponent getParentComponent();
    void setParentComponent(CompositeComponent parent);
    Collection<Assemblable> getAssemblies();
}
