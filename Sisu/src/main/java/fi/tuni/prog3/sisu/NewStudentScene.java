package fi.tuni.prog3.sisu;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.controlsfx.control.SearchableComboBox;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class NewStudentScene {

    // Creating all the elements.
    Label nameLabel = new Label("Koko nimi:");
    TextField nameField = new TextField();

    Label studentNumberLabel = new Label("Opiskelijanumero:");
    TextField studentNumberField = new TextField();

    Label startingYearLabel = new Label("Opintojen aloitusvuosi:");
    TextField startingYearField = new TextField();

    Label degreeLabel = new Label("Valitse tutkinto: (Voit vaihtaa tämän myöhemmin)");

    Button previousButton = new Button("Takaisin");

    Button nextButton = new Button("Jatka");

    NewStudentScene(Stage stage, List<Degree> degrees, List<Student> students){
        // Making the degree drop box.
        List<String> degreeNames = degrees.stream().map(Degree::getName).collect(Collectors.toList());
        ObservableList<String> degreeObsList = FXCollections.observableArrayList(degreeNames);
        final SearchableComboBox<String> degreeComboBox = new SearchableComboBox<>(degreeObsList);


        var grid = new GridPane();
        grid.setId("gridPane2");
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15,15,15,15));

        // Setting the elements.
        // node, columnIndex, rowIndex, columnSpan, rowSpan:
        grid.add(nameLabel,0,0);
        grid.add(nameField,1,0, 3, 1);
        grid.add(studentNumberLabel,0,1);
        grid.add(studentNumberField,1,1);
        grid.add(startingYearLabel,2,1);
        grid.add(startingYearField,3,1);
        grid.add(degreeLabel,0,2, 2, 1);
        grid.add(degreeComboBox,0,3, 4, 1);
        grid.add(previousButton, 1, 4);
        grid.add(nextButton,2,4);

        Scene scene = new Scene(grid, 590, 200);
        stage.setTitle("Uusi oppilas");
        stage.setScene(scene);
        stage.show();

        // Actions for text fields and buttons.
        AtomicBoolean isNameOK = new AtomicBoolean(false);
        AtomicBoolean isStudentOK = new AtomicBoolean(false);
        AtomicBoolean isYearOK = new AtomicBoolean(false);
        AtomicBoolean isDegreeOK = new AtomicBoolean(false);
        nameField.textProperty().
                addListener((ObservableValue<? extends String> o, String oldValue, String newValue) ->

                {
                    if (nameField.getText().matches("^[\\p{L} .'-]+$")) {
                        nameField.setStyle(null);
                        isNameOK.set(true);
                    } else {
                        nameField.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
                        isNameOK.set(false);
                    }
                });

        studentNumberField.textProperty().
                addListener((ObservableValue<? extends String> o, String oldValue, String newValue) ->

                {
                    var oldNumberRegex = "^([H][0-9]{6})$"; // H9999999
                    var newNumberRegex = "^[0-9]{8}$"; // 99999999

                    if (studentNumberField.getText().matches(oldNumberRegex) || studentNumberField.getText().matches(newNumberRegex)) {
                        studentNumberField.setStyle(null);
                        isStudentOK.set(true);
                    } else {
                        studentNumberField.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
                        isStudentOK.set(false);
                    }
                });

        startingYearField.textProperty().
                addListener((ObservableValue<? extends String> o, String oldValue, String newValue) ->

                {
                    if (startingYearField.getText().matches("^[0-9]{4}$")) {
                        startingYearField.setStyle(null);
                        isYearOK.set(true);
                    } else {
                        startingYearField.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
                        isYearOK.set(false);
                    }
                });

        degreeComboBox.valueProperty().
                addListener((ObservableValue<? extends String> o, String oldValue, String newValue) ->

                {
                    if (degreeComboBox.getPromptText() == null) {
                        degreeComboBox.setStyle(null);
                        isDegreeOK.set(true);
                    } else {
                        degreeComboBox.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
                        isDegreeOK.set(false);
                    }
                });

        previousButton.setOnAction(e -> {
                new LogInStage(stage, degrees, students);
        });

        nextButton.setOnAction(e -> {
            String studentNumber = studentNumberField.getText();

            if(students.stream().anyMatch(s -> studentNumber.equals(s.getStudentNumber()))) {
                grid.add(new Label("Opiskelijanumeroa on jo käytössä."), 1, 2);
                studentNumberField.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
                isStudentOK.set(false);
            }
            else if (!(isNameOK.get() && isStudentOK.get() && isYearOK.get() && isDegreeOK.get())) {
                nextButton.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
            } else {
                String name = nameField.getText();
                int startingYear = Integer.parseInt(startingYearField.getText());
                var degreeString = degreeComboBox.getValue();
                var degree = degrees.stream()
                        .filter(d -> degreeString.equals(d.getName()))
                        .collect(Collectors.toList()).get(0);

                students.add(new Student(name, studentNumber, startingYear, degree));

                try {
                    new MainStage(stage, degree);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
