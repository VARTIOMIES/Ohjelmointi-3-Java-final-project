package fi.tuni.prog3.sisu;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainStage extends Stage {
    Label label = new Label("SISU");

    MainStage() {
        var scene = new Scene(new StackPane(label), 640, 480);
        this.setScene(scene);
        this.show();
    }
}
