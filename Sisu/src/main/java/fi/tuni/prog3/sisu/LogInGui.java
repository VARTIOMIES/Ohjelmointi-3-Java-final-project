/**
 * <p>
 * @author Ronja Lipsonen
 * @since 1.0
 */


package fi.tuni.prog3.sisu;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * A class that imitates scene. User can create new account or log in to main stage here.
 */


public class LogInGui {

    // Creating all the elements.
    private final Label logInLabel = new Label("Kirjaudu sisään");
    private final Label studentNumberLabel = new Label("Opiskelijanumero");
    private TextField studentNumberField = new TextField();
    private Pane smallGap = new Pane();
    private Pane bigGap = new Pane();
    private final Button nextButton = new Button("Jatka");
    private final Button newStudentButton = new Button("Uusi oppilas");

    /**
     * Constructs the whole log in page.
     * @param stage the stage of the whole program.
     * @param degrees all the degrees that exist in Tampere's Sisu.
     * @param students all the students from json file.
     */

    LogInGui(Stage stage, List<Degree> degrees, List<Student> students) {

        // Element prepping.
        stage.setResizable(false);
        VBox vbox = new VBox(15);
        vbox.setAlignment(Pos.BASELINE_CENTER);

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setAlignment(Pos.CENTER);
        smallGap.minHeightProperty().set(5);
        bigGap.minHeightProperty().set(30);
        studentNumberField.setMaxWidth(100);

        // Setting the elements.
        vbox.getChildren().add(logInLabel);
        vbox.getChildren().add(smallGap);
        vbox.getChildren().add(studentNumberLabel);
        vbox.getChildren().add(grid);
        grid.add(studentNumberField, 0, 0, 2, 1);
        vbox.getChildren().add(bigGap);
        vbox.getChildren().add(nextButton);
        vbox.getChildren().add(newStudentButton);

        // Setting css id:s.
        studentNumberLabel.getStyleClass().add("basicText");
        logInLabel.getStyleClass().add("heading");
        vbox.getStyleClass().add("firstBackground");
        newStudentButton.getStyleClass().add("linkButton");
        nextButton.getStyleClass().add("nextButton");

        // Set ids for every important item
        setIds();

        // Stage prepping.
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

        // Checks if student number is alright and initializes either main scene or new student scene.
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

                try {
                    new MainStage(stage, student, degrees, students);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
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
