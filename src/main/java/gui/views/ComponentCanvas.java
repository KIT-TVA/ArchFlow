package gui.views;

import gui.Model;
import gui.components.Component;
import gui.components.SelectedComponentOverlay;
import gui.components.assembly.components.Assembly;
import gui.components.assembly.components.DelegationOverlay;
import gui.components.assembly.util.AssemblyEditor;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class ComponentCanvas {
    Model model;

    @FXML
    private Pane pane;

    public ComponentCanvas() {
    }

    public void initialize() {
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void draw(Component c) {
        boolean success = pane.getChildren().add(c.getContainer());
        c.getContainer().setViewOrder(2);
        if (!success) {
            //TODO handle case were adding the node fails
        }
    }

    public void draw(AssemblyEditor assemblyEditor) {
        pane.getChildren().add(assemblyEditor);
    }


    private SelectedComponentOverlay overlay;

    public void draw(SelectedComponentOverlay overlay) {
        if (this.overlay != null) {
            pane.getChildren().remove(this.overlay);
        }
        pane.getChildren().add(overlay);
        this.overlay = overlay;
        overlay.setViewOrder(1);
    }

    public void draw(DelegationOverlay overlay) {
        if (overlay != null) {
            pane.getChildren().add(overlay);
            overlay.setViewOrder(0);
        }
    }

    public void draw(Assembly assembly) {
        if (assembly != null) {
            pane.getChildren().add(assembly);
        }
    }

    public Pane getPane() {
        return pane;
    }

    public void remove(Node shape) {
        pane.getChildren().remove(shape);
    }

    public void updateAssemblyColors(List<Assembly> invalidAssemblies) {
        List<Assembly> validAssemblies = new ArrayList<>();
        for (Node node : pane.getChildren()){
            if(node instanceof Assembly){
                if(invalidAssemblies == null || !invalidAssemblies.contains(node)){
                    validAssemblies.add((Assembly)node);
                }
            }
        }
        if(invalidAssemblies != null) {
            for (Assembly assembly : invalidAssemblies) {
                assembly.getProvidingComponent().setColor(Color.RED);
                assembly.getRequiringComponent().setColor(Color.RED);
            }
        }
        for (Assembly assembly : validAssemblies){
            System.out.println("Coloring valid assemblies");
            assembly.getProvidingComponent().setColor(Color.GREEN);
            assembly.getRequiringComponent().setColor(Color.GREEN);
        }

    }
}
