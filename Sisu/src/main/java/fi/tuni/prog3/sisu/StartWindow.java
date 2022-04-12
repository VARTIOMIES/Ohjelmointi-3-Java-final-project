package fi.tuni.prog3.sisu;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;



public class StartWindow extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        Label nameLabel = new Label("Full name:");
        TextField nameField = new TextField();

        Label studentNumberLabel = new Label("Student id:");
        TextField studentNumberField = new TextField();

        Label startingYearLabel = new Label("Start year:");
        TextField startingYearField = new TextField();

        // tän vois tehä scrollaus valikkona sit jos osataan
        Label degreeLabel = new Label("Degree:");
        TextField degreeField = new TextField();

        Button nextButton = new Button("Next: ");

        var grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        // node, columnIndex, rowIndex, columnSpan, rowSpan:
        grid.add(nameLabel, 0,0);
        grid.add(nameField, 1,0);
        grid.add(studentNumberLabel, 0, 1);
        grid.add(studentNumberField, 1, 1);
        grid.add(startingYearLabel, 0, 2);
        grid.add(startingYearField, 1, 2);
        grid.add(degreeLabel, 0, 3);
        grid.add(degreeField, 1, 3);
        grid.add(nextButton, 0, 4, 2, 1);

        Scene scene = new Scene(grid, 300, 300);
        stage.setTitle("Starting window");
        stage.setScene(scene);

        //nextistä puuttuu toiminto

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
