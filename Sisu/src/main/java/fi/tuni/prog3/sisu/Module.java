package fi.tuni.prog3.sisu;

import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

public class Module {
    public String getModuleName() {
        return moduleName;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public ArrayList<StudyModule> getStudyModules() {
        return studyModules;
    }

    private final String moduleName;
    private final String moduleCode;

    public void setStudyModules(StudyModule studyModule) {
        this.studyModules.add(studyModule);
    }

    private ArrayList<StudyModule> studyModules;


    public Module(String moduleName, String moduleCode, ArrayList<StudyModule> studyModules) {
        this.moduleName = moduleName;
        this.moduleCode = moduleCode;
        this.studyModules = studyModules;
    }




}

