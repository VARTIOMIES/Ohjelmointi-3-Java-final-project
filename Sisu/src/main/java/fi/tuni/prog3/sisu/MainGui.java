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
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.controlsfx.control.SearchableComboBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A class that imitates scene. User can add courses, observe their degree's structure
 * and personal information, change their degree and see their grades mean.
 */

public class MainGui {
    // Creating all the elements.
    private final Student student;
    private final List<Degree> degrees;
    private List<Student> students;
    private List<Course> courses;
    private final Label logOutLabel;
    private final VBox layout;
    private final Scene scene;
    private final TabPane tabPane;

    // Elements that multiple tabs use.
    private SearchableComboBox<String> courseComboBox;
    private GridPane designGrid;
    private Label meanNumberLabel;
    private ProgressBar studentDegreeProgressBar;
    private Label progress;


    /**
     * Constructs the main page and adds tabs to it.
     * Precondition: stage has been created
     * Precondition: student has been created
     * Precondition: degrees is not empty
     * Precondition: students is not empty
     * Post-condition: degrees don't change
     * @param stage the stage of the whole program.
     * @param degrees all the degrees that exist in Tampere's Sisu.
     * @param students all the students from json file.
     */

    MainGui(Stage stage, Student student, List<Degree> degrees, List<Student> students) throws IOException {
        // Declaring all elements.
        this.student = student;
        this.degrees = degrees;
        this.students = students;
        this.courses = new ArrayList<>();

        collectCourses(student);

        logOutLabel = new Label("Kirjaudu ulos");
        logOutLabel.setId("logOutLabel");
        layout = new VBox();
        scene = new Scene(layout, 900, 650);
        tabPane = new TabPane();

        // Element prepping.
        layout.getStyleClass().add("hbox");
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setId("tabPane");

        // Creating tabs and adding them to tabPane
        tabPane.getTabs().add(new HomeTab("Etusivu"));
        tabPane.getTabs().add(new DesignTab("Omat suoritukset"));
        tabPane.getTabs().add(new CourseTab("Kurssinäkymä"));
        tabPane.getTabs().add(new PersonalTab("Omat tiedot"));
        layout.getChildren().add(tabPane);

        // Stage prepping.
        stage.setScene(scene);
        final String style = getClass().getResource("stylesheet.css").toExternalForm();
        scene.getStylesheets().add(style);
        stage.setTitle("SISU");
        stage.show();

        // Actions.
        logOutLabel.setOnMouseClicked(e -> {
            new LogInGui(stage, degrees, students);
        });
    }

    /**
     * Collects the courses from API according to student's degree.
     * @param student the student who has logged in or created an account.
     */

    private void collectCourses(Student student) throws IOException {
        student.getDegree().readAPI();
        for(var module : student.getDegree().getModules()) {
            for(var studyModule : module.getStudyModules()) {
                courses = Stream.concat(courses.stream(), studyModule.getCourses().stream())
                        .collect(Collectors.toList());
            }
        }
    }

    /**
     * Creates an observable list from the course list. It removes courses that student has in their
     * attainments and duplicates and then sorts the courses.
     */

    private ObservableList<String> courseObsList() {
        List<String> attCourses = new ArrayList<>();
        for(var a : student.getAttainments()) {
            attCourses.add(a.getCourse().getCourseName());
        }

        List<String> courseNames = courses.stream().map(Course::getCourseName).collect(Collectors.toList());
        courseNames.removeIf(attCourses::contains);
        List<String> listWithoutDuplicates = Lists.newArrayList(Sets.newHashSet(courseNames));
        ObservableList<String> obsList = FXCollections.observableArrayList(listWithoutDuplicates);
        FXCollections.sort(obsList);
        return obsList;
    }

    /**
     * A tab class for the home page. This tab greets the user and tells them their grade mean.
     */

    private class HomeTab extends Tab{
        // Creating all the elements.
        private final Label greetingLabel;
        private final Pane gap;
        private final Label meanLabel;
        private final GridPane grid;
        private final Label studentProgressLabel;

        /**
         * Constructs the whole home tab.
         * @param label the tab label.
         */

