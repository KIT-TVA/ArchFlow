package gui.components;

import antlr.cidlParser.ComponentContext;
import antlr.cidlParser.Method_headerContext;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.List;

public class Atomic extends BaseComponent implements Component {

    private String spec;

    private ComponentContext componentSpec;

    private Runnable editSpecFunction = () -> {
    };


    public Atomic(double x, double y, int width, int height) {
        spec = "Atomic Spec";
        setLayoutX(x);
        setLayoutY(y);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Atomic.fxml"));
            loader.setController(this);
            super.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        name.setText("default");
        rectangle.setWidth(width);
        rectangle.setHeight(height);
        this.setWidth(width);
        this.setHeight(height);
    }

    public Atomic() {
        name.setText("default");
        spec = "";
        this.setPrefSize(100, 100);
        rectangle.setWidth(this.getWidth());
        rectangle.setHeight(this.getHeight());
        setLayoutX(0);
        setLayoutY(0);
    }

    public void onEditSpec(Runnable r) {
        editSpecFunction = r;
    }

    @FXML
    private void editSpec(MouseEvent event) {
        editSpecFunction.run();

    }

    @Override
    public List<Method_headerContext> getProvidedMethods() {
        //return componentSpec.method_header();
        if (componentSpec.interface_provided() != null && componentSpec.interface_provided().method_header() != null) {
            return componentSpec.interface_provided().method_header();
        }
        return List.of();
    }

    @Override
    public List<Method_headerContext> getRequiredMethods() {
        //return componentSpec.method_header();
        if (componentSpec.interface_required() != null && componentSpec.interface_required().method_header() != null) {
            return componentSpec.interface_required().method_header();
        }
        return List.of();

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
    public void setSpec(String s) {
        this.spec = s;
    }
}
