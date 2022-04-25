package fi.tuni.prog3.sisu;

import javafx.beans.value.ObservableValue;
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
    private Degree degree;

    //Home page.
    Label greetingLabel = new Label();
    Label degreeLabel = new Label();

    // Course page.

    // Personal page.
    Label nameInfoLabel = new Label("Koko nimi");
    Label nameLabel = new Label();
    Label studentNumberInfoLabel = new Label("Opiskelijanumero");
    Label studentNumberLabel = new Label();
    Label emailInfoLabel = new Label("Sähköpostiosoite");
    Label emailLabel = new Label();

    MainStage(Stage stage, Student student, List<Degree> degrees) {
        degree = student.getDegree();
        // Making the degree drop box.
        List<String> degreeNames = degrees.stream().map(Degree::getName).collect(Collectors.toList());
        ObservableList<String> degreeObsList = FXCollections.observableArrayList(degreeNames);
        final SearchableComboBox<String> degreeComboBox = new SearchableComboBox<>(degreeObsList);

        greetingLabel.setText(String.format("Tervetuloa Sisuun %s!", student.getFirstName()));
        degreeLabel.setText(degree.getName());
        nameLabel.setText(student.getName());
        studentNumberLabel.setText(student.getStudentNumber());
        emailLabel.setText(student.getEmailAddress());

        // Preparing tabs.
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab homeTab = new Tab();
        Tab courseTab = new Tab("Kurssit");
        Tab personalTab = new Tab("Omat tiedot");

        tabPane.getTabs().add(homeTab);
        tabPane.getTabs().add(courseTab);
        tabPane.getTabs().add(personalTab);


        // TODO: Make different grids for tabs. Also make treeView work.
        HashMap<String, List<String>> testTreeItems = new HashMap<>();
        List<String> testList = Arrays.asList("Kurssi1", "Kurssi2", "Kurssi3", "Kurssi4");
        testTreeItems.put("Joku StudyModule1", testList);
        testTreeItems.put("Joku StudyModule2", testList);

        TreeItem<String> rootNode = new TreeItem<>(degree.getName());
        // TODO: StudyModule = TreeItem.
        for(var treeItem : testTreeItems.entrySet()) {
            TreeItem<String> webItem = new TreeItem<>(treeItem.getKey());
            for(var item : treeItem.getValue()) {
                webItem.getChildren().add(new TreeItem<>(item));
            }
            rootNode.getChildren().add(webItem);
        }

        TreeView<String> treeView = new TreeView<>();
        treeView.setRoot(rootNode);

        treeView.setShowRoot(true);

        // Creating all containers.
        VBox homeBox = new VBox(15);

        GridPane homeGrid = new GridPane();
        homeGrid.setHgap(15);
        homeGrid.setVgap(15);
        homeGrid.setPadding(new Insets(15,15,15,15));

        GridPane courseGrid = new GridPane();
        courseGrid.setHgap(15);
        courseGrid.setVgap(15);
        courseGrid.setPadding(new Insets(15,15,15,15));

        GridPane personalGrid = new GridPane();
        personalGrid.setHgap(15);
        personalGrid.setVgap(15);
        personalGrid.setPadding(new Insets(15,15,15,15));

        // Adding nodes to grids.
        // node, columnIndex, rowIndex, columnSpan, rowSpan:
        homeBox.getChildren().add(tabPane);
        homeTab.setContent(homeGrid);
        courseTab.setContent(courseGrid);
        personalTab.setContent(personalGrid);

        homeGrid.add(greetingLabel, 0, 0);
        homeGrid.add(degreeLabel,0,1);

        courseGrid.add(degreeComboBox, 0, 0, 3, 1);
        courseGrid.add(treeView, 0, 2, 3, 3);

        personalGrid.add(nameInfoLabel, 0, 0);
        personalGrid.add(nameLabel, 0, 1);
        personalGrid.add(studentNumberInfoLabel, 1, 0);
        personalGrid.add(studentNumberLabel, 1, 1);
        personalGrid.add(emailInfoLabel, 0, 2);
        personalGrid.add(emailLabel, 0, 3);

        // Setting css id:s.
        homeGrid.getStyleClass().add("secBackground");
        homeTab.getStyleClass().add("homeIcon");
        tabPane.getStyleClass().add("tabPane");
        degreeComboBox.getStyleClass().add("comboBox");

        var scene = new Scene(homeBox, 900, 650);
        stage.setScene(scene);
        final String style = getClass().getResource("stylesheet.css").toExternalForm();
        scene.getStylesheets().add(style);
        stage.setTitle("SISU");
        stage.show();

        // TODO: Change degree
        degreeComboBox.valueProperty().
                addListener((ObservableValue<? extends String> o, String oldValue, String newValue) ->
                {});
    }
}
