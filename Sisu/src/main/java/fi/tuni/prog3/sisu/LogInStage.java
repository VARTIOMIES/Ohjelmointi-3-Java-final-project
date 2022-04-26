package fi.tuni.prog3.sisu;

import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class LogInStage {

    // Creating all the elements.
    Label logInLabel = new Label("Kirjaudu sisään");
    Label studentNumberLabel = new Label("Opiskelijanumero");
    TextField studentNumberField = new TextField();
    Pane smallGap = new Pane();
    Pane bigGap = new Pane();
    Button nextButton = new Button("Jatka");
    Button newStudentButton = new Button("Uusi oppilas");

    LogInStage(Stage stage, List<Degree> degrees, List<Student> students) {

        // Grid prepping.
        stage.setResizable(false);
        VBox vbox = new VBox(15);
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setAlignment(Pos.CENTER);
        vbox.setAlignment(Pos.BASELINE_CENTER);
        smallGap.minHeightProperty().set(5);
        bigGap.minHeightProperty().set(30);

        // TODO: Center.
        // Setting the elements.
        vbox.getChildren().add(logInLabel);
        vbox.getChildren().add(smallGap);
        vbox.getChildren().add(studentNumberLabel);
        vbox.getChildren().add(grid);
        grid.add(studentNumberField, 0, 0, 2, 1);
        studentNumberField.setPrefWidth(200);
        vbox.getChildren().add(bigGap);
        vbox.getChildren().add(nextButton);
        vbox.getChildren().add(newStudentButton);

        // Setting css id:s.
        logInLabel.getStyleClass().add("heading");
        studentNumberLabel.getStyleClass().add("basicText");
        vbox.getStyleClass().add("firstBackground");
        newStudentButton.getStyleClass().add("linkButton");
        nextButton.getStyleClass().add("nextButton");

        // Set ids for every important item
        setIds();



        Scene scene = new Scene(vbox, 350, 400);
        stage.setTitle("Kirjaudu sisään");
        final String style = getClass().getResource("stylesheet.css").toExternalForm();
        scene.getStylesheets().add(style);
        stage.setScene(scene);
        stage.show();

        // Actions for text fields and buttons.

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
                studentNumberField.setText("Opiskelijanumeroa ei löydy.");
                studentNumberField.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
                isValueOK.set(false);
            }
            else if (!isValueOK.get()) {
                nextButton.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
            } else {
                Student student = students.stream()
                        .filter(s -> studentNumber.equals(s.getStudentNumber()))
                        .collect(Collectors.toList()).get(0);

                new MainStage(stage, student, degrees, students);
            }
        });

        newStudentButton.setOnAction(e -> {
             new NewStudentScene(stage, degrees, students);
        });
    }

    private void setIds(){
        logInLabel.setId("logInLabel");
        studentNumberLabel.setId("studentNumberLabel");
        studentNumberField.setId("studentNumberField");
        nextButton.setId("nextButton");
        newStudentButton.setId("newStudentButton");
    }
}
