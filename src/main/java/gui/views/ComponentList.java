package gui.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.kordamp.ikonli.javafx.FontIcon;

public class ComponentList extends HBox {

    public Button atomic;
    public Button composite;

    public ComponentList() {
        final String style = "-fx-border-width: 1; -fx-background-radius: 12; -fx-border-radius: 12; -fx-start-margin: 12; -fx-border-color: #a19c9c; -fx-font-size: 14; -fx-background-color: #ffffff; -fx-text-fill: #434040;";
        final int pref_btn_width = 100;
        super.setSpacing(12);
        super.setAlignment(Pos.CENTER);
        super.setPadding(new Insets(10, 10, 10, 10));
        super.setStyle("-fx-background-color: #FFFFFF; -fx-fill-width: true;");
        atomic = new Button("Atomic");
        atomic.setGraphic(new FontIcon(CarbonIcons.CENTER_SQUARE));
        atomic.setStyle(style);
        composite = new Button("Composite");
        composite.setGraphic(new FontIcon(CarbonIcons.CONTAINER_SOFTWARE));
        composite.setStyle(style);

        atomic.setMinWidth(pref_btn_width);
        composite.setMinWidth(pref_btn_width);
        super.getChildren().addAll(atomic, composite);
    }
}
