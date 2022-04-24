package fi.tuni.prog3.sisu;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class MainStage {
    Label degreeLabel = new Label();
    TreeView<Object> degreeTree = new TreeView<>();

    MainStage(Stage stage, Degree defaultDegree) {
        // Preparing tabs.
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab tab1 = new Tab("Koti");
        Tab tab2 = new Tab("Kurssit");
        Tab tab3 = new Tab("Omat tiedot");

        tabPane.getTabs().add(tab1);
        tabPane.getTabs().add(tab2);
        tabPane.getTabs().add(tab3);

        degreeLabel.setText(defaultDegree.getName());
        TreeItem<String> rootNode = new TreeItem<String>(defaultDegree.getName());
        rootNode.setExpanded(true);

        // TODO: Make different grids for tabs. Also make treeview work.
        for (Module m : defaultDegree.getModules()) {
            TreeItem<String> empLeaf = new TreeItem<>(m.getModuleName());
            for (TreeItem<String> depNode : rootNode.getChildren()) {
                depNode.getChildren().add(empLeaf);
                break;
            }
        }

        var homeGrid = new GridPane();
        homeGrid.setId("gridPane3");
        homeGrid.setHgap(15);
        homeGrid.setVgap(15);
        homeGrid.setPadding(new Insets(15,15,15,15));

        // node, columnIndex, rowIndex, columnSpan, rowSpan:
        homeGrid.add(tabPane, 0, 0, 2, 1);
        homeGrid.add(degreeLabel,1,1);
        homeGrid.add(degreeTree, 0, 2);

        // Setting css id:s.
        homeGrid.getStyleClass().add("secBackground");
        tab1.getStyleClass().add("homeIcon");

        var scene = new Scene(homeGrid, 900, 650);
        stage.setScene(scene);
        final String style = getClass().getResource("stylesheet.css").toExternalForm();
        scene.getStylesheets().add(style);
        stage.setTitle("SISU");
        stage.show();
    }
}
