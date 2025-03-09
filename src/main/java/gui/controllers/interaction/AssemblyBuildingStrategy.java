package gui.controllers.interaction;

import gui.Model;
import gui.components.Component;
import gui.components.assembly.components.Assembly;
import gui.components.assembly.components.ComponentConnection;
import gui.components.assembly.components.Delegates;
import gui.components.assembly.util.AssemblyBuilder;
import gui.components.util.ComponentTraverser;
import gui.views.ComponentCanvas;
import gui.views.MainLayout;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class AssemblyBuildingStrategy extends BaseInteractionStrategy {
    private AssemblyBuilder assemblyBuilder;

    public AssemblyBuildingStrategy(MainLayout mainLayout, Model model, InteractionEventDispatcher eventDispatcher) {
        super(mainLayout, model, eventDispatcher);
    }

    @Override
    public void componentClicked(Component component, MouseEvent event) {
        handleAssemblyBuildingClick(component, event);
    }

    @Override
    public void canvasClicked(ComponentCanvas canvas, MouseEvent event) {
        handleAssemblyBuildingClick(null, event);
    }

    @Override
    public void componentMouseOver(Component component, MouseEvent event) {
        Point2D point = ComponentTraverser.getSwitchedCoordinates(component, null).add(new Point2D(event.getX(), event.getY()));
        assemblyBuilder.mouseoverComponent(component, point);
    }

    @Override
    public void canvasMouseOver(ComponentCanvas canvas, MouseEvent event) {
        assemblyBuilder.mouseoverComponent(null, new Point2D(event.getX(), event.getY()));
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            model.assemblies.remove(assemblyBuilder.getAssembly());
            canvas.remove(assemblyBuilder.getAssembly());
            assemblyBuilder = null;
            disable();
        }
    }

    public void initialize(Component component, Side side) {
        assemblyBuilder = new AssemblyBuilder(component, side);
        initializeAssemblyBuilder();
    }

    private void handleAssemblyBuildingClick(Component component, MouseEvent e) {
        Point2D point = ComponentTraverser.getSwitchedCoordinates(component, null).add(new Point2D(e.getX(), e.getY()));
        assemblyBuilder.clickComponent(component, point);
    }

    private void initializeAssemblyBuilder() {
        assemblyBuilder.setOnEnd(() -> {
            if (!deleteAssemblyIfIncomplete()) {
                Assembly assembly = assemblyBuilder.getAssembly();
                assembly.getChildren().forEach(a -> {
                    if (a instanceof Delegates) {
                        ((Delegates) a).getComponent().getAssemblies().add((Delegates) a);
                        a.setMouseTransparent(false);
                    } else if (a instanceof ComponentConnection) {
                        ((ComponentConnection) a).getComponent().getAssemblies().add((ComponentConnection) a);
                    }
                });
            }
            assemblyBuilder.getAssembly().setViewOrder(0);
            assemblyBuilder = null;
            this.disable();
        });
        canvas.draw(assemblyBuilder.getAssembly());
        model.assemblies.add(assemblyBuilder.getAssembly());
    }

    private boolean deleteAssemblyIfIncomplete() {
        if (!assemblyBuilder.getAssembly().isComplete()) {
            canvas.remove(assemblyBuilder.getAssembly());
            model.assemblies.remove(assemblyBuilder.getAssembly());
            return true;
        }
        return false;
    }

    @Override
    public void enable() {
        super.enable();
        eventDispatcher.moveComponentStrategy.disable();
        eventDispatcher.assemblyEditingStrategy.disable();
    }

    @Override
    public void disable() {
        super.disable();
        eventDispatcher.moveComponentStrategy.enable();
        eventDispatcher.assemblyEditingStrategy.enable();
    }
}
