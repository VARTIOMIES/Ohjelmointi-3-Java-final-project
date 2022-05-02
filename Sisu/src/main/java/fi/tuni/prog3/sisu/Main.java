package fi.tuni.prog3.sisu;

import com.google.gson.*;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * A class to initialize the program and launch the GUI.
 */
public class Main extends Application {

    /* Lists containing references to every object used in the program. Just to
    make sure that every object can be accessed at least somehow. */
    private List<Student> students;
    private List<Degree> degrees;
    private Gson gson;

    // Constructor

    /**
     * Constructor for the Main class
     * @throws IOException
     */
    public Main() throws IOException {


        // Initializing all containers
        students = new ArrayList<>();
        degrees = new ArrayList<>();
        gson = new GsonBuilder().setPrettyPrinting().create();

        // Initializing functions

        degreeRead();
        degrees.add(new Degree("","",0));
        createStudents();
    }

    // The Sisu main window now exists in class MainStage.

    /**
     * Starts the GUI by launching LoginGUI
     * Precondition: log in gui has been created
     * Post-conditions: json-file has added new student
     * @param stage Starts LoginStage
     */
    @Override
    public void start(Stage stage) {
        new LogInGui(stage, degrees, students);
    }

    /**
     * Starts the whole program
     *
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Reads the degrees and creates objects for each degree.
     * @throws IOException
     */
    public void degreeRead() throws IOException {

        // Connecting to all degrees.
        String stringURL = "https://sis-tuni.funidata.fi/kori/api/module-search?curriculumPeriodId=uta-lvv-2021&universityId=tuni-university-root-id&moduleType=DegreeProgramme&limit=1000";

        URL url = new URL(stringURL);

        URLConnection request = url.openConnection();

        // Importing Gson to parse JSON data

        JsonElement element = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
        JsonObject elementObject = element.getAsJsonObject();
        JsonArray degreePrograms = elementObject.get("searchResults").getAsJsonArray();

        // Initializing Degree objects
        for (var degree : degreePrograms) {

            var groupId = degree.getAsJsonObject().get("groupId").getAsString();
            var name = degree.getAsJsonObject().get("name").getAsString();
            var creditEntries = degree.getAsJsonObject().get("credits").getAsJsonObject().get("min").getAsInt();

            var newDegree = new Degree(groupId, name, creditEntries);
            degrees.add(newDegree);
        }
    }


    /**
     * Creates student objects by reading a datafile.
     * @throws IOException
     */
    public void createStudents() throws IOException {
        // Creating reade to read the datafile
        Reader reader = Files.newBufferedReader(Paths.get("Sisudatafile.json"));

        // Initializing studentsArray
        Student[] studentsArray = gson.fromJson(reader,Student[].class);

        // Adding the students from studentsArray to students.
        students.addAll(Arrays.asList(studentsArray));

    }

    /**
     * Writes the Json file with updated students.
     * @throws IOException
     */
    @Override
    public void stop() throws IOException {

        // Creating a writer to update the Json datafile
        Writer writer = Files.newBufferedWriter(Paths.get("Sisudatafile.json"));
        gson.toJson(students,writer);
        writer.close();
    }
}

