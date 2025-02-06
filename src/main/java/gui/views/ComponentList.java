package gui.views;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ComponentList extends HBox {

    public Button atomic;
    public Button composite;

    public ComponentList() {
        final int pref_btn_width = 200;
        atomic = new Button("Atomic Component");
        //atomic.setStyle("-fx-text-color: black;");
        composite = new Button("Composite Component");
        //composite.setStyle("-fx-text-color: black;");
        atomic.setMinWidth(pref_btn_width);
        composite.setMinWidth(pref_btn_width);
        super.getChildren().addAll(atomic, composite);
    }
}
