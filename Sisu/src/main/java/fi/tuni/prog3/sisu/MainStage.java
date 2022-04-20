package fi.tuni.prog3.sisu;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainStage {
    Label degreeLabel = new Label();

    MainStage(Stage stage, Degree defaultDegree) throws FileNotFoundException {
        degreeLabel.setText(defaultDegree.getName());

        // Setting images.
        FileInputStream inputStream = new FileInputStream("src/main/resources/fi/tuni/prog3/sisu/pictures/mainPic.png");
        Image image = new Image(inputStream);
        ImageView imageView = new ImageView(image);
        imageView.setX(50);
        imageView.setY(25);
        imageView.setFitWidth(900);
        imageView.setPreserveRatio(true);


        var grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        // grid.setPadding(new Insets(15,15,15,15));

        // node, columnIndex, rowIndex, columnSpan, rowSpan:
        grid.add(degreeLabel,0,0);
        grid.add(imageView, 0, 1);

        var scene = new Scene(grid, 900, 650);
        stage.setScene(scene);
        stage.setTitle("SISU");
        stage.show();
    }
}
