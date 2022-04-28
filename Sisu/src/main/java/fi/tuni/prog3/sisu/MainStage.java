package fi.tuni.prog3.sisu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.controlsfx.control.SearchableComboBox;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


public class MainStage {
    private Student student;
    private List<Degree> degrees;
    private TabPane tabPane;
    private MenuBar menuBar;
    private Label logOutLabel;
    private List<Course> courses;

    MainStage(Stage stage, Student student, List<Degree> degrees, List<Student> students) throws IOException {
        // Initializing stuff
        student.getDegree().readAPI();
        for(var module : student.getDegree().getModules()) {
            for(var studyModule : module.getStudyModules()) {
                courses = studyModule.getCourses();
            }
        }

        this.student = student;
        this.degrees = degrees;
        this.logOutLabel = new Label("Kirjaudu ulos");
        this.logOutLabel.setId("logOutLabel");
        logOutLabel.setOnMouseClicked(e -> {
            new LogInStage(stage, degrees, students);
        });

        // Creating containers.
        VBox layout = new VBox();
        layout.getStyleClass().add("hbox");
        var scene = new Scene(layout, 900, 650);

        // Preparing tabs.
        tabPane = new TabPane();
        menuBar = new MenuBar();
        Menu menu = new Menu("", logOutLabel);
        menuBar.getMenus().add(menu);

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        tabPane.setId("tabPane");

        // Creating tabs and adding them to tabPane
        tabPane.getTabs().add(new HomeTab("Etusivu"));
        tabPane.getTabs().add(new DesignTab("Omat suoritukset"));
        tabPane.getTabs().add(new CourseTab("Kurssinäkymä"));
        tabPane.getTabs().add(new PersonalTab("Omat tiedot"));


        Region spacer = new Region();
        spacer.getStyleClass().add("spacer");
        HBox menuBox = new HBox(spacer, menuBar);
        menuBox.getStyleClass().add("hbox");
        layout.getChildren().add(menuBox);
        layout.getChildren().add(tabPane);
        menuBox.setId("menuBox");

        // Setting scene and stage.
        stage.setScene(scene);
        final String style = getClass().getResource("stylesheet.css").toExternalForm();
        scene.getStylesheets().add(style);
        stage.setTitle("SISU");
        stage.show();

    }
    private class HomeTab extends Tab{
        //Buttons and other elements
        private final Label greetingLabel;
        private final Pane gap = new Pane();
        private final Label meanLabel = new Label("Opintojen keskiarvo");
        private Label meanNumberLabel;

        // Constructor
        HomeTab(String label) {
            super(label);
            GridPane grid = new GridPane();
            grid.setHgap(15);
            grid.setVgap(15);
            grid.setPadding(new Insets(15,15,15,15));
            gap.minHeightProperty().set(160);

            this.setContent(grid);
            this.setId("homeTab");

            greetingLabel = new Label(String.format("Tervetuloa Sisuun %s!", student.getFirstName()));
            grid.add(greetingLabel,0,0);
            grid.add(gap, 0, 3);
            grid.add(meanLabel, 0, 4);

            // Setting css id:s.
            grid.getStyleClass().add("grid-pane");
            grid.getStyleClass().add("secBackground");
            greetingLabel.getStyleClass().add("bigHeading");
            meanLabel.getStyleClass().add("heading");
            this.getStyleClass().add("homeIcon");
        }
    }
    private class DesignTab extends Tab{
        //Buttons and other elements
        private Course selectedCourse;
        private final Label infoLabel = new Label("Merkitse kursseja");
        private final SearchableComboBox<String> courseComboBox;
        private TreeView<String> treeView;
        private TreeItem<String> rootNode;
        private final Button chooseCourseButton = new Button("Valitse");

        // Constructor
        DesignTab(String label){
            super(label);

            List<String> courseNames = courses.stream().map(Course::getCourseName).collect(Collectors.toList());
            ObservableList<String> courseObsList = FXCollections.observableArrayList(courseNames);
            courseComboBox = new SearchableComboBox<>(courseObsList);
            courseComboBox.setPromptText("Hae kursseja");

            treeView = new TreeView<>();
            rootNode = new TreeItem<>("Suoritetut kurssit");

            for(var treeItem : student.getAttainments()) {
                TreeItem<String> moduleItem = new TreeItem<>(String.format("%d %s", treeItem.getGrade(), treeItem.getCourse().getCourseName()));
                rootNode.getChildren().add(moduleItem);
            }
            treeView.setRoot(rootNode);
            treeView.setShowRoot(true);

            GridPane grid = new GridPane();
            grid.setHgap(15);
            grid.setVgap(15);
            grid.setPadding(new Insets(15,15,15,15));

            grid.add(infoLabel, 0, 0);
            grid.add(courseComboBox, 0, 1, 3, 1);
            grid.add(chooseCourseButton, 4, 1);
            grid.add(treeView, 0, 2, 3, 1);

            this.setContent(grid);
            this.setId("designTab");

            // Setting css id:s.
            grid.getStyleClass().add("grid-pane");
            infoLabel.getStyleClass().add("bigHeading");
            chooseCourseButton.getStyleClass().add("basicButton");

            chooseCourseButton.setOnAction(e -> {
                if(courseComboBox.getValue() != null) {
                    var courseString = courseComboBox.getValue();
                    selectedCourse = courses.stream()
                            .filter(c -> courseString.equals(c.getCourseName()))
                            .collect(Collectors.toList()).get(0);
                }
            });
        }
    }

