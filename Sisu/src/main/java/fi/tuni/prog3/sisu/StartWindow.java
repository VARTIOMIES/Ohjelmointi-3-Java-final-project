package fi.tuni.prog3.sisu;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
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
    public void start(Stage startStage) throws Exception {

        Label nameLabel = new Label("Koko nimi:");
        TextField nameField = new TextField();

        Label studentNumberLabel = new Label("Opiskelijanumero:");
        TextField studentNumberField = new TextField();

        Label startingYearLabel = new Label("Opintojen aloitusvuosi:");
        TextField startingYearField = new TextField();

        // tän vois tehä scrollaus valikkona sit jos osataan
        Label degreeLabel = new Label("Tutkinto:");
        TextField degreeField = new TextField();

        Button nextButton = new Button("Jatka: ");

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

        Scene scene = new Scene(grid, 400, 200);
        startStage.setTitle("Starting window");
        startStage.setScene(scene);

        nameField.textProperty().addListener((ObservableValue<? extends String> o, String oldValue, String newValue) ->
        {
            if (nameField.getText().matches("^[\\p{L} .'-]+$"))
            {
                nameField.setStyle(null);
            } else {
                nameField.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
            }
        });

        studentNumberField.textProperty().addListener((ObservableValue<? extends String> o, String oldValue, String newValue) ->
        {
            var oldNumberRegex = "^([A-z][0-9]{6})$"; // X9999999
            var newNumberRegex = "^[0-9]{8}$"; // 99999999

            // TODO: Check if student is in the database
            if (studentNumberField.getText().matches(oldNumberRegex) || studentNumberField.getText().matches(newNumberRegex))
            {
                studentNumberField.setStyle(null);
            } else {
                studentNumberField.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
            }
        });

        startingYearField.textProperty().addListener((ObservableValue<? extends String> o, String oldValue, String newValue) ->
        {
            if (startingYearField.getText().matches("^[0-9]{4}$"))
            {
                startingYearField.setStyle(null);
            } else {
                startingYearField.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
            }
        });

        nextButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                if(nameField.getStyle() == null && studentNumberField.getStyle() == null && startingYearField.getStyle() == null) {
                    String name = nameField.getText();
                    String studentNumber = studentNumberField.getText();
                    int startingYear = Integer.parseInt(startingYearField.getText());

                    // TODO: Adding the new student and checking if it is a new one.
                }
            }
        });

        startStage.show();
    }

    public static void main() {
        launch();
    }
}
