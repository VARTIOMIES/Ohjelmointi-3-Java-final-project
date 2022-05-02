/**
 * <p>
 * @author Ronja Lipsonen
 * @since 1.0
 */

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

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * A class that imitates scene. User can create new student account here.
 */

public class NewStudentGui {
    // Creating all the elements.
    private final Stage stage;
    private final List<Degree> degrees;
    private final List<Student> students;
    private final Label newStudentLabel;
    private final Label nameLabel;
    private final TextField nameField;
    private final Label studentNumberLabel;
    private final TextField studentNumberField;
    private final Label startingYearLabel;
    private final TextField startingYearField;
    private final Label degreeLabel;
    private final Pane gap;
    private final Button previousButton;
    private final Button nextButton;
    private final SearchableComboBox<String> degreeComboBox;
    private final GridPane grid;
    private final Scene scene;

    /**
     * Constructs the whole log in page.
     * Precondition: stage has been created
     * Precondition: degrees is not empty
     * Precondition: students is not empty
     * Post-condition: degrees don't change
     * @param stage the stage of the whole program.
     * @param degrees all the degrees that exist in Tampere's Sisu.
     * @param students all the students from json file.
     */

    NewStudentGui(Stage stage, List<Degree> degrees, List<Student> students){
        // Declaring all elements.
        this.stage = stage;
        this.students = students;
        this.degrees = degrees;
        newStudentLabel = new Label("Uusi oppilas");
        nameLabel = new Label("Koko nimi");
        nameField = new TextField();
        studentNumberLabel = new Label("Opiskelijanumero");
        studentNumberField = new TextField();
        startingYearLabel = new Label("Opintojen aloitusvuosi");
        startingYearField = new TextField();
        degreeLabel = new Label("Valitse tutkinto (voit vaihtaa tämän myöhemmin)");
        gap = new Pane();
        previousButton = new Button("Takaisin");
        nextButton = new Button("Jatka");
        grid = new GridPane();
        scene = new Scene(grid, 350, 420);

        // Making the degree drop box.
        List<String> degreeNames = degrees.stream().map(Degree::getName).collect(Collectors.toList());
        ObservableList<String> degreeObsList = FXCollections.observableArrayList(degreeNames);
        degreeComboBox = new SearchableComboBox<>(degreeObsList);

        // Grid prepping.
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(15,15,15,15));
        gap.minHeightProperty().set(20);

        // Setting the elements.
        // node, columnIndex, rowIndex, columnSpan, rowSpan:
        grid.add(newStudentLabel, 1, 0, 2, 1);
        grid.add(nameLabel, 0, 1);
        grid.add(nameField, 0, 2, 3, 1);
        grid.add(studentNumberLabel, 0, 3,2,1);
        grid.add(studentNumberField, 0, 4, 2, 1);
        grid.add(startingYearLabel, 2, 3);
        grid.add(startingYearField, 2, 4);
        grid.add(degreeLabel, 0, 5, 3, 1);
        grid.add(degreeComboBox, 0, 6, 3, 1);
        grid.add(gap, 0, 7);
        grid.add(nextButton,0,8, 3, 1);
        grid.add(previousButton, 0, 9, 2, 1);

        // Setting css id:s.
        newStudentLabel.getStyleClass().add("heading");
        grid.getStyleClass().add("firstBackground");
        nextButton.getStyleClass().add("nextButton");
        previousButton.getStyleClass().add("linkButton");

        this.setIds();

        // Stage prepping.
        this.stage.setTitle("Uusi oppilas");
        final String style = getClass().getResource("stylesheet.css").toExternalForm();
        scene.getStylesheets().add(style);
        this.stage.setScene(scene);
        this.stage.show();

