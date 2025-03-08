package gui.controllers.interaction;

import gui.Model;
import gui.views.ComponentCanvas;
import gui.views.MainLayout;

public abstract class BaseInteractionStrategy implements InteractionStrategy{
    protected MainLayout mainLayout;
    protected ComponentCanvas canvas;
    protected Model model;
    protected InteractionEventDispatcher eventDispatcher;
    protected boolean enabled;

    @Override
    public void disable() {
        enabled = false;
    }
    @Override
    public void enable() {
        enabled = true;
    }
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public BaseInteractionStrategy(MainLayout mainLayout, Model model, InteractionEventDispatcher eventDispatcher) {
        this.mainLayout = mainLayout;
        this.model = model;
        canvas = mainLayout.getCanvas();
        this.eventDispatcher = eventDispatcher;
        enable();
    }
}
