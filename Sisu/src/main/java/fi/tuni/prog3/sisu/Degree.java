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
import java.util.HashMap;
import java.util.List;

public class Degree {
    private final String id;
    private final String code;
    private final String language;
    private final String groupId;
    private final String name;
    private final int creditsMin;

    private ArrayList<Module> modules;



    public Degree(String id, String code, String language, String groupId, String name, int creditsMin) {
        this.id = id;
        this.code = code;
        this.language = language;
        this.groupId = groupId;
        this.name = name;
        this.creditsMin = creditsMin;


        modules = new ArrayList<>();

    }

    public String getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getName() {
        return name;
    }

    public int getCreditsMin() {
        return creditsMin;
    }

    public String getCode() {
        return code;
    }

    public void setModules(Module module) {
        this.modules.add(module);
    }


    public ArrayList<Module> getModules() {
        return this.modules;
    }


    public void readAPI() throws IOException {
        moduleRead(this);
        studyModuleRead();
        courseRead(this);
    }

    private void moduleRead(Degree degree) throws IOException {


            var moduleGroupId = degree.getGroupId();
            var degreeURL = createModuleURL(moduleGroupId);

            URL url = new URL(degreeURL);
            JsonObject degreeObject = createModuleObject(url);

            var moduleArray = new JsonArray();
            JsonArray moduleRules = recursiveDegreeModule(degreeObject, moduleArray);


            var tempModules = new JsonArray();
            var modRules = recursiveModules(moduleRules,tempModules);
            for(var module : modRules) {


                Module mod = null;
                var studyModules = new ArrayList<StudyModule>();
                var moduleString = module.getAsJsonObject().get("moduleGroupId").getAsString();
                String moduleURLString = createModuleURL(moduleString);
                URL moduleURL = new URL(moduleURLString);
                var moduleObject = createModuleObject(moduleURL);

                if(moduleObject.get("name").getAsJsonObject().get("fi") == null) {
                    mod = new Module(moduleObject.get("name").getAsJsonObject().get("en").getAsString(),moduleObject.get("id").getAsString(), studyModules);
                } else {
                    mod = new Module(moduleObject.get("name").getAsJsonObject().get("fi").getAsString(),moduleObject.get("id").getAsString(), studyModules);
                }
                degree.setModules(mod);

            }



    }

    private void studyModuleRead() throws IOException {

        for(var rule : this.modules) {

                var tempModules = new JsonArray();

                JsonArray studyModuleRules = new JsonArray();
                JsonObject studyModuleObject = new JsonObject();
                var courseArray = new ArrayList<Course>();


                var moduleGroupId = rule.getModuleCode();
                var studyModuleURL = createModuleURL(moduleGroupId);
                StudyModule studyModule = null;
                URL url = new URL(studyModuleURL);
                studyModuleObject = createModuleObject(url);
                studyModuleRules.add(studyModuleObject);

                if (studyModuleObject.get("name").getAsJsonObject().get("fi") == null) {
                    studyModule = new StudyModule(studyModuleObject.get("name").getAsJsonObject().get("en").getAsString(), studyModuleRules, courseArray);
                } else {
                    studyModule = new StudyModule(studyModuleObject.get("name").getAsJsonObject().get("fi").getAsString(), studyModuleRules, courseArray);
                }
                rule.setStudyModules(studyModule);
            }
        }



