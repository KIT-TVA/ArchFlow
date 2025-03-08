package gui.controllers.interaction;

import gui.Model;
import gui.components.Component;
import gui.components.Composite;
import gui.components.SelectedComponentOverlay;
import gui.components.assembly.components.Assembly;
import gui.components.util.ComponentMover;
import gui.views.ComponentCanvas;
import gui.views.MainLayout;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.Collection;

public class MoveComponentStrategy extends BaseInteractionStrategy {
    private SelectedComponentOverlay overlay;
    private final ComponentMover componentMover;

    public MoveComponentStrategy(MainLayout mainLayout, Model model, InteractionEventDispatcher eventDispatcher) {
        super(mainLayout, model, eventDispatcher);
        componentMover = new ComponentMover(mainLayout.getCanvas(), model.components);
    }


    @Override
    public void componentClicked(Component component, MouseEvent event) {
        setOverlay(component);
    }

    @Override
    public void componentMouseDown(Component component, MouseEvent event) {
        componentMover.setComponent(component);
        componentMover.mouseDownComponent(event);
    }

    @Override
    public void componentMouseOver(Component component, MouseEvent event) {
        componentMover.mouseMovedComponent(component, event);
    }

    @Override
    public void componentDragged(Component component, MouseEvent event) {
        componentMover.mouseDraggedComponent(event);
        if (overlay != null) {
            overlay.reposition();
        }
    }

    @Override
    public void componentMouseUp(Component component, MouseEvent event) {
        componentMover.mouseReleasedComponent(event);
    }

    @Override
    public void canvasClicked(ComponentCanvas canvas, MouseEvent event) {
        canvas.remove(overlay);
        overlay = null;
    }
    @Override
    public void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            if (overlay != null) {
                canvas.remove(overlay);
                overlay = null;
            }
        }
        if (event.getCode() == KeyCode.DELETE && overlay != null) {
            Component componentToDelete = overlay.getComponent();
            deleteComponent(componentToDelete);
        }
    }

    private void setOverlay(Component component) {
        overlay = SelectedComponentOverlay.fromComponent(component);

        overlay.setOnArrowClicked(side -> {
            eventDispatcher.assemblyBuildingStrategy.enable();
            eventDispatcher.assemblyBuildingStrategy.initialize(component, side);
            disable();
        });
        overlay.setOnEdgePressed((event, s) -> {
            event.consume();
            componentMover.mouseDownResizeComponent(event, s);
        });
        overlay.setOnEdgeDragged((event, side) -> {
            event.consume();
            componentMover.mouseDraggedComponent(event);
            overlay.resize();
        });
        overlay.setOnEdgeReleased((event, side) -> {
            event.consume();
            componentMover.mouseReleasedComponent(event);
        });
        canvas.draw(overlay);
    }

    private void deleteComponent(Component componentToDelete) {
        model.components.remove(componentToDelete);
        if (componentToDelete.getParentComponent() == null) {
            canvas.remove(componentToDelete.getContainer());
        } else {
            componentToDelete.getParentComponent().removeChildComponent(componentToDelete);
        }
        Collection<Assembly> assembliesToRemove = model.assemblies.stream().filter(assembly -> (assembly.getProvidingComponent() == componentToDelete || assembly.getRequiringComponent() == componentToDelete)).toList();
        assembliesToRemove.forEach(a -> {
            model.assemblies.remove(a);
            canvas.remove(a);
        });
        if (componentToDelete instanceof Composite) {
            ((Composite) componentToDelete).getChildComponents().forEach(this::deleteComponent);
        }
        canvas.remove(overlay);
        overlay = null;
    }

    @Override
    public void disable() {
        super.disable();
        canvas.remove(overlay);
    }

    @Override
    public void enable() {
        super.enable();
        if (overlay != null) {
            canvas.draw(overlay);
        }
    }
}
