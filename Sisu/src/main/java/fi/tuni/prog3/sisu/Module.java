package fi.tuni.prog3.sisu;

import java.util.ArrayList;

public class Module {

    private final String moduleName;
    private final String moduleCode;
    private ArrayList<StudyModule> studyModules;

    public Module(String moduleName, String moduleCode, ArrayList<StudyModule> studyModules) {
        this.moduleName = moduleName;
        this.moduleCode = moduleCode;
        this.studyModules = studyModules;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public ArrayList<StudyModule> getStudyModules() {
        return studyModules;
    }

    public void setStudyModules(StudyModule studyModule) {
        this.studyModules.add(studyModule);
    }
}

