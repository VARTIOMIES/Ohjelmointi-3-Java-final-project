package fi.tuni.prog3.sisu;

import java.util.List;

public class StudyModule {


        public String getStudyModuleName() {
            return studyModuleName;
        }

        public List<Course> getCourses() {
            return courses;
        }

        private final String studyModuleName;
        private final List<Course> courses;


        public StudyModule(String moduleName, List<Course> courses) {
            this.studyModuleName = moduleName;
            this.courses = courses;
        }


    }



