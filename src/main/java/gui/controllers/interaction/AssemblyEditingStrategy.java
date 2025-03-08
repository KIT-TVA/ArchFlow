package gui.controllers.interaction;

import gui.Model;
import gui.views.MainLayout;

public class AssemblyEditingStrategy extends BaseInteractionStrategy{

    public AssemblyEditingStrategy(MainLayout mainLayout, Model model, InteractionEventDispatcher eventDispatcher) {
        super(mainLayout, model, eventDispatcher);
    }
}