        // Actions.
        AtomicBoolean isNameOK = new AtomicBoolean(false);
        AtomicBoolean isStudentOK = new AtomicBoolean(false);
        AtomicBoolean isYearOK = new AtomicBoolean(false);
        AtomicBoolean isDegreeOK = new AtomicBoolean(false);
        observeNameField(isNameOK);
        observeStudentNumber(isStudentOK);
        observeStartingYear(isYearOK);
        observeDegreeBox(isDegreeOK);
        onPreviousButtonClick();
        onNextButtonClick(isNameOK, isStudentOK, isYearOK, isDegreeOK);
    }

    private void setIds(){
        newStudentLabel.setId("newStudentLabel");
        nameLabel.setId("nameLabel");
        nameField.setId("nameField");
        studentNumberLabel.setId("studentNumberLabel");
        studentNumberField.setId("studentNumberField");
        startingYearLabel.setId("startingYearLabel");
        startingYearField.setId("startingYearField");
        degreeLabel.setId("degreeLabel");
        previousButton.setId("previousButton");
        nextButton.setId("nextButton");
        degreeComboBox.setId("degreeComboBox");
    }

    /**
     * Checks if name is ok.
     */

    private void observeNameField(AtomicBoolean isNameOK) {
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
    }

    /**
     * Checks if student number is ok.
     */

    private void observeStudentNumber(AtomicBoolean isStudentOK) {
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
    }

    /**
     * Checks if starting year is ok.
     */

    private void observeStartingYear(AtomicBoolean isYearOK) {
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
    }

    /**
     * Checks if degree is null.
     */

    private void observeDegreeBox(AtomicBoolean isDegreeOK) {
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
    }

    /**
     * Goes back to log in page.
     */

    private void onPreviousButtonClick() {
        previousButton.setOnAction(e -> {
            new LogInGui(stage, degrees, students);
        });
    }

    /**
     * Checks that all the parameters are ok and gives necessary warnings if they aren't. When everything is ok, the
     * main window can be launched.
     */

    private void onNextButtonClick(AtomicBoolean isNameOK, AtomicBoolean isStudentOK, AtomicBoolean isYearOK, AtomicBoolean isDegreeOK) {
        nextButton.setOnAction(e -> {
            String studentNumber = studentNumberField.getText();
            nextButton.setDisable(true);

            // Student number is already being used.
            if(students.stream().anyMatch(s ->
                    studentNumber.equals(s.getStudentNumber()))) {

                // This checks that if there is not already a label with this id
                // then creates new label and sets that id. !!This is made to
                // prevent creating a new label everytime the button is clicked.
                if (stage.getScene().lookup("#alreadyUsedInfoLabel")==null){
                    Label alreadyUsedInfoLabel =
                            new Label("Opiskelijanumeroa on jo käytössä.");
                    alreadyUsedInfoLabel.setId("alreadyUsedInfoLabel");
                    grid.add(alreadyUsedInfoLabel, 1, 2);
                }
                studentNumberField.setStyle("-fx-border-color: red ;" +
                        " -fx-border-width: 1px ;");
                isStudentOK.set(false);
            }
            // Every parameter is not ok.
            else if (!(isNameOK.get() && isStudentOK.get() && isYearOK.get() && isDegreeOK.get())) {
                nextButton.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");

                // Made to prevent creating UNLIMITED POWe.. labels
                if (stage.getScene().lookup("#notFilledAllInfoLabel")==null){
                    Label notFilledAllInfoLabel =
                            new Label("Täytäthän jokaisen kohdan!");
                    notFilledAllInfoLabel.setId("notFilledAllInfoLabel");
                    grid.add(notFilledAllInfoLabel,1, 7, 2, 1);
                }

                // Opens the main scene.
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

                        Student newStudent = new Student(name, studentNumber, startingYear, degree);
                        students.add(newStudent);

                        int howMany = 0;
                        for(Student s : students) {
                            if(s.getName().equals(name)) {
                                howMany += 1;
                            }
                        }
                        newStudent.setSameNamed(howMany);

                        try {
                            new MainGui(stage, newStudent, degrees, students);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
            nextButton.setDisable(false);
        });
    }
}
