module edu.kit.tva.cif {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    //requires org.antlr.antlr4.runtime;
    requires antlr4.runtime;
    requires javafx.graphics;
    requires digraph.parser;
    requires org.json;
    requires java.desktop;
    requires codemodel;
    requires jsonschema2pojo.core;
    requires org.kordamp.ikonli.carbonicons;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;

    opens gui to javafx.fxml;
    exports gui;
    exports gui.views;
    opens gui.views to javafx.fxml;
    exports gui.controllers;
    opens gui.controllers to javafx.fxml;
    exports utils;
    exports gui.components;
    opens utils to javafx.fxml;
    opens gui.components to javafx.fxml;
    exports gui.components.util;
    opens gui.components.util to javafx.fxml;
}