    private void courseRead(Degree degree) throws IOException {

            for (var module : degree.getModules()) {
                for(var studyModule : module.getStudyModules()) {
                    JsonArray tempCourses = new JsonArray();
                    var courses = recursiveCourses(studyModule.getModuleRules(), tempCourses);

                    for (var course : courses) {

                        var courseURL = createCourseURL(course.getAsJsonObject().get("courseUnitGroupId").getAsString());
                        URL url = new URL(courseURL);
                        var courseObject = createModuleObject(url);
                        var courseName = "";
                        if (courseObject.getAsJsonObject().get("name").getAsJsonObject().get("fi") == null) {
                            courseName = courseObject.get("name").getAsJsonObject().get("en").getAsString();
                        } else {
                            courseName = courseObject.get("name").getAsJsonObject().get("fi").getAsString();
                        }

                        var createCourse = new Course(courseName, courseObject.getAsJsonObject().get("id").getAsString(), courseObject.getAsJsonObject().get("credits").getAsJsonObject().get("min").getAsInt());
                        studyModule.setCourses(createCourse);
                    }

                }
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

    private String createCourseURL(String courseUnitId) {

        var URL = "";
        var substring = courseUnitId.substring(0, 3);
        if (substring.equals("otm")) {
            URL = "https://sis-tuni.funidata.fi/kori/api/course-units/" + courseUnitId;
        } else {
            URL = "https://sis-tuni.funidata.fi/kori/api/course-units/by-group-id?groupId=" + courseUnitId + "&universityId=tuni-university-root-id";
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


    private JsonArray recursiveModules(JsonArray modules, JsonArray tempModules) {

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


    private JsonArray recursiveCourses(JsonArray rules, JsonArray tempCourses) throws IOException {


        for(var rule : rules) {
            if (rule.getAsJsonObject().get("type").getAsString().equals("CourseUnitRule")) {
                if (!tempCourses.contains(rule.getAsJsonObject())) {
                    tempCourses.add(rule.getAsJsonObject());

                }

            }else if(rule.getAsJsonObject().get("type").getAsString().equals("CompositeRule")) {
                tempCourses = recursiveCourses(rule.getAsJsonObject().get("rules").getAsJsonArray(),tempCourses);

            } else if(rule.getAsJsonObject().get("type").getAsString().equals("ModuleRule")) {
                var moduleUrl = createModuleURL(rule.getAsJsonObject().get("moduleGroupId").getAsString());
                URL url = new URL(moduleUrl);
                var moduleObject = createModuleObject(url);
                if(moduleObject.get("type").getAsString().equals("ModuleRule")) {
                    tempCourses = recursiveCourses(moduleObject.getAsJsonObject().getAsJsonArray(),tempCourses);
                } else if(moduleObject.get("type").getAsString().equals("CompositeRule")){
                    tempCourses = recursiveCourses(moduleObject.get("rules").getAsJsonArray(),tempCourses);
                } else if(moduleObject.get("type").getAsString().equals("StudyModule")){

                    tempCourses = recursiveCourses(checkModuleObject(moduleObject),tempCourses);

                } else {
                    tempCourses = recursiveCourses(checkModuleObject(moduleObject),tempCourses);
                }

            } else if(rule.getAsJsonObject().get("type").getAsString().equals("AnyModuleRule")) {
                continue;
            } else if(rule.getAsJsonObject().get("type").getAsString().equals("AnyCourseUnitRule")){
                continue;
            } else {

                if (rule.getAsJsonObject().get("rule").getAsJsonObject().get("type").getAsString().equals("CompositeRule")) {
                    tempCourses = recursiveCourses(rule.getAsJsonObject().get("rule").getAsJsonObject().get("rules").getAsJsonArray(), tempCourses);
                } else {
                    tempCourses = recursiveCourses(rule.getAsJsonObject().get("rule").getAsJsonObject().get("rule").getAsJsonObject().get("rules").getAsJsonArray(),tempCourses);
                }
            }
        }


        return tempCourses;


    }

    private JsonArray checkModuleObject(JsonObject moduleObject) {
        if(moduleObject.get("rule").getAsJsonObject().get("type").getAsString().equals("CompositeRule")) {
            return moduleObject.get("rule").getAsJsonObject().get("rules").getAsJsonArray();
        } else {
            return moduleObject.get("rule").getAsJsonObject().get("rule").getAsJsonObject().get("rules").getAsJsonArray();
        }
    }

    private JsonArray recursiveDegreeModule(JsonObject degreeObject, JsonArray moduleArray) {
        if (degreeObject.getAsJsonObject().get("type").getAsString().equals("CompositeRule")) {
            moduleArray = degreeObject.getAsJsonObject().get("rules").getAsJsonArray();
            return moduleArray;
        } else {
            moduleArray = recursiveDegreeModule(degreeObject.getAsJsonObject().get("rule").getAsJsonObject(), moduleArray);
        }
        return moduleArray;
    }






}

