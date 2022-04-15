package fi.tuni.prog3.sisu;

import java.util.List;

public class StudyModule {


        public String getStudyModuleName() {
            return studyModuleName;
        }

        public String getModuleCode() {
            return studyModuleCode;
        }

        public List<Course> getCourses() {
            return courses;
        }

        private final String studyModuleName;
        private final String studyModuleCode;
        private final List<Course> courses;


        public StudyModule(String moduleName, String moduleCode, List<Course> courses) {
            this.studyModuleName = moduleName;
            this.studyModuleCode = moduleCode;
            this.courses = courses;
        }


    }



