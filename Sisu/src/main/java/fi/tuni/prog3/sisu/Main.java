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
    private List<Teacher> teachers;
    private List<Degree> degrees;
    private List<Attainment> attainments;
    private HashMap<Degree, JsonArray> modules;
    private HashMap<JsonObject, JsonArray> studyModules;
    private HashMap<StudyModule, List<Course>> courses;


    // Constructor
    public Main() throws IOException {

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

        /*
        TÄTÄ MUOKKAAMALLA LOKAALISTI SAA KÄÄNTYMÄÄN NOPEEMMIN KUN EI LUE APIA
         */
        boolean API_READ = true;

        if(API_READ){
            degreeRead(degrees);
//            moduleRead(degrees);
//            studyModuleRead(modules);
//            courseRead(degrees);
        }
        addTestStudents();
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

            URL url = new URL(degreeURL);
            JsonObject degreeObject = createModuleObject(url);

            var moduleArray = new JsonArray();
            JsonArray moduleRules = recursiveDegreeModule(degreeObject, moduleArray);
            modules.put(degree,moduleRules);
            //degree.setModules(moduleRules);
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
                studyModuleObject = createModuleObject(url);
                studyModuleRules.add(studyModuleObject.get("rule").getAsJsonObject());
                degree.getKey().setModules(studyModuleObject);

            }
            studyModules.put(studyModuleObject, studyModuleRules);


        }
        }

    public void courseRead(List<Degree> degrees) throws IOException {

        for(var degree : degrees) {

            JsonArray tempModules = new JsonArray();
            var courses = recursiveCourses(degree.getModules(),tempModules);
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
            } else if(subModule.getAsJsonObject().get("type").getAsString().equals("CompositeRule")){
                recursiveModules(subModule.getAsJsonObject().get("rules").getAsJsonArray(),tempModules);
            } else {
                recursiveModules(subModule.getAsJsonObject().get("rule").getAsJsonArray(),tempModules);
            }
        }
        return tempModules;
        }


    public JsonArray recursiveCourses(JsonArray modules, JsonArray tempModules) throws IOException {



        for(var subCourse : modules) {

            if (subCourse.getAsJsonObject().get("type").getAsString().equals("CourseUnitRule")) {
                if(!tempModules.contains(subCourse.getAsJsonObject())) {
                    tempModules.add(subCourse.getAsJsonObject());

                }

            } else if(subCourse.getAsJsonObject().get("type").getAsString().equals("CompositeRule")){
                tempModules = recursiveCourses(subCourse.getAsJsonObject().get("rules").getAsJsonArray(),tempModules);
            } else if(subCourse.getAsJsonObject().get("type").getAsString().equals("ModuleRule")){
                var moduleUrl = createModuleURL(subCourse.getAsJsonObject().get("moduleGroupId").getAsString());
                URL url = new URL(moduleUrl);
                var moduleObject = createModuleObject(url);

                if(moduleObject.get("type").getAsString().equals("ModuleRule")) {
                    tempModules = recursiveCourses(moduleObject.getAsJsonObject().getAsJsonArray(),tempModules);
                } else if(moduleObject.get("type").getAsString().equals("CompositeRule")){
                    tempModules = recursiveCourses(moduleObject.get("rules").getAsJsonArray(),tempModules);
                } else if(moduleObject.get("type").getAsString().equals("StudyModule")){

                    tempModules = recursiveCourses(checkModuleObject(moduleObject),tempModules);

                } else {
                    tempModules = recursiveCourses(checkModuleObject(moduleObject),tempModules);
                }

            } else if(subCourse.getAsJsonObject().get("type").getAsString().equals("AnyCourseUnitRule")){
                System.out.println("asd");
            }else {
                if(subCourse.getAsJsonObject().get("type").getAsString().equals("AnyModuleRule")) {
                    System.out.println("asff");
                } else {
                    tempModules = recursiveCourses(subCourse.getAsJsonObject().get("rule").getAsJsonObject().get("rules").getAsJsonArray(), tempModules);
                }


            }

        }
        return tempModules;


    }

    public JsonArray checkModuleObject(JsonObject moduleObject) {
        if(moduleObject.get("rule").getAsJsonObject().get("type").getAsString().equals("CompositeRule")) {
            return moduleObject.get("rule").getAsJsonObject().get("rules").getAsJsonArray();
        } else {
            return moduleObject.get("rule").getAsJsonObject().get("rule").getAsJsonObject().get("rules").getAsJsonArray();
        }
    }

    public JsonArray recursiveDegreeModule(JsonObject degreeObject, JsonArray moduleArray) {
        if (degreeObject.getAsJsonObject().get("type").getAsString().equals("CompositeRule")) {
            moduleArray = degreeObject.getAsJsonObject().get("rules").getAsJsonArray();
            return moduleArray;
        } else {
            moduleArray = recursiveDegreeModule(degreeObject.getAsJsonObject().get("rule").getAsJsonObject(), moduleArray);
        }
        return moduleArray;
    }

    public void addTestStudents() {
        students.add(new Student("Heikki Paasonen", "12345678", 2001, degrees.get(0)));
        students.add(new Student("Kimmo Koodari", "12345679", 2002, degrees.get(2)));
        students.add(new Student("Kimmo Koodari", "15344444", 2020, degrees.get(20)));
        students.add(new Student("Ronja Lipsonen", "50121133", 2020, degrees.get(3)));
    }
}

