package fi.tuni.prog3.sisu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.SearchableComboBox;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


public class MainStage {

    private Student student;
    private List<Degree> degrees;
    private TabPane tabPane;


    HashMap<String, List<String>> testTreeItems = new HashMap<>();
    List<String> testList = Arrays.asList("Kurssi1", "Kurssi2", "Kurssi3", "Kurssi4");


    MainStage(Stage stage, Student student, List<Degree> degrees) {
        // Initializing stuff
        this.student = student;
        this.degrees = degrees;

        // Creating container.
        VBox layout = new VBox(15);

        // Preparing tabs.
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Creating tabs and adding them to tabPane
        tabPane.getTabs().add(new HomeTab("Etusivu"));
        tabPane.getTabs().add(new DesignTab("Omat suoritukset"));
        tabPane.getTabs().add(new CourseTab("Kurssinäkymä"));
        tabPane.getTabs().add(new PersonalTab("Omat tiedot"));

        // TODO: Make treeView work.
        testTreeItems.put("Joku StudyModule1", testList);
        testTreeItems.put("Joku StudyModule2", testList);

        // Adding the tabpane to the
        layout.getChildren().add(tabPane);

        // Setting css id for tabPane.
        tabPane.getStyleClass().add("tabPane");

        // Setting scene and stage.
        var scene = new Scene(layout, 900, 650);
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

            grid.getStyleClass().add("secBackground");
            greetingLabel.getStyleClass().add("bigHeading");
            greetingLabel.setText(String.format("Tervetuloa Sisuun %s!", student.getFirstName()));

            grid.getStyleClass().add("secBackground");
            greetingLabel.getStyleClass().add("bigHeading");
            this.getStyleClass().add("homeIcon");


        }
    }
    private class DesignTab extends Tab{
        //Buttons and other elements

        // Constructor
        DesignTab(String label){
            super(label);
            GridPane grid = new GridPane();
            grid.setHgap(15);
            grid.setVgap(15);
            grid.setPadding(new Insets(15,15,15,15));

            this.setContent(grid);

        }

    }

    private class CourseTab extends Tab{
        //Buttons and other elements
        private final Button changeDegreeButton;
        private TreeView<String> treeView;
        private TreeItem<String> rootNode;
        private final SearchableComboBox<String> degreeComboBox;

        // Constructor
        CourseTab(String label){
            super(label);
            List<String> degreeNames = degrees.stream().map(Degree::getName).collect(Collectors.toList());
            ObservableList<String> degreeObsList = FXCollections.observableArrayList(degreeNames);
            degreeComboBox = new SearchableComboBox<>(degreeObsList);
            degreeComboBox.setPromptText("Voit vaihtaa tästä tutkinnon");

            GridPane grid = new GridPane();
            grid.setHgap(15);
            grid.setVgap(15);
            grid.setPadding(new Insets(15,15,15,15));

            this.setContent(grid);

            treeView = new TreeView<>();
            makeTreeView(student.getDegree());
            treeView.setShowRoot(true);

            changeDegreeButton = new Button("Vaihda");
            grid.add(degreeComboBox,0,0,3,1);
            grid.add(changeDegreeButton,4,0);
            grid.add(treeView,0,2,3,3);

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

        private void makeTreeView(Degree degree){
            rootNode = new TreeItem<>(degree.getName());
            for(var treeItem : testTreeItems.entrySet()) {
                TreeItem<String> webItem = new TreeItem<>(treeItem.getKey());
                for(var item : treeItem.getValue()) {
                    webItem.getChildren().add(new TreeItem<>(item));
                }
                rootNode.getChildren().add(webItem);
            }
            treeView.setRoot(rootNode);
        }



    }

    private class PersonalTab extends Tab{
        //Buttons and other elements
        private Label nameInfoLabel;
        private Label nameLabel;
        private Label studentNumberInfoLabel;
        private Label studentNumberLabel;
        private Label emailInfoLabel;
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


            grid.add(nameInfoLabel, 0, 0);
            grid.add(nameLabel, 0, 1);
            grid.add(studentNumberInfoLabel, 1, 0);
            grid.add(studentNumberLabel, 1, 1);
            grid.add(emailInfoLabel, 0, 2);
            grid.add(emailLabel, 0, 3);

            nameLabel.setText(student.getName());
            studentNumberLabel.setText(student.getStudentNumber());
            emailLabel.setText(student.getEmailAddress());

            this.setContent(grid);

            nameInfoLabel.getStyleClass().add("smallHeading");
            studentNumberInfoLabel.getStyleClass().add("smallHeading");
            emailInfoLabel.getStyleClass().add("smallHeading");


        }

    }
}
