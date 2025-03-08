package gui.controllers.interaction;

import gui.Model;
import gui.components.Component;
import gui.views.MainLayout;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

public class EditComponentNameStrategy extends BaseInteractionStrategy {


    public EditComponentNameStrategy(MainLayout mainLayout, Model model, InteractionEventDispatcher eventDispatcher) {
        super(mainLayout, model, eventDispatcher);
    }

    @Override
    public void componentClicked(Component component, MouseEvent event) {
        if (event.getClickCount() > 1) {
            editComponentName(component);
        }
    }

    private static void editComponentName(Component component) {
        TextField textField = new TextField();
        component.getContainer().getChildren().add(textField);
        //Store the name and remove text field when enter is pressed
        textField.setOnKeyPressed(key -> {
            if (key.getCode().equals(KeyCode.ENTER)) {
                String inputText = textField.getText();
                System.out.println(inputText);
                //Remove text field
                component.getContainer().getChildren().remove(textField);
                //Set new name
                component.setName(inputText);
            }
        });
    }

}
