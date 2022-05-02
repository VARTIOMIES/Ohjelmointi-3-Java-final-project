package fi.tuni.prog3.sisu;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


/**
 * This class is used to read API and store information about a specific Degree.
 */
public class Degree {

    private final String groupId;
    private final String name;
    private final int creditsMin;

    private transient ArrayList<Module> modules;


    /**
     * Constructor for the Degree object.
     * @param groupId Used to navigate to the modules.
     * @param name Name of the degree.
     * @param creditsMin Minimum amount of credits to complete the degree.
     */
    public Degree(String groupId, String name, int creditsMin) {

        this.groupId = groupId;

        this.name = name;
        this.creditsMin = creditsMin;

        modules = new ArrayList<>();

    }



    /**
     * Gets the groupId for advancing in the API
     * @return degree groupId
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * Gets the name of the degree
     * @return degree name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns credits from the degree
     * @return credits
     */
    public int getCreditsMin() {
        return creditsMin;
    }


    /**
     * Adds a module to the module list
     * Precondition: module has been created
     * Post-conditions: modules is not empty
     * @param module the stored module
     */
    public void setModules(Module module) {
        if(modules == null) {
            this.modules = new ArrayList<>();
        }
        modules.add(module);
    }

    /**
     * Gets modules for the current degree
     * @return list of modules
     */
    public ArrayList<Module> getModules() {
        return this.modules;
    }

    /**
     * Reads the API where most of the information is stored.
     * Stores modules and courses to the current degree.
     * @throws IOException
     */
    public void readAPI() throws IOException {

        moduleRead(this);
        studyModuleRead();
        courseRead(this);



    }

    /**
     * Reads the modules from the current degree.
     * @param degree Current degree
     * @throws IOException
     */
    private void moduleRead(Degree degree) throws IOException {

            // Creating variable which are needed in creating a JsonObject
            var moduleGroupId = degree.getGroupId();
            var degreeURL = createModuleURL(moduleGroupId);

            // Creating URL for the object
            URL url = new URL(degreeURL);
            JsonObject degreeObject = createModuleObject(url);

            // Getting the moduleRules through recursion.
            var moduleArray = new JsonArray();
            JsonArray moduleRules = recursiveDegreeModule(degreeObject, moduleArray);

            // Checking Modules from the degree Objects
            var tempModules = new JsonArray();
            var modRules = recursiveModules(moduleRules,tempModules);
            for(var module : modRules) {

                // Variable for a new Module object
                Module mod = null;

                // Creating an array for studyModules
                var studyModules = new ArrayList<StudyModule>();

                // Creating a JsonObject
                var moduleString = module.getAsJsonObject().get("moduleGroupId").getAsString();
                String moduleURLString = createModuleURL(moduleString);
                URL moduleURL = new URL(moduleURLString);
                var moduleObject = createModuleObject(moduleURL);

                // Checks the name of the object.
                if(moduleObject.get("name").getAsJsonObject().get("fi") == null) {
                    mod = new Module(moduleObject.get("name").getAsJsonObject().get("en").getAsString(),moduleObject.get("id").getAsString(), studyModules);
                } else {
                    mod = new Module(moduleObject.get("name").getAsJsonObject().get("fi").getAsString(),moduleObject.get("id").getAsString(), studyModules);
                }
                // Adds the module to the degree
                degree.setModules(mod);

            }



    }

    /**
     * Stores the study modules of the current degree.
     * @throws IOException
     */
    private void studyModuleRead() throws IOException {

        // Loops through all the modules in degree.
        for(var rule : this.modules) {

                // Creating arrays for the wanted attributes
                JsonArray studyModuleRules = new JsonArray();
                var courseArray = new ArrayList<Course>();

                // Creating a JsonObject
                var moduleGroupId = rule.getModuleCode();
                var studyModuleURL = createModuleURL(moduleGroupId);
                StudyModule studyModule = null;
                URL url = new URL(studyModuleURL);
                JsonObject studyModuleObject = createModuleObject(url);
                studyModuleRules.add(studyModuleObject);

                // Creating a study module name
                if (studyModuleObject.get("name").getAsJsonObject().get("fi") == null) {
                    studyModule = new StudyModule(studyModuleObject.get("name").getAsJsonObject().get("en").getAsString(), studyModuleRules, courseArray);
                } else {
                    studyModule = new StudyModule(studyModuleObject.get("name").getAsJsonObject().get("fi").getAsString(), studyModuleRules, courseArray);
                }
                // Adds the study module to degree.
                rule.setStudyModules(studyModule);
            }
        }


