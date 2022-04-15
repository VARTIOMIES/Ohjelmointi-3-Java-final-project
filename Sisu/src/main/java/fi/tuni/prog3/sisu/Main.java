package fi.tuni.prog3.sisu;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Main extends Application {

    /* Lists containing references to every object used in the program. Just to
    make sure that every object can be accessed at least somehow. */
    private List<Student> students;
    private List<Teacher> teachers;
    private List<Degree> degrees;
    private List<Attainment> attainments;
    private HashMap<Degree, JsonArray> modules;
    private HashMap<Module, List<StudyModule>> studyModules;
    private HashMap<StudyModule, List<Course>> courses;


    // Constructor
    public Main() throws IOException {
        // First read datafile.
        // Then get everything from datafile and create objects.
        // Then add those objects to the lists.
        // ...
        // profit.

        // TODO: Datafile reading

        // Initializing all containers
        students = new ArrayList<>();
        teachers = new ArrayList<>();
        degrees = new ArrayList<>();
        attainments = new ArrayList<>();
        modules = new HashMap<>();
        courses = new HashMap<>();

        // TODO: Fill containers with the data from datafile.

        degreeRead(degrees);
        courseRead(degrees);

    }

    // The Sisu main window now exists in class MainStage.
    @Override
    public void start(Stage stage) {
        new StartStage(getDegrees());
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
        for(var degree : degreePrograms) {
            var id = degree.getAsJsonObject().get("id").getAsString();
            var code = degree.getAsJsonObject().get("code").getAsString();
            var language = degree.getAsJsonObject().get("lang").getAsString();
            var groupId = degree.getAsJsonObject().get("groupId").getAsString();
            var name = degree.getAsJsonObject().get("name").getAsString();
            var creditEntries = degree.getAsJsonObject().get("credits").getAsJsonObject().entrySet();


            var creditMin = 0;
            for(var entry : creditEntries) {
                if(Objects.equals(entry.getKey(), "min")) {
                    creditMin = entry.getValue().getAsInt();
                }
            }

            var newDegree = new Degree(id,code,language,groupId,name, creditMin);
            degrees.add(newDegree);

        }

    }

    public void courseRead(List<Degree> degrees) throws IOException {


        for (var degree : degrees) {

            var isRule = true;


            var degreeURL = "";
            var substring = degree.getGroupId().substring(0, 3);
            if (substring.equals("otm")) {
                degreeURL = "https://sis-tuni.funidata.fi/kori/api/modules/" + degree.getGroupId();
            } else {
                degreeURL = "https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?groupId=" + degree.getGroupId() + "&universityId=tuni-university-root-id";
            }
            /*
            URL url = new URL(degreeURL);
            URLConnection request = url.openConnection();
            JsonElement degreeElement = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
            var degreeObject = degreeElement.getAsJsonObject().get("metadata").getAsJsonArray();


            if(degreeObject.get("type").getAsString().equals("CreditsRule")) {
                var compositeRule = degreeObject.get("rule").getAsJsonObject();
                if (compositeRule.get("type").equals("CompositeRule")) {
                    var compositeArray = compositeRule.get("rules").getAsJsonArray();
                    var compositeArrayType = compositeArray.get(0).getAsJsonObject().get("type");
                    if(compositeArrayType.equals("CompositeRule")) {
                        var moduleRules = compositeArray.get(0).getAsJsonObject().get("rules").getAsJsonArray();
                } else {
                        var moduleRules = compositeArray;
                    }
            } else {
                    System.out.println("Invalid");

            }
            */
            /*
            try {
                JsonObject elementObject = element.getAsJsonObject();
                JsonObject moduleObjects = elementObject.get("rule").getAsJsonObject();
                var moduleRules = moduleObjects.get("rules").getAsJsonArray();
                modules.put(degree,moduleRules);
            } catch (IllegalStateException e) {
                JsonArray elementObject = element.getAsJsonArray();
                JsonObject moduleObjects = elementObject.get(0).getAsJsonObject();
                /*
                var moduleRules = moduleObjects.get(0).getAsJsonArray();
                v
                ar moduleRules1 = moduleObjects.get(0).getAsJsonArray();
                 */

                //modules.put(degree,moduleRules1);

            }



            /*
            for (var moduleRule : moduleRules) {
                var moduleGroup = moduleRule.getAsJsonObject().get("moduleGroupId");
                URL moduleRuleURL = new URL("https://sis-tuni.funidata.fi/kori/api/modules/" + moduleGroup.getAsString());
                URLConnection modRequest = moduleRuleURL.openConnection();

                JsonElement modElement = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));

                var modElementObject = modElement.getAsJsonObject();
                var moduleRuleObjects = elementObject.get("rule").getAsJsonObject();
                var majorRules = moduleRuleObjects.get("rules").getAsJsonArray();

            }

               */
        }





    public List<Student> getStudents() {
        return students;
    }

    public void addStudent(String name, String studentNumber, int startingYear, Degree degree) {
        Student newStudent = new Student(name, studentNumber, startingYear, degree);
        students.add(newStudent);
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public List<Degree> getDegrees() {
        return degrees;
    }

    public List<String> getDegreeNames() {
        return degrees.stream().map(Degree::getName).collect(Collectors.toList());
    }
}
