package fi.tuni.prog3.sisu;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.stream.Collectors;

public class Main extends Application {

    /* Lists containing references to every object used in the program. Just to
    make sure that every object can be accessed at least somehow. */
    private List<Student> students;
    private List<Degree> degrees;

    // Constructor
    public Main() throws IOException {

        // TODO: Datafile reading

        // Initializing all containers
        students = new ArrayList<>();
        degrees = new ArrayList<>();


        // TODO: Fill containers with the data from datafile.

        /*
        TÄTÄ MUOKKAAMALLA LOKAALISTI SAA KÄÄNTYMÄÄN NOPEEMMIN KUN EI LUE APIA
         */
        boolean API_READ = true;

        if(API_READ){
            degreeRead(degrees);

        }
        addTestStudents();
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
            var creditEntries = degree.getAsJsonObject().get("credits").getAsJsonObject().entrySet();

            var creditMin = 0;
            for (var entry : creditEntries) {
                if (Objects.equals(entry.getKey(), "min")) {
                    creditMin = entry.getValue().getAsInt();
                }
            }
            var newDegree = new Degree(id, code, language, groupId, name, creditMin);
            degrees.add(newDegree);
        }
    }





    public void addTestStudents() {
        students.add(new Student("Heikki Paasonen", "12345678", 2001, degrees.get(0)));
        students.add(new Student("Kimmo Koodari", "12345679", 2002, degrees.get(2)));
        students.add(new Student("Kimmo Koodari", "15344444", 2020, degrees.get(20)));
        Student courseTestStudent = new Student("Ronja Lipsonen", "50121133", 2020, degrees.get(3));
        students.add(courseTestStudent);
    }
}

