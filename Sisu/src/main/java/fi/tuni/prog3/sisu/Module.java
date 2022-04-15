package fi.tuni.prog3.sisu;

import java.util.List;

public class Module {
    public String getModuleName() {
        return moduleName;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public List<Course> getCourses() {
        return courses;
    }

    private final String moduleName;
    private final String moduleCode;
    private final List<Course> courses;


    public Module(String moduleName, String moduleCode, List<Course> courses) {
        this.moduleName = moduleName;
        this.moduleCode = moduleCode;
        this.courses = courses;
    }


}

