package fi.tuni.prog3.sisu;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class MainStage {
    Label degreeLabel = new Label();
    TreeView<Object> degreeTree = new TreeView<>();

    MainStage(Stage stage, Degree defaultDegree) {
        degreeLabel.setText(defaultDegree.getName());
        TreeItem<String> rootNode = new TreeItem<String>(defaultDegree.getName());
        rootNode.setExpanded(true);

//        for (Module m : defaultDegree.getModules()) {
//            TreeItem<String> empLeaf = new TreeItem<>(m.getModuleName());
//            for (TreeItem<String> depNode : rootNode.getChildren()) {
//                depNode.getChildren().add(empLeaf);
//                break;
//            }
//        }

        var grid = new GridPane();
        grid.setId("gridPane3");
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(15,15,15,15));

        // node, columnIndex, rowIndex, columnSpan, rowSpan:
        grid.add(degreeLabel,0,0);
        grid.add(degreeTree, 0, 1);

        // Setting css id:s.
        grid.getStyleClass().add("secBackground");

        var scene = new Scene(grid, 900, 650);
        stage.setScene(scene);
        final String style = getClass().getResource("stylesheet.css").toExternalForm();
        scene.getStylesheets().add(style);
        stage.setTitle("SISU");
        stage.show();
    }
}
