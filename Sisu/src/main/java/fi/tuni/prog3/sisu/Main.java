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

public class Main extends Application {

    /* Lists containing references to every object used in the program. Just to
    make sure that every object can be accessed at least somehow. */
    private List<Student> students;
    private List<Degree> degrees;
    private Gson gson;

    // Constructor
    public Main() throws IOException {


        // Initializing all containers
        students = new ArrayList<>();
        degrees = new ArrayList<>();
        gson = new GsonBuilder().setPrettyPrinting().create();

        // Initializing functions
        degreeRead(degrees);
        createStudents();
    }

    // The Sisu main window now exists in class MainStage.
    @Override
    public void start(Stage stage) {
        new LogInGui(stage, degrees, students);
    }

    
    public static void main(String[] args) throws IOException {
        launch();
    }

    public static void degreeRead(List<Degree> degrees) throws IOException {

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
            var id = degree.getAsJsonObject().get("id").getAsString();
            var code = degree.getAsJsonObject().get("code").getAsString();
            var language = degree.getAsJsonObject().get("lang").getAsString();
            var groupId = degree.getAsJsonObject().get("groupId").getAsString();
            var name = degree.getAsJsonObject().get("name").getAsString();
            var creditEntries = degree.getAsJsonObject().get("credits").getAsJsonObject().get("min").getAsInt();

            var newDegree = new Degree(id, code, language, groupId, name, creditEntries);
            degrees.add(newDegree);
        }
    }


    public void createStudents() throws IOException {
        // Creating reade to read the datafile
        Reader reader = Files.newBufferedReader(Paths.get("Sisudatafile.json"));

        // Initializing studentsArray
        JsonArray studentsArray = gson.fromJson(reader,JsonArray.class);

        // Looping through JsonObjects
        for(var student : studentsArray) {
            students.add(new Student(
                    student.getAsJsonObject().get("name").getAsString(),
                    student.getAsJsonObject().get("studentNumber").getAsString(),
                    student.getAsJsonObject().get("startingYear").getAsInt(),
                    degrees.get(0)));
        }
    }

    @Override
    public void stop() throws IOException {

        // Creating a writer to update the Json datafile
        Writer writer = Files.newBufferedWriter(Paths.get("Sisudatafile.json"));
        gson.toJson(students,writer);
        writer.close();
    }
}