    /**
     * Stores the courses in the current degree
     * @param degree Current degree
     * @throws IOException
     */
    private void courseRead(Degree degree) throws IOException {

            // Loops through modules of the degree
            for (var module : degree.getModules()) {

                // Loops through study modules of the module
                for(var studyModule : module.getStudyModules()) {

                    // Recursive function to ho through modules
                    JsonArray tempCourses = new JsonArray();
                    var courses = recursiveCourses(studyModule.getModuleRules(), tempCourses);

                    // Loops through courses
                    for (var course : courses) {

                        // Creating a JsonObject
                        var courseURL = createCourseURL(course.getAsJsonObject().get("courseUnitGroupId").getAsString());
                        URL url = new URL(courseURL);
                        var courseObject = createModuleObject(url);
                        var courseName = "";

                        // Checks the name
                        if (courseObject.getAsJsonObject().get("name").getAsJsonObject().get("fi") == null) {
                            courseName = courseObject.get("name").getAsJsonObject().get("en").getAsString();
                        } else {
                            courseName = courseObject.get("name").getAsJsonObject().get("fi").getAsString();
                        }

                        // Creates a course object and stores it
                        var createCourse = new Course(courseName, courseObject.getAsJsonObject().get("code").getAsString(), courseObject.getAsJsonObject().get("credits").getAsJsonObject().get("min").getAsInt());
                        studyModule.setCourses(createCourse);
                    }

                }
            }
        }


    /**
     * Creates the URL string for the wanted module URL
     * @param moduleGroupId Group Id for the wanted module
     * @return Complete string
     */
    private String createModuleURL(String moduleGroupId) {

        // Creates the URL depending on the substring in front.
        var URL = "";
        var substring = moduleGroupId.substring(0, 3);

        // Compares the substring
        if (substring.equals("otm")) {
            URL = "https://sis-tuni.funidata.fi/kori/api/modules/" + moduleGroupId;
        } else {
            URL = "https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?groupId=" + moduleGroupId + "&universityId=tuni-university-root-id";
        }

        // Return the URL string
        return URL;
    }

    /**
     * Creates the URL string for the wanted course URL
     * @param courseUnitId wanted courseUnitId for the course URL
     * @return Complete string
     */
    private String createCourseURL(String courseUnitId) {


        // Creates the URL for the courses depending on the substring on front.
        var URL = "";
        var substring = courseUnitId.substring(0, 3);
        if (substring.equals("otm")) {
            URL = "https://sis-tuni.funidata.fi/kori/api/course-units/" + courseUnitId;
        } else {
            URL = "https://sis-tuni.funidata.fi/kori/api/course-units/by-group-id?groupId=" + courseUnitId + "&universityId=tuni-university-root-id";
        }

        // Returns the URL string
        return URL;
    }


