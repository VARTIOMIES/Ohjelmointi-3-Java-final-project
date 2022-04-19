package fi.tuni.prog3.sisu;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class LogInStage extends Stage {

    Label logInLabel = new Label("Kirjaudu sisään");

    Label studentNumberLabel = new Label("Opiskelijanumero:");
    TextField studentNumberField = new TextField();

    Button nextButton = new Button("Jatka");

    Button newStudentButton = new Button("Uusi oppilas");

    LogInStage(List<Degree> degrees, List<Student> students) {
        var grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15,15,15,15));

        // node, columnIndex, rowIndex, columnSpan, rowSpan:
        grid.add(logInLabel,0,0);
        grid.add(studentNumberLabel,0,1);
        grid.add(studentNumberField,1,1);
        grid.add(nextButton,1,3);
        grid.add(newStudentButton, 1, 4);

        Scene scene = new Scene(grid, 590, 200);
        this.setTitle("Kirjaudu sisään");
        this.setScene(scene);
        this.show();

        newStudentButton.setStyle("-fx-text-fill: #0000ff; -fx-background-color: white");
        AtomicBoolean isValueOK = new AtomicBoolean(false);
        studentNumberField.textProperty().
                addListener((ObservableValue<? extends String> o, String oldValue, String newValue) ->

                {
                    var oldNumberRegex = "^([H][0-9]{6})$"; // H9999999
                    var newNumberRegex = "^[0-9]{8}$"; // 99999999

                    if (studentNumberField.getText().matches(oldNumberRegex) || studentNumberField.getText().matches(newNumberRegex)) {
                        studentNumberField.setStyle(null);
                        isValueOK.set(true);
                    } else {
                        studentNumberField.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
                        isValueOK.set(false);
                    }
                });

        nextButton.setOnAction(e -> {
            String studentNumber = studentNumberField.getText();

            if(students.stream().noneMatch(s -> studentNumber.equals(s.getStudentNumber()))) {
                grid.add(new Label("Opiskelijanumeroa ei löydy."), 1, 2);
                studentNumberField.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
                isValueOK.set(false);
            }
            else if (!isValueOK.get()) {
                nextButton.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
            } else {
                Student student = students.stream()
                        .filter(s -> studentNumber.equals(s.getStudentNumber()))
                        .collect(Collectors.toList()).get(0);

                new MainStage(student.getDegree());
                this.close();
            }
        });

        newStudentButton.setOnAction(e -> {
            new NewStudentStage(degrees, students);
            this.close();
        });
    }
}
