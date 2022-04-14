package fi.tuni.prog3.sisu;

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

import java.util.concurrent.atomic.AtomicBoolean;

public class StartStage extends Stage {

    Label nameLabel = new Label("Koko nimi:");
    TextField nameField = new TextField();

    Label studentNumberLabel = new Label("Opiskelijanumero:");
    TextField studentNumberField = new TextField();

    Label startingYearLabel = new Label("Opintojen aloitusvuosi:");
    TextField startingYearField = new TextField();

    // TODO: Scroll bar for degrees
    Label degreeLabel = new Label("Tutkinto:");
    TextField degreeField = new TextField();

    Button nextButton = new Button("Jatka: ");

    StartStage(){
        var grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new

                Insets(10,10,10,10));

        // node, columnIndex, rowIndex, columnSpan, rowSpan:
        grid.add(nameLabel,0,0);
        grid.add(nameField,1,0);
        grid.add(studentNumberLabel,0,1);
        grid.add(studentNumberField,1,1);
        grid.add(startingYearLabel,0,2);
        grid.add(startingYearField,1,2);
        grid.add(degreeLabel,0,3);
        grid.add(degreeField,1,3);
        grid.add(nextButton,0,4,2,1);

        Scene scene = new Scene(grid, 400, 200);
        this.setTitle("Starting window");
        this.setScene(scene);
        this.show();

        // Actions
        AtomicBoolean isValueOK = new AtomicBoolean(false);
        nameField.textProperty().
                addListener((ObservableValue<? extends String> o, String oldValue, String newValue) ->

                {
                    if (nameField.getText().matches("^[\\p{L} .'-]+$")) {
                        nameField.setStyle(null);
                        isValueOK.set(true);
                    } else {
                        nameField.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
                        isValueOK.set(false);
                    }
                });

        studentNumberField.textProperty().
                addListener((ObservableValue<? extends String> o, String oldValue, String newValue) ->

                {
                    var oldNumberRegex = "^([A-z][0-9]{6})$"; // X9999999
                    var newNumberRegex = "^[0-9]{8}$"; // 99999999

                    // TODO: Check if student is in the database
                    if (studentNumberField.getText().matches(oldNumberRegex) || studentNumberField.getText().matches(newNumberRegex)) {
                        studentNumberField.setStyle(null);
                        isValueOK.set(true);
                    } else {
                        studentNumberField.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
                        isValueOK.set(false);
                    }
                });

        startingYearField.textProperty().
                addListener((ObservableValue<? extends String> o, String oldValue, String newValue) ->

                {
                    if (startingYearField.getText().matches("^[0-9]{4}$")) {
                        startingYearField.setStyle(null);
                        isValueOK.set(true);
                    } else {
                        startingYearField.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
                        isValueOK.set(false);
                    }
                });

        nextButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle (ActionEvent e){
                if (isValueOK.get()) {
                    String name = nameField.getText();
                    String studentNumber = studentNumberField.getText();
                    int startingYear = Integer.parseInt(startingYearField.getText());

                    // TODO: Adding the new student and checking if it is a new one.
                    new MainStage();
                }

            }
        });
    }
}
