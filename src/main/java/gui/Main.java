package gui;

import gui.controllers.ViewController;
import javafx.scene.paint.Color;
import lattice.LatticeBuilder;

import java.io.File;
import java.io.FileInputStream;

import gui.views.MainLayout;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("Working Directory = " + System.getProperty("user.dir"));
            var model = new Model();
            var file = new File("Examples/lattice.dot");
            var lattice = LatticeBuilder.buildLatticeFromDot(new FileInputStream(file));
            model.setLattice(lattice);
            System.out.println(lattice);
            var mainLayoutLoader = new FXMLLoader(getClass().getResource("/gui/MainLayout.fxml"));
            BorderPane mainLayout = mainLayoutLoader.load();
            MainLayout layoutController = mainLayoutLoader.getController();
            var canvas = layoutController.getCanvas();
            canvas.setModel(model);
            Scene scene = new Scene(mainLayout, 800, 500);
            primaryStage.setScene(scene);
            primaryStage.setTitle("ArchFlow UI");
            primaryStage.show();
            var viewController = new ViewController(model, layoutController, canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }


}
