package gui.components;

import antlr.cidlParser;
import antlr.cidlParser.ComponentContext;
import antlr.cidlParser.Method_headerContext;
import antlr.cidlParser.List_subcomponentsContext;
import antlr.cidlParser.AssemblyContext;
import antlr.cidlParser.DelegationContext;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Composite extends BaseComponent implements CompositeComponent {

    private String spec;
    private ComponentContext componentSpec;


    private List<Component> subComponents = new ArrayList<>();


    public Composite(double x, double y, int width, int height) {
        spec = "Composite Spec";
        setLayoutX(x);
        setLayoutY(y);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Composite.fxml"));
            loader.setController(this);
            super.getChildren().add(loader.load());
            super.getChildren().forEach(element -> element.setViewOrder(3));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.name.setText("Composite Component");
        rectangle.setWidth(width);
        rectangle.setHeight(height);
    }

    public Composite() {
        this.name.setText("Composite Component");
        spec = "";
        this.rectangle.setWidth(300);
        this.rectangle.setHeight(50);
        setLayoutX(0);
        setLayoutY(0);
        rectangle.setViewOrder(5);
    }

    @Override
    public void setComponentContext(ComponentContext c) {
        componentSpec = c;
    }

    @Override
    public ComponentContext getComponentContext() {
        return componentSpec;
    }

    @Override
    public String getSpec() {
        return spec;
    }

    @Override
    public void setSpec(String s){
        this.spec = s;
    }

    @FXML
    public void addChildComponent(Component component) {
        if (!subComponents.contains(component)) {
            subComponents.add(component);
            getContainer().getChildren().add(component.getContainer());
            component.getContainer().setViewOrder(2);
        }
        resizeContainerToFitComponent(component);
    }

    private void resizeContainerToFitComponent(Component component) {

        double oldXCoordinate = getLayoutX();
        double oldYCoordinate = getLayoutY();

        changePositionToFitComponent(component);

        double newWidth = Math.max(rectangle.getWidth() + oldXCoordinate - getLayoutX(), component.getContainer().getLayoutX() + component.getContainer().getWidth() + oldXCoordinate - getLayoutX());
        double newHeight = Math.max(rectangle.getHeight() + oldYCoordinate - getLayoutY(), component.getContainer().getLayoutY() + component.getContainer().getHeight() + oldYCoordinate - getLayoutY());

        rectangle.setWidth(newWidth);
        rectangle.setHeight(newHeight);
    }

    private void changePositionToFitComponent(Component component) {
        double newXCoordinate = Math.min(getLayoutX(), getLayoutX() + component.getContainer().getLayoutX());
        double newYCoordinate = Math.min(getLayoutY(), getLayoutY() + component.getContainer().getLayoutY());
        changeLocalPositionRetainingSubcomponentGlobalPosition(newXCoordinate, newYCoordinate);
    }

    private void changeLocalPositionRetainingSubcomponentGlobalPosition(double newXCoordinate, double newYCoordinate) {
        double oldXCoordinate = getLayoutX();
        double oldYCoordinate = getLayoutY();
        setLayoutX(newXCoordinate);
        setLayoutY(newYCoordinate);

        getChildren().forEach(child -> {
            child.setLayoutX(child.getLayoutX() + oldXCoordinate - getLayoutX());
            child.setLayoutY(child.getLayoutY() + oldYCoordinate - getLayoutY());
        });
    }

    public List_subcomponentsContext getSubComponents(){
        return componentSpec.list_subcomponents();
    }

    public List<AssemblyContext> getCompositeAssemblies(){
        return componentSpec.assembly();
    }

    public List<DelegationContext> getCompositeDelegations(){
        return componentSpec.delegation();
    }

    @Override
    public Collection<Component> getChildComponents() {
        return subComponents;
    }

    @Override
    public void removeChildComponent(Component child) {
        subComponents.remove(child);
    }
    
    @Override
    public List<Method_headerContext> getProvidedMethods() {
        return componentSpec.interface_provided().method_header();
    }

    @Override
    public List<Method_headerContext> getRequiredMethods() {
        return componentSpec.interface_required().method_header();

    }
}