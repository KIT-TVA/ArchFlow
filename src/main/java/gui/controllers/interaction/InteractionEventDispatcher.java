package gui.controllers.interaction;

import gui.Model;
import gui.components.Component;
import gui.views.ComponentCanvas;
import gui.views.MainLayout;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class InteractionEventDispatcher {
    private final MainLayout mainLayout;
    private final Model model;
    private final ArrayList<InteractionStrategy> interactionStrategies = new ArrayList<>();
    private Component currentComponent;
    protected AssemblyBuildingStrategy assemblyBuildingStrategy;
    protected EditComponentNameStrategy editComponentNameStrategy;
    protected MoveComponentStrategy moveComponentStrategy;
    protected SpecEditingStrategy specEditingStrategy;
    protected AssemblyEditingStrategy assemblyEditingStrategy;

    public InteractionEventDispatcher(MainLayout mainLayout, Model model) {
        this.mainLayout = mainLayout;
        this.model = model;
        assemblyBuildingStrategy = new AssemblyBuildingStrategy(mainLayout, model, this);
        editComponentNameStrategy = new EditComponentNameStrategy(mainLayout, model, this);
        moveComponentStrategy = new MoveComponentStrategy(mainLayout, model, this);
        specEditingStrategy = new SpecEditingStrategy(mainLayout, model, this);
        assemblyEditingStrategy = new AssemblyEditingStrategy(mainLayout, model, this);
        interactionStrategies.add(assemblyBuildingStrategy);
        interactionStrategies.add(editComponentNameStrategy);
        interactionStrategies.add(moveComponentStrategy);
        interactionStrategies.add(specEditingStrategy);
        interactionStrategies.add(assemblyEditingStrategy);
        assemblyBuildingStrategy.disable();
        setHandleCanvasInteraction();
        setHandlerForAllComponents();
        mainLayout.getCanvas().getPane().getScene().setOnKeyPressed(event -> {
            for (InteractionStrategy interactionStrategy : interactionStrategies) {
                if (interactionStrategy.isEnabled()) {
                    interactionStrategy.keyPressed(event);
                }
            }
        });
    }

    private void setHandlerForAllComponents() {
        model.components.forEach(this::setHandleComponentInteraction);
        mainLayout.getCanvas().getPane().getChildren().addListener((ListChangeListener<? super Node>) change -> {
            if (change.next()) {
                change.getAddedSubList().forEach(node -> {
                    if (node instanceof Component) {
                        setHandleComponentInteraction((Component) node);
                    }
                });
            }
        });
    }

    public Component getCurrentComponent() {
        return currentComponent;
    }

    public void clearStrategy() {
        interactionStrategies.clear();
    }

    public void addStrategy(InteractionStrategy strategy) {
        interactionStrategies.add(strategy);
    }

    private void setHandleComponentInteraction(Component component) {
        Pane container = component.getContainer();
        container.setOnMouseClicked(event -> {
            currentComponent = component;
            handleComponentClicked(component, event);
            event.consume();
        });
        container.setOnMouseDragged(event -> {
            handleComponentDragged(component, event);
            event.consume();
        });
        container.setOnMouseReleased(event -> {
            handleComponentReleased(component, event);
            event.consume();
        });
        container.setOnMouseMoved(event -> {
            handleComponentMoved(component, event);
            event.consume();
        });
        container.setOnMousePressed(event -> {
            handleComponentPressed(component, event);
            event.consume();
        });
    }

    private void setHandleCanvasInteraction() {
        ComponentCanvas canvas = mainLayout.getCanvas();
        Pane container = canvas.getPane();
        container.setOnMouseClicked(event -> {
            handleCanvasClicked(canvas, event);
            event.consume();
        });
        container.setOnMouseDragged(event -> {
            handleCanvasDragged(canvas, event);
            event.consume();
        });
        container.setOnMouseReleased(event -> {
            handleCanvasReleased(canvas, event);
            event.consume();
        });
        container.setOnMouseMoved(event -> {
            handleCanvasMoved(canvas, event);
            event.consume();
        });
        container.setOnMousePressed(event -> {
            handleCanvasPressed(canvas, event);
            event.consume();
        });
    }

    private void handleComponentClicked(Component component, MouseEvent event) {
        for (InteractionStrategy strategy : interactionStrategies) {
            if (strategy.isEnabled()) {
                strategy.componentClicked(component, event);
            }
        }
    }

    private void handleComponentDragged(Component component, MouseEvent event) {
        for (InteractionStrategy strategy : interactionStrategies) {
            if (strategy.isEnabled()) {
                strategy.componentDragged(component, event);
            }
        }
    }

    private void handleComponentPressed(Component component, MouseEvent event) {
        for (InteractionStrategy strategy : interactionStrategies) {
            if (strategy.isEnabled()) {
                strategy.componentMouseDown(component, event);
            }
        }
    }

    private void handleComponentReleased(Component component, MouseEvent event) {
        for (InteractionStrategy strategy : interactionStrategies) {
            if (strategy.isEnabled()) {
                strategy.componentMouseUp(component, event);
            }
        }
    }

    private void handleComponentMoved(Component component, MouseEvent event) {
        for (InteractionStrategy strategy : interactionStrategies) {
            if (strategy.isEnabled()) {
                strategy.componentMouseOver(component, event);
            }
        }
    }

    private void handleCanvasClicked(ComponentCanvas canvas, MouseEvent event) {
        for (InteractionStrategy strategy : interactionStrategies) {
            if (strategy.isEnabled()) {
                strategy.canvasClicked(canvas, event);
            }
        }
    }

    private void handleCanvasDragged(ComponentCanvas canvas, MouseEvent event) {
        for (InteractionStrategy strategy : interactionStrategies) {
            if (strategy.isEnabled()) {
                strategy.canvasDragged(canvas, event);
            }
        }
    }
    private void handleCanvasReleased(ComponentCanvas canvas, MouseEvent event) {
        for (InteractionStrategy strategy : interactionStrategies) {
            if (strategy.isEnabled()) {
                strategy.canvasMouseUp(canvas, event);
            }
        }
    }
    private void handleCanvasPressed(ComponentCanvas canvas, MouseEvent event) {
        for (InteractionStrategy strategy : interactionStrategies) {
            if (strategy.isEnabled()) {
                strategy.canvasMouseDown(canvas, event);
            }
        }
    }
    private void handleCanvasMoved(ComponentCanvas canvas, MouseEvent event) {
        for (InteractionStrategy strategy : interactionStrategies) {
            if (strategy.isEnabled()) {
                strategy.canvasMouseOver(canvas, event);
            }
        }
    }
}