        HomeTab(String label) {
            super(label);

            // Declaring all elements.
            meanNumberLabel = new Label(student.getMean());
            greetingLabel= new Label(String.format("Tervetuloa Sisuun %s!", student.getFirstName()));
            greetingLabel.setId("greetingLabel");
            gap = new Pane();
            meanLabel = new Label("Opintojen keskiarvo");
            grid = new GridPane();
            studentProgressLabel = new Label("Edistyminen");
            studentDegreeProgressBar = new ProgressBar(student.getDegreeProgress());
            progress = new Label(String.format("  %d/%d", student.getTotalCredits(), student.getDegree().getCreditsMin()));

            // Element prepping.
            grid.setHgap(15);
            grid.setVgap(15);
            grid.setPadding(new Insets(15,15,15,15));
            gap.minHeightProperty().set(160);
            this.setContent(grid);
            this.setId("homeTab");

            // Setting the elements.
            grid.add(greetingLabel,0,0);
            grid.add(gap, 0, 3);
            grid.add(meanLabel, 0, 4);
            grid.add(meanNumberLabel, 0, 5);
            grid.add(studentDegreeProgressBar,1,5);
            grid.add(studentProgressLabel,1,4);
            grid.add(progress, 1, 6);

            // Setting css id:s.
            grid.getStyleClass().add("grid-pane");
            grid.getStyleClass().add("secBackground");
            greetingLabel.getStyleClass().add("bigHeading");
            meanLabel.getStyleClass().add("heading");
            meanNumberLabel.getStyleClass().add("heading");
            studentProgressLabel.getStyleClass().add("heading");
            studentDegreeProgressBar.getStyleClass().add("heading");
            progress.getStyleClass().add("smallHeading");
            this.getStyleClass().add("homeIcon");
        }
    }

    /**
     * A tab class for the course design page. This tab allows the user to add courses with a grade (1-5)
     * that are in their degree. It also shows the student's attainments.
     */

    private class DesignTab extends Tab{
        // Creating all the elements.
        private Course selectedCourse;
        private final Label infoLabel;
        private TreeItem<String> rootNode;
        private final Button chooseCourseButton;
        private final Label addCourseLabel;
        private Label selectedCourseLabel;
        private final Label gradeLabel;
        private final TextField gradeField;
        private final Button addCourseButton;
        private final TreeView<String> treeView;
        private final VBox vbox;

        /**
         * Constructs the whole design tab.
         * @param label the tab label.
         */

        DesignTab(String label){
            super(label);

            // Declaring all elements.
            courseComboBox = new SearchableComboBox<>(courseObsList());
            infoLabel = new Label("Merkitse kursseja");
            chooseCourseButton = new Button("Valitse");
            addCourseLabel = new Label("Merkitse suoritus");
            selectedCourseLabel = new Label();
            gradeLabel = new Label("Merkitse arvosana (1-5)");
            gradeField = new TextField();
            addCourseButton = new Button("Lisää");
            treeView = new TreeView<>();
            vbox = new VBox(15);
            designGrid = new GridPane();

            // Element prepping.
            courseComboBox.setPromptText("Hae kursseja");
            gradeField.setMaxWidth(25);
            treeView.setMinWidth(300);
            treeView.setMaxWidth(350);
            vbox.setAlignment(Pos.BASELINE_CENTER);
            this.setContent(designGrid);
            this.setId("designTab");

            makeAttainmentTreeView();
            treeView.setShowRoot(true);

            // Inner box.
            vbox.getChildren().add(addCourseLabel);
            vbox.getChildren().add(selectedCourseLabel);
            vbox.getChildren().add(gradeLabel);
            vbox.getChildren().add(gradeField);
            vbox.getChildren().add(addCourseButton);

            // Outer grid.
            designGrid.setHgap(15);
            designGrid.setVgap(15);
            designGrid.setPadding(new Insets(15,15,15,15));

            // Setting the elements.
            designGrid.add(infoLabel, 0, 0);
            designGrid.add(courseComboBox, 0, 1, 3, 1);
            designGrid.add(chooseCourseButton, 4, 1);
            designGrid.add(treeView, 4, 2, 3, 3);

            // Setting css id:s.
            designGrid.getStyleClass().add("grid-pane");
            infoLabel.getStyleClass().add("bigHeading");
            addCourseLabel.getStyleClass().add("heading");
            selectedCourseLabel.getStyleClass().add("basicText");
            chooseCourseButton.getStyleClass().add("basicButton");
            addCourseButton.getStyleClass().add("basicButton");

            // Actions.
            AtomicBoolean isValueOK = new AtomicBoolean(false);
            chooseCourse();
            observeGrade(isValueOK);
            addCourse(isValueOK);
            }

