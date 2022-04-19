package fi.tuni.prog3.sisu;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MainStage extends Stage {
    Label degreeLabel = new Label();

    MainStage(Degree defaultDegree) {
        degreeLabel.setText(defaultDegree.getName());

        var grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15,15,15,15));

        // node, columnIndex, rowIndex, columnSpan, rowSpan:
        grid.add(degreeLabel,0,0);

        var scene = new Scene(grid, 640, 480);
        this.setScene(scene);
        this.show();
    }
}
