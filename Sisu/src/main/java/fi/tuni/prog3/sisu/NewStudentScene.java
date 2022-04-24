package fi.tuni.prog3.sisu;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.controlsfx.control.SearchableComboBox;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class NewStudentScene {

    // Creating all the elements.
    Label newStudentLabel = new Label("Uusi oppilas");
    Label nameLabel = new Label("Koko nimi");
    TextField nameField = new TextField();
    Label studentNumberLabel = new Label("Opiskelijanumero");
    TextField studentNumberField = new TextField();
    Label startingYearLabel = new Label("Opintojen aloitusvuosi");
    TextField startingYearField = new TextField();
    Label degreeLabel = new Label("Valitse tutkinto (voit vaihtaa tämän myöhemmin)");
    Pane gap = new Pane();
    Button previousButton = new Button("Takaisin");
    Button nextButton = new Button("Jatka");

    NewStudentScene(Stage stage, List<Degree> degrees, List<Student> students){
        // Making the degree drop box.
        List<String> degreeNames = degrees.stream().map(Degree::getName).collect(Collectors.toList());
        ObservableList<String> degreeObsList = FXCollections.observableArrayList(degreeNames);
        final SearchableComboBox<String> degreeComboBox = new SearchableComboBox<>(degreeObsList);

        // Grid prepping.
        var grid = new GridPane();
        grid.setId("gridPane2");
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(15,15,15,15));
        gap.minHeightProperty().set(20);

        // Setting the elements.
        // node, columnIndex, rowIndex, columnSpan, rowSpan:
        grid.add(newStudentLabel, 0, 0, 2, 1);
        grid.add(nameLabel, 0, 1);
        grid.add(nameField, 0, 2, 3, 1);
        grid.add(studentNumberLabel, 0, 3);
        grid.add(studentNumberField, 0, 4, 2, 1);
        grid.add(startingYearLabel, 2, 3);
        grid.add(startingYearField, 2, 4);
        grid.add(degreeLabel, 0, 5, 3, 1);
        grid.add(degreeComboBox, 0, 6, 3, 1);
        grid.add(gap, 0, 7);
        grid.add(nextButton,0,8, 3, 1);
        grid.add(previousButton, 1, 9, 2, 1);

        // Setting css id:s.
        newStudentLabel.getStyleClass().add("heading");
        grid.getStyleClass().add("firstBackground");
        nextButton.getStyleClass().add("nextButton");

        Scene scene = new Scene(grid, 350, 400);
        stage.setTitle("Uusi oppilas");
        final String style = getClass().getResource("stylesheet.css").toExternalForm();
        scene.getStylesheets().add(style);
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

            // Student number is already being used.
            if(students.stream().anyMatch(s -> studentNumber.equals(s.getStudentNumber()))) {
                grid.add(new Label("Opiskelijanumeroa on jo käytössä."), 1, 2);
                studentNumberField.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
                isStudentOK.set(false);
            }
            // Every parameter is not ok.
            else if (!(isNameOK.get() && isStudentOK.get() && isYearOK.get() && isDegreeOK.get())) {
                nextButton.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
                grid.add(new Label("Täytäthän jokaisen kohdan!"), 1, 7, 2, 1);
            // Everything ok, opens the main scene.
            } else {
                if(!Objects.equals(startingYearField.getText(), "")) {
                    if(Integer.parseInt(startingYearField.getText()) < 1960 || Integer.parseInt(startingYearField.getText()) > 2022) {
                        startingYearField.setStyle("-fx-border-color: red ; -fx-border-width: 1px ; -fx-text-fill: RED");
                        isYearOK.set(false);
                        startingYearField.setText("Virheellinen vuosiluku!");
                    } else {
                        String name = nameField.getText();
                        int startingYear = Integer.parseInt(startingYearField.getText());
                        var degreeString = degreeComboBox.getValue();
                        var degree = degrees.stream()
                                .filter(d -> degreeString.equals(d.getName()))
                                .collect(Collectors.toList()).get(0);

                        students.add(new Student(name, studentNumber, startingYear, degree));

                        new MainStage(stage, degree);
                    }
                }
            }
        });
    }
}
