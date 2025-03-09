package gui.controllers.interaction;

import gui.Model;
import gui.components.Component;
import gui.components.assembly.components.Assembly;
import gui.components.assembly.util.AssemblyEditor;
import gui.views.ComponentCanvas;
import gui.views.MainLayout;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class AssemblyEditingStrategy extends BaseInteractionStrategy {
    private AssemblyEditor assemblyEditor;

    public AssemblyEditingStrategy(MainLayout mainLayout, Model model, InteractionEventDispatcher eventDispatcher) {
        super(mainLayout, model, eventDispatcher);
        model.assemblies.forEach(this::setAssemblyInteractionHandler);
        watchForNewAssemblies();
    }

    private void setAssemblyInteractionHandler(Assembly assembly) {
        assembly.getChildren().forEach(assemblable -> {
            assemblable.setOnMouseEntered(event -> initializeAssemblyEditor(assembly));
        });
    }

    private void watchForNewAssemblies() {
        canvas.getPane().getChildren().addListener((ListChangeListener<? super Node>) change -> {
            if (change.next()) {
                change.getAddedSubList().forEach(node -> {
                    if (node instanceof Assembly) {
                        setAssemblyInteractionHandler((Assembly) node);
                    }
                });
            }
        });
    }

    private void initializeAssemblyEditor(Assembly assembly) {
        if (!enabled || assemblyEditor != null) {
            return;
        }
        assemblyEditor = new AssemblyEditor(assembly);
        canvas.draw(assemblyEditor);
    }

    @Override
    public void canvasClicked(ComponentCanvas canvas, MouseEvent event) {
        finishEditing();
    }

    @Override
    public void componentClicked(Component component, MouseEvent event) {
        finishEditing();
    }

    @Override
    public void componentMouseDown(Component component, MouseEvent event) {
        finishEditing();
    }

    @Override
    public void disable() {
        super.disable();
        finishEditing();
    }

    private void finishEditing() {
        if (assemblyEditor != null) {
            canvas.remove(assemblyEditor);
            assemblyEditor = null;
        }
    }
}
