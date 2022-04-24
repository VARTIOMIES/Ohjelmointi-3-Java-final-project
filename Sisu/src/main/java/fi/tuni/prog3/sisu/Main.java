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
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.stream.Collectors;

public class Main extends Application {

    /* Lists containing references to every object used in the program. Just to
    make sure that every object can be accessed at least somehow. */
    private List<Student> students;
    private List<Teacher> teachers;
    private List<Degree> degrees;
    private List<Attainment> attainments;
    private HashMap<Degree, JsonArray> modules;
    private HashMap<JsonObject, JsonArray> studyModules;
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
        studyModules = new HashMap<>();

        // TODO: Fill containers with the data from datafile.

        degreeRead(degrees);
//        moduleRead(degrees);
//        studyModuleRead(modules);
//        courseRead(studyModules);

    }

    // The Sisu main window now exists in class MainStage.
    @Override
    public void start(Stage stage) {
        new LogInStage(stage, degrees, students);
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

    public void moduleRead(List<Degree> degrees) throws IOException {


        for (var degree : degrees) {

            var moduleGroupId = degree.getGroupId();
            var degreeURL = createModuleURL(moduleGroupId);

            JsonObject degreeObject;

            URL url = new URL(degreeURL);
            URLConnection request = url.openConnection();
            JsonElement degreeElement = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
            if (!degreeElement.isJsonObject()) {
                degreeObject = degreeElement.getAsJsonArray().get(0).getAsJsonObject();
            } else {
                degreeObject = degreeElement.getAsJsonObject();
            }

            JsonArray moduleRules;

            if (degreeObject.get("type").getAsString().equals("DegreeProgramme")) {
                var creditsRule = degreeObject.get("rule").getAsJsonObject();
                if (creditsRule.get("type").getAsString().equals("CreditsRule")) {
                    var creditsObject = creditsRule.get("rule").getAsJsonObject();
                    var creditsObjectType = creditsObject.get("type");
                    if (creditsObjectType.getAsString().equals("CompositeRule")) {
                        moduleRules = creditsObject.get("rules").getAsJsonArray();
                        modules.put(degree, moduleRules);
                    } else {
                        moduleRules = creditsObject.get("rules").getAsJsonArray();
                        modules.put(degree, moduleRules);
                    }
                } else if (creditsRule.get("type").getAsString().equals("CompositeRule")) {
                    moduleRules = creditsRule.get("rules").getAsJsonArray();
                    modules.put(degree, moduleRules);
                }
            }
        }
    }

    public void studyModuleRead(HashMap<Degree, JsonArray> modules) throws IOException {

        for(var degree : modules.entrySet()) {
            var tempModules = new JsonArray();
            var modul = recursiveModules(degree.getValue(),tempModules);
            JsonArray studyModuleRules = new JsonArray();
            JsonObject studyModuleObject = new JsonObject();

            for(var moduleGroup : modul) {


                var moduleGroupId = moduleGroup.getAsJsonObject().get("moduleGroupId").getAsString();
                var studyModuleURL = createModuleURL(moduleGroupId);

                URL url = new URL(studyModuleURL);
                URLConnection request = url.openConnection();
                JsonElement studyElement = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));

                if(!studyElement.isJsonObject()) {
                    studyModuleObject = studyElement.getAsJsonArray().get(0).getAsJsonObject();
                } else {
                    studyModuleObject = studyElement.getAsJsonObject();
                }


                studyModuleRules.add(studyModuleObject.get("rule").getAsJsonObject());


            }
            studyModules.put(studyModuleObject, studyModuleRules);

        }


    }

    public void courseRead(HashMap<JsonObject, JsonArray> studyModules) {

        for(var studyModule : studyModules.entrySet()) {

            JsonArray tempModules = new JsonArray();
            var courses = recursiveCourses(studyModule.getValue(),tempModules);
            System.out.println("asd");


        }

    }

    private String createModuleURL(String moduleGroupId) {

        var URL = "";
        var substring = moduleGroupId.substring(0, 3);
        if (substring.equals("otm")) {
            URL = "https://sis-tuni.funidata.fi/kori/api/modules/" + moduleGroupId;
        } else {
            URL = "https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?groupId=" + moduleGroupId + "&universityId=tuni-university-root-id";
        }

        return URL;
    }


    private JsonObject createModuleObject(URL url) throws IOException {

        var moduleObject = new JsonObject();
        URLConnection request = url.openConnection();
        JsonElement element = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));

        if(!element.isJsonObject()) {
            moduleObject = element.getAsJsonArray().get(0).getAsJsonObject();
        } else {
            moduleObject = element.getAsJsonObject();
        }

        return moduleObject;
    }


    public JsonArray recursiveModules(JsonArray modules, JsonArray tempModules) {


        for(var subModule : modules) {
            if (subModule.getAsJsonObject().get("type").getAsString().equals("ModuleRule")) {
                tempModules.add(subModule.getAsJsonObject());
            } else {
                recursiveModules(subModule.getAsJsonObject().get("rules").getAsJsonArray(),tempModules);
            }
        }
        return tempModules;
    }


    public JsonArray recursiveCourses(JsonArray modules, JsonArray tempModules) {


        for(var subCourse : modules) {
            if (subCourse.getAsJsonObject().get("type").getAsString().equals("CourseUnitRule")) {
                tempModules.add(subCourse.getAsJsonObject());
            } else if(subCourse.getAsJsonObject().get("type").getAsString().equals("CompositeRule")){
                recursiveCourses(subCourse.getAsJsonObject().get("rules").getAsJsonArray(),tempModules);
            } else if(subCourse.getAsJsonObject().get("type").getAsString().equals("ModuleRule")){

            } else {
                recursiveCourses(subCourse.getAsJsonObject().get("rule").getAsJsonObject().get("rules").getAsJsonArray(), tempModules);
            }
        }
        return tempModules;
    }




    public List<Student> getStudents() {
        return students;
    }

    public void addStudent(String name, String studentNumber, int startingYear, Degree degree) {
        if(isStudent(studentNumber)) {
            Student newStudent = new Student(name, studentNumber, startingYear, degree);
            students.add(newStudent);
        }
    }

    public boolean isStudent(String studentNumber) {
        return students.stream().anyMatch(s -> studentNumber.equals(s.getStudentNumber()));
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