        /**
         * Constructs the attainment tree view.
         */

        private void makeAttainmentTreeView() {
            rootNode = new TreeItem<>("Suoritetut kurssit\n (Arvosana - Nimi)");
            for(var treeItem : student.getAttainments()) {
                TreeItem<String> moduleItem = new TreeItem<>(String.format("%d - %s", treeItem.getGrade(), treeItem.getCourse().getCourseName()));
                rootNode.getChildren().add(moduleItem);
            }
            treeView.setRoot(rootNode);
        }

        /**
         * Sets selected course.
         */

        private void chooseCourse() {
            chooseCourseButton.setOnAction(e -> {
                // Making sure previous actions don't interfere here.
                gradeField.setText("");
                gradeField.setStyle(null);

                // Initializes selected course.
                if(courseComboBox.getValue() != null) {
                    var courseString = courseComboBox.getValue();
                    selectedCourse = courses.stream()
                            .filter(c -> courseString.equals(c.getCourseName()))
                            .collect(Collectors.toList()).get(0);
                    selectedCourseLabel.setText(selectedCourse.getCourseName());
                    designGrid.add(vbox, 0, 2, 3, 1);
                }
            });
        }

        /**
         * Sets boolean value according to the grade text field.
         */

        private void observeGrade(AtomicBoolean isValueOK) {
            gradeField.textProperty().
                    addListener((ObservableValue<? extends String> o, String oldValue, String newValue) ->
                    {
                        if (gradeField.getText().matches("^[1-5]$")) {
                            gradeField.setStyle(null);
                            isValueOK.set(true);
                        } else {
                            gradeField.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
                            isValueOK.set(false);
                        }
                    });
        }

        /**
         * Adds a new attainment to the student. Clears selections after.
         */

        private void addCourse(AtomicBoolean isValueOK) {
            addCourseButton.setOnAction(e -> {
                if(isValueOK.get()) {
                    courseComboBox.getSelectionModel().clearSelection();

                    // Adding the new attainment.
                    int grade = Integer.parseInt(gradeField.getText());
                    student.addAttainment(new Attainment(selectedCourse, grade));
                    treeView.setRoot(null);
                    makeAttainmentTreeView();

                    meanNumberLabel.setText(student.getMean());
                    studentDegreeProgressBar.setProgress(student.getDegreeProgress());
                    progress.setText(String.format("%d/%d", student.getTotalCredits(), student.getDegree().getCreditsMin()));

                    designGrid.getChildren().removeIf(n -> n instanceof VBox);
                    courseComboBox.getItems().remove(selectedCourse.getCourseName());
                }
            });
        }

    }

    /**
     * A tab class for the course page. The user can change their degree here and observe the
     * courses under that degree.
     */

    private class CourseTab extends Tab {
        // Buttons and other elements.
        private final Label infoLabel;
        private final Button changeDegreeButton;
        private final Label courseInfoLabel;
        private final Label courseNameInfoLabel;
        private final Label courseNameLabel;
        private final Label codeInfoLabel;
        private final Label codeLabel;
        private final Label creditsLabel;
        private final Label creditsInfoLabel;
        private final SearchableComboBox<String> degreeComboBox;
        private TreeItem<String> rootNode;
        private final TreeView<String> treeView;
        private final GridPane grid;
        private final VBox vbox;

        /**
         * Constructs the whole course tab.
         * @param label the tab label.
         */

