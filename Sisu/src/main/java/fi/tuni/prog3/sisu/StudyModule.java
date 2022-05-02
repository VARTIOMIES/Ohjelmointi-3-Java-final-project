package fi.tuni.prog3.sisu;

import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

public class StudyModule {

    private final String studyModuleName;
    private final JsonArray moduleRules;
    private final ArrayList<Course> courses;

    public StudyModule(String moduleName, JsonArray moduleRules, ArrayList<Course> courses) {
        this.studyModuleName = moduleName;
        this.moduleRules = moduleRules;
        this.courses = courses;
    }

    public String getStudyModuleName() {
        return studyModuleName;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public JsonArray getModuleRules() {
        return moduleRules;
    }

    public void setCourses(Course course) {
        this.courses.add(course);
    }

}



