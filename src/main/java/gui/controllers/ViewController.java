package gui.controllers;

import gui.Model;
import gui.components.Atomic;
import gui.components.Composite;
import gui.controllers.interaction.InteractionEventDispatcher;
import gui.views.ComponentCanvas;
import gui.views.MainLayout;

public class ViewController {
    private final Model model;
    private final ComponentCanvas canvas;
    private final MainLayout mainLayout;
    private InteractionEventDispatcher eventDispatcher;

    public ViewController(Model model, MainLayout mainLayout, ComponentCanvas canvas) {
        this.model = model;
        this.canvas = canvas;
        this.mainLayout = mainLayout;
        eventDispatcher = new InteractionEventDispatcher(mainLayout, model);
        mainLayout.setOnDiagramLoaded(newModel -> {
            model.components = newModel.components;
            model.assemblies = newModel.assemblies;
            model.components.forEach(c -> {
                if (c.getParentComponent() == null) {
                    canvas.draw(c);
                }
            });
            model.assemblies.forEach(canvas::draw);
        });

        //Set Events on non-changing components
        setHandleAddAtomicComponent();
        setHandleAddCompositeComponent();
    }


    private void setHandleAddAtomicComponent() {
        mainLayout.componentList.atomic.setOnAction(e -> {
            Atomic a = new Atomic(20, 20, 50, 50);
            //Set component specific handlers
            model.add_component(a);
            canvas.draw(a);

        });
    }

    private void setHandleAddCompositeComponent() {
        mainLayout.componentList.composite.setOnAction(e -> {
            Composite c = new Composite(20, 20, 250, 50);
            //Set component specific handlers
            model.add_component(c);
            canvas.draw(c);
        });
    }
}