        CourseTab(String label){
            super(label);

            // Preparing for degreeComboBox.
            List<String> degreeNames = degrees.stream().map(Degree::getName).collect(Collectors.toList());
            ObservableList<String> degreeObsList = FXCollections.observableArrayList(degreeNames);

            // Declaring all elements.
            infoLabel = new Label("Tutkintorakenne");
            changeDegreeButton = new Button("Vaihda");
            courseInfoLabel = new Label();
            courseNameInfoLabel = new Label();
            courseNameLabel = new Label();
            codeInfoLabel = new Label();
            codeLabel = new Label();
            creditsLabel = new Label();
            creditsInfoLabel = new Label();
            degreeComboBox = new SearchableComboBox<>(degreeObsList);
            treeView = new TreeView<>();
            grid = new GridPane();
            vbox = new VBox(15);

            // Element prepping.
            degreeComboBox.setPromptText("Voit vaihtaa tästä tutkinnon");
            this.setContent(grid);
            this.setId("courseTab");

            makeTreeView(student.getDegree());
            treeView.setShowRoot(true);

            // Inner vbox.
            vbox.getChildren().add(courseNameInfoLabel);
            vbox.getChildren().add(courseNameLabel);
            vbox.getChildren().add(codeInfoLabel);
            vbox.getChildren().add(codeLabel);
            vbox.getChildren().add(creditsInfoLabel);
            vbox.getChildren().add(creditsLabel);

            // Outer grid.
            grid.setHgap(15);
            grid.setVgap(15);
            grid.setPadding(new Insets(15,15,15,15));

            // Setting the elements.
            grid.add(infoLabel, 0, 0);
            grid.add(degreeComboBox,0,1,3,1);
            grid.add(changeDegreeButton,4,1);
            grid.add(treeView,0,3,3,3);
            grid.add(vbox, 4, 3, 2, 3);

            // Setting css id:s.
            grid.getStyleClass().add("grid-pane");
            infoLabel.getStyleClass().add("bigHeading");
            courseNameInfoLabel.getStyleClass().add("smallHeading");
            codeInfoLabel.getStyleClass().add("smallHeading");
            creditsInfoLabel.getStyleClass().add("smallHeading");

            // Actions.
            changeDegree();
            showCourseInfo();

        }

        /**
         * Constructs the degree and course tree view.
         */

        private void makeTreeView(Degree degree){
            rootNode = new TreeItem<>(degree.getName());
            for(var treeItem : student.getDegree().getModules()) {
                TreeItem<String> moduleItem = new TreeItem<>(treeItem.getModuleName());
                for(var studyModuleItem : treeItem.getStudyModules()) {
                    for(var courseItem : studyModuleItem.getCourses()) {
                        TreeItem<String> course = new TreeItem<>(courseItem.getCourseName());
                        moduleItem.getChildren().add(course);
                    }
                }
                rootNode.getChildren().add(moduleItem);
            }
            treeView.setRoot(rootNode);
        }

        /**
         * This updates courses up to the degree and initializes nodes that use courses or degree.
         */

        private void changeDegree() {
            changeDegreeButton.getStyleClass().add("basicButton");
            changeDegreeButton.setOnAction(e -> {
                changeDegreeButton.setDisable(true);

                if(degreeComboBox.getValue() != null) {
                    var degreeString = degreeComboBox.getValue();

                    // Making sure the degree isn't the same as the old one.
                    if(!Objects.equals(degreeString, student.getDegree().getName())) {
                        var degree = degrees.stream()
                                .filter(d -> degreeString.equals(d.getName()))
                                .collect(Collectors.toList()).get(0);
                        student.changeDegree(degree);
                        studentDegreeProgressBar.setProgress(student.getDegreeProgress());
                        progress.setText(String.format("  %d/%d", student.getTotalCredits(), student.getDegree().getCreditsMin()));

                        // Removing instances of the old degree's courses and adding in the new ones.
                        courses.clear();
                        try {
                            collectCourses(student);
                        }
                        catch(Exception ignored) {
                        }
                        designGrid.getChildren().removeIf(n -> n instanceof ComboBox);
                        courseComboBox = new SearchableComboBox<>(courseObsList());
                        designGrid.add(courseComboBox, 0, 1, 3, 1);
                        makeTreeView(degree);
                    }
                }
                changeDegreeButton.setDisable(false);
            });
        }

        /**
         * Shows selected courses information on the page.
         */

