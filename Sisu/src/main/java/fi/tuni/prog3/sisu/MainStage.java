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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


public class MainStage {
    private Student student;
    private List<Degree> degrees;
    private TabPane tabPane;
    private MenuBar menuBar;
    private Label logOutLabel;
    private List<Course> courses;

    HashMap<String, List<String>> testTreeItems = new HashMap<>();
    List<String> testList = Arrays.asList("Kurssi1", "Kurssi2", "Kurssi3", "Kurssi4");


    MainStage(Stage stage, Student student, List<Degree> degrees, List<Student> students) throws IOException {
        // Initializing stuff
        courses = student.getDegree().getCourses();
        this.student = student;
        this.degrees = degrees;
        this.logOutLabel = new Label("Kirjaudu ulos");
        logOutLabel.setOnMouseClicked(e -> {
            new LogInStage(stage, degrees, students);
        });

        // TODO: Make treeView work.
        testTreeItems.put("Joku StudyModule1", testList);
        testTreeItems.put("Joku StudyModule2", testList);

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

        // Constructor
        HomeTab(String label) {
            super(label);
            GridPane grid = new GridPane();
            grid.setHgap(15);
            grid.setVgap(15);
            grid.setPadding(new Insets(15,15,15,15));

            this.setContent(grid);

            greetingLabel = new Label();
            grid.add(greetingLabel,0,0);

            greetingLabel.setText(String.format("Tervetuloa Sisuun %s!", student.getFirstName()));

            // Setting css id:s.
            grid.getStyleClass().add("grid-pane");
            grid.getStyleClass().add("secBackground");
            greetingLabel.getStyleClass().add("bigHeading");
            this.getStyleClass().add("homeIcon");
        }
    }
    private class DesignTab extends Tab{
        //Buttons and other elements
        private final Label infoLabel = new Label("Hae kursseja");
        private final SearchableComboBox<String> courseComboBox;
        private TreeView<String> treeView;
        private TreeItem<String> rootNode;

        // Constructor
        DesignTab(String label){
            super(label);

            // TODO: Course list here.
            courseComboBox = new SearchableComboBox<>();

            // TODO: TreeView change.
            rootNode = new TreeItem<>("Suoritetut kurssit");
            treeView = new TreeView<>();
            for(var treeItem : courses) {
                TreeItem<String> webItem = new TreeItem<>(treeItem.getCourseName());
                rootNode.getChildren().add(webItem);
            }
            treeView.setRoot(rootNode);
            treeView.setShowRoot(true);

            GridPane grid = new GridPane();
            grid.setHgap(15);
            grid.setVgap(15);
            grid.setPadding(new Insets(15,15,15,15));

            grid.add(infoLabel, 0, 0);
            grid.add(courseComboBox, 0, 1);
            grid.add(treeView, 0, 2, 2, 1);

            this.setContent(grid);

            // Setting css id:s.
            grid.getStyleClass().add("grid-pane");
            infoLabel.getStyleClass().add("bigHeading");
        }
    }

    private class CourseTab extends Tab{
        //Buttons and other elements
        private final Label infoLabel = new Label("Tutkintorakenne");
        private final Button changeDegreeButton;
        private TreeView<String> treeView;
        private TreeItem<String> rootNode;
        private final SearchableComboBox<String> degreeComboBox;
        private final Label courseInfoLabel = new Label();

        private void makeTreeView(Degree degree){
            rootNode = new TreeItem<>(degree.getName());
            for(var treeItem : courses) {
                TreeItem<String> webItem = new TreeItem<>(treeItem.getCourseName());
                rootNode.getChildren().add(webItem);
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

            // TODO: TreeView change.
            treeView = new TreeView<>();
            makeTreeView(student.getDegree());
            treeView.setShowRoot(true);

            GridPane grid = new GridPane();
            grid.setHgap(15);
            grid.setVgap(15);
            grid.setPadding(new Insets(15,15,15,15));
            this.setContent(grid);

            changeDegreeButton = new Button("Vaihda");
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
            nameLabel = new Label();
            studentNumberInfoLabel = new Label("Opiskelijanumero");
            studentNumberLabel = new Label();
            emailInfoLabel = new Label("Sähköpostiosoite");
            emailLabel = new Label();


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

            // Setting css id:s.
            grid.getStyleClass().add("grid-pane");
            infoLabel.getStyleClass().add("bigHeading");
            nameInfoLabel.getStyleClass().add("smallHeading");
            studentNumberInfoLabel.getStyleClass().add("smallHeading");
            emailInfoLabel.getStyleClass().add("smallHeading");
        }
    }
}