    private class CourseTab extends Tab{
        //Buttons and other elements
        private final Label infoLabel = new Label("Tutkintorakenne");
        private final Button changeDegreeButton = new Button("Vaihda");
        private TreeView<String> treeView;
        private TreeItem<String> rootNode;
        private final SearchableComboBox<String> degreeComboBox;
        private final Label courseInfoLabel = new Label();

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

        // Constructor
        CourseTab(String label){
            super(label);
            // Setting the degreeBox.
            List<String> degreeNames = degrees.stream().map(Degree::getName).collect(Collectors.toList());
            ObservableList<String> degreeObsList = FXCollections.observableArrayList(degreeNames);
            degreeComboBox = new SearchableComboBox<>(degreeObsList);
            degreeComboBox.setPromptText("Voit vaihtaa tästä tutkinnon");

            courseInfoLabel.setText("Kurssi-info tähän.");

            treeView = new TreeView<>();
            makeTreeView(student.getDegree());
            treeView.setShowRoot(true);

            GridPane grid = new GridPane();
            grid.setHgap(15);
            grid.setVgap(15);
            grid.setPadding(new Insets(15,15,15,15));
            this.setContent(grid);
            this.setId("courseTab");

            grid.add(infoLabel, 0, 0);
            grid.add(degreeComboBox,0,1,3,1);
            grid.add(courseInfoLabel, 4, 3, 2, 3);
            grid.add(changeDegreeButton,4,1);
            grid.add(treeView,0,3,3,3);

            // Setting css id:s.
            grid.getStyleClass().add("grid-pane");
            infoLabel.getStyleClass().add("bigHeading");
            courseInfoLabel.getStyleClass().add("infoLabel");

            // Actions.
            changeDegreeButton.getStyleClass().add("basicButton");
            changeDegreeButton.setOnAction(e -> {
                if(degreeComboBox.getValue() != null) {
                    var degreeString = degreeComboBox.getValue();
                    var degree = degrees.stream()
                            .filter(d -> degreeString.equals(d.getName()))
                            .collect(Collectors.toList()).get(0);
                    student.changeDegree(degree);

                    makeTreeView(degree);
                }
            });

            treeView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
                var course = treeView.getSelectionModel().getSelectedItem().getValue();
                if(courses.stream().anyMatch(c -> course.equals(c.getCourseName()))) {
                    grid.add(new Label(course), 4, 3);
                }
            });
        }
    }

    private class PersonalTab extends Tab{
        //Buttons and other elements
        private final Label infoLabel = new Label("Henkilötiedot");
        private final Label nameInfoLabel;
        private Label nameLabel;
        private final Label studentNumberInfoLabel;
        private Label studentNumberLabel;
        private final Label emailInfoLabel;
        private Label emailLabel;


        // Constructor
        PersonalTab(String label){
            super(label);
            GridPane grid = new GridPane();
            grid.setHgap(15);
            grid.setVgap(15);
            grid.setPadding(new Insets(15,15,15,15));

            nameInfoLabel = new Label("Koko nimi");
            nameInfoLabel.setId("nameInfoLabel");
            nameLabel = new Label();
            nameLabel.setId("nameLabel");
            studentNumberInfoLabel = new Label("Opiskelijanumero");
            studentNumberInfoLabel.setId("studentNumberInfoLabel");
            studentNumberLabel = new Label();
            studentNumberLabel.setId("studentNumberLabel");
            emailInfoLabel = new Label("Sähköpostiosoite");
            emailInfoLabel.setId("emailInfoLabel");
            emailLabel = new Label();
            emailLabel.setId("emailLabel");


            grid.add(infoLabel, 0, 0);
            grid.add(nameInfoLabel, 0, 1);
            grid.add(nameLabel, 0, 2);
            grid.add(studentNumberInfoLabel, 1, 1);
            grid.add(studentNumberLabel, 1, 2);
            grid.add(emailInfoLabel, 0, 3);
            grid.add(emailLabel, 0, 4);

            nameLabel.setText(student.getName());
            studentNumberLabel.setText(student.getStudentNumber());
            emailLabel.setText(student.getEmailAddress());

            this.setContent(grid);
            this.setId("personalTab");

            // Setting css id:s.
            grid.getStyleClass().add("grid-pane");
            infoLabel.getStyleClass().add("bigHeading");
            nameInfoLabel.getStyleClass().add("smallHeading");
            studentNumberInfoLabel.getStyleClass().add("smallHeading");
            emailInfoLabel.getStyleClass().add("smallHeading");
        }
    }
}
