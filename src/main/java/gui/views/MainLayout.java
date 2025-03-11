package gui.views;

import gui.Model;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONObject;
import utils.DiagramFileUtils;
import utils.JavaClassGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.function.Consumer;

public class MainLayout {
    @FXML
    private ComponentCanvas canvasController;

    @FXML
    private TextArea spec;

    @FXML
    public ComponentList componentList;

    @FXML
    private TextArea errors;

    @FXML
    public MenuItem loadSpec;

    @FXML
    public MenuItem saveSpec;

    @FXML
    private TabPane tabs;

    @FXML
    public MenuItem checkAssembly;
    @FXML
    public MenuItem checkComposite;

    @FXML
    private Button showModelButton;
    @FXML
    private Button showCodeEditorButton;

    @FXML
    private void showModel() {
        tabs.getSelectionModel().select(0);
        showModelButton.setStyle("-fx-background-color: #3d7dd0");
        showCodeEditorButton.setStyle("-fx-background-color: #FFFFFF");
    }
    @FXML
    private void showCodeEditor() {
        tabs.getSelectionModel().select(1);
        showCodeEditorButton.setStyle("-fx-background-color: #3d7dd0");
        showModelButton.setStyle("-fx-background-color: #FFFFFF");
    }

    public void setOnDiagramLoaded(Consumer<Model> onDiagramLoaded) {
        this.onDiagramLoaded = onDiagramLoaded;
    }

    public void setOnDiagramSaved(Consumer<File> onDiagramSaved) {
        this.onDiagramSaved = onDiagramSaved;
    }


    private Consumer<File> onDiagramSaved;
    private Consumer<Model> onDiagramLoaded;

    @FXML
    private void generateJava() {
        Stage stage = (Stage) canvasController.getPane().getScene().getWindow();
        DirectoryChooser fileChooser = new DirectoryChooser();
        File targetFile = fileChooser.showDialog(stage);
        JavaClassGenerator.generate(getCanvas().model, targetFile);
    }

    @FXML
    private void saveDiagram() {
        Stage stage = (Stage) canvasController.getPane().getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File targetFile = fileChooser.showSaveDialog(stage);
        if (targetFile != null) {
            try {
                FileWriter writer = new FileWriter(targetFile.getPath());
                writer.write(DiagramFileUtils.generateJson(canvasController.model.components, canvasController.model.assemblies).toString());
                writer.close();
                if (onDiagramSaved != null) {
                    onDiagramSaved.accept(targetFile);
                }
            } catch (IOException e) {
                if (onDiagramSaved != null) {
                    onDiagramSaved.accept(targetFile);
                }
                System.err.println(e.getMessage());
            }
        }
    }

    @FXML
    private void loadDiagram() throws IOException {
        Stage stage = (Stage) canvasController.getPane().getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File targetFile = fileChooser.showOpenDialog(stage);
        String json = new String(Files.readAllBytes(targetFile.toPath()));
        Model model = DiagramFileUtils.generateModel(new JSONObject(json));
        onDiagramLoaded.accept(model);
    }


    public MainLayout() {
    }

    public ComponentCanvas getCanvas() {
        return canvasController;
    }

    public void setInterfaceTextField(List<String> lines) {
        lines.forEach(l -> spec.appendText(l + "\n"));
        //pane.setRight(spec);
    }

    public void clearInterfaceTextField() {
        spec.clear();
    }

    public void setInterfaceTextField(String s) {
        spec.appendText(s);
        //pane.setRight(spec);
    }

    public void setErrorsTextField(List<String> lines) {
        errors.setStyle("-fx-text-fill: Red");
        lines.forEach(l -> errors.appendText(l + '\n'));
        errors.appendText("\n");
        //pane.setRight(spec);
    }

    public void clearErrorsTextField() {
        errors.clear();
    }

    public void setErrorsTextField(String s) {
        errors.appendText(s + "\n");
        //pane.setRight(spec);
    }

    public TextArea getSpec() {
        return spec;
    }

    public TextArea getErrorView() {
        return errors;
    }

    //public String getInterfaceTextField() {
    //   return spec.getText();
    //}

    //public void setSpec(String s) {
    //  spec.setText(s);
    // }
}