        private void showCourseInfo() {
            treeView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
                try {
                    var courseName = treeView.getSelectionModel().getSelectedItem().getValue();
                    var course = courses.stream()
                            .filter(c -> courseName.equals(c.getCourseName()))
                            .collect(Collectors.toList()).get(0);
                    // These only appear when a course is selected.
                    if(courses.stream().anyMatch(c -> courseName.equals(c.getCourseName()))) {
                        courseNameInfoLabel.setText("Kurssin nimi");
                        courseNameLabel.setText(courseName);
                        codeInfoLabel.setText("Kurssin koodi");
                        codeLabel.setText(course.getCourseCode());
                        creditsInfoLabel.setText("Laajuus");
                        creditsLabel.setText(String.format("%d op", course.getCredits()));
                    }
                } catch (Exception ignored) {
                }
            });
        }
    }

    /**
     * A tab class for the personal info page. The user can observe their personal information here.
     */

    private class PersonalTab extends Tab{
        // Creating all the elements.
        private final Label infoLabel;
        private final Label nameInfoLabel;
        private final Label nameLabel;
        private final Label studentNumberInfoLabel;
        private final Label studentNumberLabel;
        private final Label startYearInfoLabel;
        private final Label startYearLabel;
        private final Label endYearInfoLabel;
        private final Label endYearLabel;
        private final Label emailInfoLabel;
        private final Label emailLabel;
        private final Pane gap;
        private final GridPane grid;

        /**
         * Constructs the whole personal info tab.
         * @param label the tab label.
         */

        PersonalTab(String label){
            super(label);

            // Declaring all elements.
            infoLabel = new Label("Henkilötiedot");
            nameInfoLabel = new Label("Koko nimi");
            nameLabel = new Label(student.getName());
            studentNumberInfoLabel = new Label("Opiskelijanumero");
            studentNumberLabel = new Label(student.getStudentNumber());
            startYearInfoLabel = new Label("Aloitusvuosi");
            startYearLabel = new Label(String.valueOf(student.getStartingYear()));
            endYearInfoLabel = new Label("Odotettu päättymisvuosi");
            endYearLabel = new Label(String.valueOf(student.getExpectedEndYear()));
            emailInfoLabel = new Label("Sähköpostiosoite");
            emailLabel = new Label(student.getEmailAddress());
            gap = new Pane();
            grid = new GridPane();

            // Element prepping.
            gap.minHeightProperty().set(40);
            grid.setHgap(15);
            grid.setVgap(15);
            grid.setPadding(new Insets(15,15,15,15));
            this.setContent(grid);
            this.setId("personalTab");

            // Setting the elements.
            grid.add(infoLabel, 0, 0);
            grid.add(nameInfoLabel, 0, 1);
            grid.add(nameLabel, 0, 2);
            grid.add(studentNumberInfoLabel, 1, 1);
            grid.add(studentNumberLabel, 1, 2);
            grid.add(startYearInfoLabel, 0, 3);
            grid.add(startYearLabel, 0, 4);
            grid.add(endYearInfoLabel, 1, 3);
            grid.add(endYearLabel, 1, 4);
            grid.add(emailInfoLabel, 0, 5);
            grid.add(emailLabel, 0, 6);
            grid.add(gap, 0, 7);
            grid.add(logOutLabel, 0, 8);

            // Setting css id:s.
            grid.getStyleClass().add("grid-pane");
            infoLabel.getStyleClass().add("bigHeading");
            nameInfoLabel.getStyleClass().add("smallHeading");
            studentNumberInfoLabel.getStyleClass().add("smallHeading");
            startYearInfoLabel.getStyleClass().add("smallHeading");
            endYearInfoLabel.getStyleClass().add("smallHeading");
            emailInfoLabel.getStyleClass().add("smallHeading");
            logOutLabel.getStyleClass().add("basicButton");

            // Set ids for every important item.
            setIds();
        }

        private void setIds() {
            infoLabel.setId("infoLabel");
            nameInfoLabel.setId("nameInfoLabel");
            nameLabel.setId("nameLabel");
            studentNumberInfoLabel.setId("studentNumberInfoLabel");
            studentNumberLabel.setId("studentNumberLabel");
            emailInfoLabel.setId("emailInfoLabel");
            emailLabel.setId("emailLabel");

        }
    }
}