    /**
     * Creates a Module object from the API
     * @param url URL to connect to the object
     * @return Wanted object in JSON format
     * @throws IOException
     */
    private JsonObject createModuleObject(URL url) throws IOException {

        // Creating a new JsonObject
        var moduleObject = new JsonObject();
        URLConnection request = url.openConnection();
        JsonElement element = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));

        // Checks if the object is actually an object
        if(!element.isJsonObject()) {
            moduleObject = element.getAsJsonArray().get(0).getAsJsonObject();
        } else {
            moduleObject = element.getAsJsonObject();
        }

        // Returns the object
        return moduleObject;
    }

    /**
     * Recursive function to go through modules.
     * @param modules Modules from the degree
     * @param tempModules Array to store the modules
     * @return tempModules
     */
    private JsonArray recursiveModules(JsonArray modules, JsonArray tempModules) {

        // Looping through modules
        for(var subModule : modules) {

            // If it is a module, it will be added to the tempModules
            if (subModule.getAsJsonObject().get("type").getAsString().equals("ModuleRule")) {
                tempModules.add(subModule.getAsJsonObject());
                // If it is a CompositeRule, the module rules can be found inside.
            } else if(subModule.getAsJsonObject().get("type").getAsString().equals("CompositeRule")){
                recursiveModules(subModule.getAsJsonObject().get("rules").getAsJsonArray(),tempModules);
            } else {
                // If it is something else, such as CreditsRule, recursive function takes the rule as an array
                recursiveModules(subModule.getAsJsonObject().get("rule").getAsJsonArray(),tempModules);
            }
        }
        // Returns tempModules
        return tempModules;
    }

    /**
     * Recursive function to locate and store courses.
     * @param rules Different module, such as CompositeRule, ModuleRule etc.
     * @param tempCourses Temporary array to store courses.
     * @return Complete list of courses in current degree.
     * @throws IOException
     */
    private JsonArray recursiveCourses(JsonArray rules, JsonArray tempCourses) throws IOException {

        // Loops through the rules
        for(var rule : rules) {

            // If the current rule is a CourseUnit, Course will be added to the tempCourses
            if (rule.getAsJsonObject().get("type").getAsString().equals("CourseUnitRule")) {
                if (!tempCourses.contains(rule.getAsJsonObject())) {
                    tempCourses.add(rule.getAsJsonObject());

                }

                // If the rule is a CompositeRule, recursive function will take its rules as an array.
            }else if(rule.getAsJsonObject().get("type").getAsString().equals("CompositeRule")) {
                tempCourses = recursiveCourses(rule.getAsJsonObject().get("rules").getAsJsonArray(),tempCourses);

                // If the rule is ModuleRule, it will be created as an object and used recursion again
            } else if(rule.getAsJsonObject().get("type").getAsString().equals("ModuleRule")) {
                var moduleUrl = createModuleURL(rule.getAsJsonObject().get("moduleGroupId").getAsString());
                URL url = new URL(moduleUrl);
                var moduleObject = createModuleObject(url);

                // Same type of exception testing
                if(moduleObject.get("type").getAsString().equals("ModuleRule")) {
                    tempCourses = recursiveCourses(moduleObject.getAsJsonObject().getAsJsonArray(),tempCourses);
                } else if(moduleObject.get("type").getAsString().equals("CompositeRule")){
                    tempCourses = recursiveCourses(moduleObject.get("rules").getAsJsonArray(),tempCourses);
                } else if(moduleObject.get("type").getAsString().equals("StudyModule")){

                    tempCourses = recursiveCourses(checkModuleObject(moduleObject),tempCourses);

                } else {
                    tempCourses = recursiveCourses(checkModuleObject(moduleObject),tempCourses);
                }

                // AnyModule rules and AnyCourseUnits won't be processed.
            } else if(rule.getAsJsonObject().get("type").getAsString().equals("AnyModuleRule")) {
                continue;
            } else if(rule.getAsJsonObject().get("type").getAsString().equals("AnyCourseUnitRule")){
                continue;
            } else {
                // Exception checks
                if (rule.getAsJsonObject().get("rule").getAsJsonObject().get("type").getAsString().equals("CompositeRule")) {
                    tempCourses = recursiveCourses(rule.getAsJsonObject().get("rule").getAsJsonObject().get("rules").getAsJsonArray(), tempCourses);
                } else {
                    tempCourses = recursiveCourses(rule.getAsJsonObject().get("rule").getAsJsonObject().get("rule").getAsJsonObject().get("rules").getAsJsonArray(),tempCourses);
                }
            }
        }

        // Returns tempCourses
        return tempCourses;


    }

    /**
     * Checks the validity of a module object
     * @param moduleObject Required module object
     * @return JsonArray of modules.
     */
    private JsonArray checkModuleObject(JsonObject moduleObject) {

        // Checks if the moduleObject has a CompositeRule inside
        if(moduleObject.get("rule").getAsJsonObject().get("type").getAsString().equals("CompositeRule")) {
            return moduleObject.get("rule").getAsJsonObject().get("rules").getAsJsonArray();
        } else {
            return moduleObject.get("rule").getAsJsonObject().get("rule").getAsJsonObject().get("rules").getAsJsonArray();
        }
    }

    /**
     * Recursive function to find modules
     * @param degreeObject Degree where the modules are
     * @param moduleArray Temporary Array to be filled with modules
     * @return Complete list of modules in that current degree
     */
    private JsonArray recursiveDegreeModule(JsonObject degreeObject, JsonArray moduleArray) {

        // Checks the type, if not in wanted type, recurses again.
        if (degreeObject.getAsJsonObject().get("type").getAsString().equals("CompositeRule")) {
            moduleArray = degreeObject.getAsJsonObject().get("rules").getAsJsonArray();
            return moduleArray;
        } else {
            moduleArray = recursiveDegreeModule(degreeObject.getAsJsonObject().get("rule").getAsJsonObject(), moduleArray);
        }

        // Return moduleArray
        return moduleArray;
    }






}

