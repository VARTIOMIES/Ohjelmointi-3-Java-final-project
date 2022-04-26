package fi.tuni.prog3.sisu;

import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

public class StudyModule {


        public String getStudyModuleName() {
            return studyModuleName;
        }

        public List<Course> getCourses() {
            return courses;
        }

        private final String studyModuleName;

    public JsonArray getModuleRules() {
        return moduleRules;
    }

    private JsonArray moduleRules;
        private ArrayList<Course> courses;


        public StudyModule(String moduleName, JsonArray moduleRules, ArrayList<Course> courses) {
            this.studyModuleName = moduleName;
            this.moduleRules = moduleRules;
            this.courses = courses;
        }


        public void setCourses(Course course) {
            this.courses.add(course);
        }



    }



