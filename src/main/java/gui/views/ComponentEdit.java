package gui.views;

import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ComponentEdit extends VBox {
    public Button edit_name;
    public Button load_interface;
    public Button save_interface;
    public Button invoke_parser;

    public ComponentEdit() {
        final int pref_btn_width = 200;
        edit_name = new Button("Edit name");
        load_interface = new Button("Load interface");
        save_interface = new Button("Save interface");
        invoke_parser = new Button("Invoke parser");
        edit_name.setMinWidth(pref_btn_width);
        load_interface.setMinWidth(pref_btn_width);
        save_interface.setMinWidth(pref_btn_width);
        invoke_parser.setMinWidth(pref_btn_width);
        BackgroundFill background_fill = new BackgroundFill(Color.LIGHTBLUE, null, null);

        super.getChildren().addAll(edit_name, load_interface, save_interface, invoke_parser);
        super.setBackground(new Background(background_fill));
        super.setPrefWidth(pref_btn_width);
    }
}
