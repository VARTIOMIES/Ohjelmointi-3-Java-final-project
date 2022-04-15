package fi.tuni.prog3.sisu;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class StartStage extends Stage {

    Label nameLabel = new Label("Koko nimi:");
    TextField nameField = new TextField();

    Label studentNumberLabel = new Label("Opiskelijanumero:");
    TextField studentNumberField = new TextField();

    Label startingYearLabel = new Label("Opintojen aloitusvuosi:");
    TextField startingYearField = new TextField();

    // TODO: Scroll bar for degrees
    Label degreeLabel = new Label("Valitse tutkinto:");
    TextField degreeField = new TextField();


    Button nextButton = new Button("Jatka: ");

    StartStage(List<Degree> degrees){
        // Making the degree drop box.
        List<String> degreeNames = degrees.stream().map(Degree::getName).collect(Collectors.toList());
        ObservableList<String> degreeObsList = FXCollections.observableArrayList(degreeNames);
        final ComboBox<String> degreeComboBox = new ComboBox<>(degreeObsList);

        var grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new

                Insets(15,15,15,15));

        // node, columnIndex, rowIndex, columnSpan, rowSpan:
        grid.add(nameLabel,0,0);
        grid.add(nameField,1,0, 3, 1);
        grid.add(studentNumberLabel,0,1);
        grid.add(studentNumberField,1,1);
        grid.add(startingYearLabel,2,1);
        grid.add(startingYearField,3,1);
        grid.add(degreeLabel,0,2);
        grid.add(degreeComboBox,0,3, 4, 1);
        grid.add(nextButton,2,4);

        Scene scene = new Scene(grid, 590, 200);
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

        degreeComboBox.valueProperty().
                addListener((ObservableValue<? extends String> o, String oldValue, String newValue) ->

                {
                    // TODO: Make the degree box listener work.
                    if (degreeComboBox.getPromptText() == null) {
                        degreeComboBox.setStyle(null);
                        isValueOK.set(true);
                    } else {
                        degreeComboBox.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
                        isValueOK.set(false);
                    }
                });

        nextButton.setOnAction(e -> {
            if (isValueOK.get()) {
                String name = nameField.getText();
                String studentNumber = studentNumberField.getText();
                int startingYear = Integer.parseInt(startingYearField.getText());
                var degreeString = degreeComboBox.getPromptText();
                var degree = degrees.stream().filter(d -> degreeString.equals(d.getName()));

                // TODO: Adding the new student and checking if it is a new one.
                new MainStage();
                this.close();
            } else {
                nextButton.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
            }
        });
    }
}
