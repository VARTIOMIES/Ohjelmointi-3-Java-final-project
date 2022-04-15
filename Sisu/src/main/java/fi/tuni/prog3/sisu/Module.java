package fi.tuni.prog3.sisu;

import com.google.gson.JsonArray;

import java.util.List;

public class Module {
    public String getModuleName() {
        return moduleName;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public JsonArray getStudyModules() {
        return studyModules;
    }

    private final String moduleName;
    private final String moduleCode;
    private final JsonArray studyModules;


    public Module(String moduleName, String moduleCode, JsonArray studyModules) {
        this.moduleName = moduleName;
        this.moduleCode = moduleCode;
        this.studyModules = studyModules;
    }


}

