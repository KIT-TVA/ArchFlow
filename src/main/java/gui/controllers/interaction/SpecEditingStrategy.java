package gui.controllers.interaction;

import antlr.cidlParser;
import composition.CheckComposition;
import gui.Model;
import gui.components.Component;
import gui.components.Composite;
import gui.views.MainLayout;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import utils.InterfaceParser;
import utils.TextFileReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SpecEditingStrategy extends BaseInteractionStrategy{
    CheckComposition checker;
    InterfaceParser parser;


    public SpecEditingStrategy(MainLayout mainLayout, Model model, InteractionEventDispatcher eventDispatcher) {
        super(mainLayout, model, eventDispatcher);
        parser = new InterfaceParser();
        checker = new CheckComposition(model);
        setHandleLoadInterfaceFromFile();
        setHandleSaveInterfaceToFile();
        setHandleCheckComposite();
    }

    @Override
    public void componentClicked(Component component, MouseEvent mouseEvent) {
        initializeEditingFields(component);
    }

    private void initializeEditingFields(Component component) {
        //Clear spec field
        mainLayout.clearInterfaceTextField();
        //Clear error view
        mainLayout.clearErrorsTextField();
        //Set the spec for the current component
        mainLayout.setInterfaceTextField(component.getSpec());
    }

    private void setHandleCheckComposite(){
        mainLayout.checkComposite.setOnAction(e -> {
            System.out.println("Composite Check Clicked");
            System.out.println(getCurrentComponent());
            if(checker.checkComposite((Composite)getCurrentComponent())){
                System.out.println("All OK");
                getCurrentComponent().setColor(Color.GREEN);
            } else{
                System.out.println("Composition not OK");
                getCurrentComponent().setColor(Color.RED);
            }
        });

    }

    private void setHandleSaveInterfaceToFile() {
        mainLayout.saveSpec.setOnAction(e -> {
            System.out.println("Saving file");
            FileChooser chooser = new FileChooser();
            File selectedFile = chooser.showOpenDialog(null);
            if (selectedFile == null) {
                // TODO Handle no file selected
                System.out.println("No file selected");
            } else {
                String loadedFileName = selectedFile.getName();

                try (FileWriter writer = new FileWriter(loadedFileName)) {
                    String spec = mainLayout.getSpec().toString();
                    writer.write(spec);

                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    //noinspection CallToPrintStackTrace
                    e1.printStackTrace();
                }
            }
        });
    }

    private void setHandleLoadInterfaceFromFile() {
        mainLayout.loadSpec.setOnAction(e -> {
            System.out.println("Loading file");
            FileChooser chooser = new FileChooser();
            File selectedFile = chooser.showOpenDialog(null);
            if (selectedFile == null) {
                // TODO Handle no file selected
                System.out.println("No file selected");
            } else {
                TextFileReader reader = new TextFileReader();
                List<String> lines = reader.read(selectedFile);

                mainLayout.clearInterfaceTextField();
                mainLayout.clearErrorsTextField();
                mainLayout.setInterfaceTextField(lines);

                //Also set the interface spec of the current selected component FIX: done in a bad way for now.
                String specText = mainLayout.getSpec().getText();
                System.out.println("handling load");
                System.out.println(specText);
                getCurrentComponent().setSpec(specText);
                //FIX: Need to instantiate new parser to not display old errors...
                parser = new InterfaceParser();
                cidlParser.ComponentContext componentContext = parser.parse(specText);
                System.out.println(componentContext);
                getCurrentComponent().setComponentContext(componentContext);
            }
        });
    }

    private void saveAndParseInterfaceSpec() {
        TextArea spec = mainLayout.getSpec();
        spec.setOnKeyTyped(e -> {
            String specText = spec.getText();
            //Set current text to the spec of component
            getCurrentComponent().setSpec(specText);
            //Parse the spec
            cidlParser.ComponentContext componentContext = parser.parse(specText);
            //Set current component context
            getCurrentComponent().setComponentContext(componentContext);


            //Get the parse errors
            List<String> parserErrors = parser.getErrors();
            //Clear the error view FIX does not seem to clear properly?
            mainLayout.clearErrorsTextField();
            //Set new parse errors
            mainLayout.setErrorsTextField(parserErrors);
        });
    }

    private Component getCurrentComponent() {
        return eventDispatcher.getCurrentComponent();
    }

}
