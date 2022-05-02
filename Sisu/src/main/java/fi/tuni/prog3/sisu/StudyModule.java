package fi.tuni.prog3.sisu;

import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

/**
 * This class stores information about individual study module
 */
public class StudyModule {

    private final String studyModuleName;
    private final JsonArray moduleRules;
    private final ArrayList<Course> courses;

    /**
     * Constructor
     * Precondition: courses is not empty
     * Post-condition: courses don't change
     * @param moduleName Name of the study module
     * @param moduleRules JsonArray of module rules
     * @param courses ArrayList of courses
     */
    public StudyModule(String moduleName, JsonArray moduleRules, ArrayList<Course> courses) {
        this.studyModuleName = moduleName;
        this.moduleRules = moduleRules;
        this.courses = courses;
    }

    /**
     * Gets the name of the study module
     * @return name of the study module
     */
    public String getStudyModuleName() {
        return studyModuleName;
    }

    /**
     * Gets the list of the courses
     * @return ArrayList of courses
     */
    public List<Course> getCourses() {
        return courses;
    }

    /**
     * Gets the module rules in an ArrayList
     * @return
     */
    public JsonArray getModuleRules() {
        return moduleRules;
    }

    /**
     * Adds the wanted course to the courses ArrayList
     * @param course wanted course to be added
     */
    public void setCourses(Course course) {
        this.courses.add(course);
    }

}



