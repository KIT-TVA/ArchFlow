package gui.controllers.interaction;

import gui.Model;
import gui.components.Component;
import gui.views.ComponentCanvas;
import gui.views.MainLayout;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

public class EditComponentNameStrategy extends BaseInteractionStrategy {
    private TextField nameField;
    private Component component;

    public EditComponentNameStrategy(MainLayout mainLayout, Model model, InteractionEventDispatcher eventDispatcher) {
        super(mainLayout, model, eventDispatcher);
    }

    @Override
    public void componentClicked(Component component, MouseEvent event) {
        removeTextField();
        if (event.getClickCount() > 1) {
            editComponentName(component);
        }
    }

    @Override
    public void canvasClicked(ComponentCanvas canvas, MouseEvent event) {
        removeTextField();
    }

    private void editComponentName(Component component) {
        removeTextField();
        nameField = new TextField();
        this.component = component;
        component.getContainer().getChildren().add(nameField);
        //Store the name and remove text field when enter is pressed
        nameField.setOnKeyPressed(key -> {
            if (key.getCode().equals(KeyCode.ENTER)) {
                String inputText = nameField.getText();
                //Remove text field
                component.getContainer().getChildren().remove(nameField);
                //Set new name
                component.setName(inputText);
            }
        });
    }

    private void removeTextField() {
        if (nameField != null && component != null) {
            component.getContainer().getChildren().remove(nameField);
            nameField = null;
            component = null;
        }
    }

}
