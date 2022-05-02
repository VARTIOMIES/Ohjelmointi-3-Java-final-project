package fi.tuni.prog3.sisu;

import java.util.ArrayList;

/**
 * This class stores information about modules
 */
public class Module {

    private final String moduleName;
    private final String moduleCode;
    private ArrayList<StudyModule> studyModules;

    /**
     * Constructor
     * Precondition: studyModules is not empty
     * Post-condition: studyModules don't change
     * @param moduleName Module name
     * @param moduleCode Module code
     * @param studyModules Arraylist of study modules
     */
    public Module(String moduleName, String moduleCode, ArrayList<StudyModule> studyModules) {
        this.moduleName = moduleName;
        this.moduleCode = moduleCode;
        this.studyModules = studyModules;
    }

    /**
     * Gets the module name
     * @return module name
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * Gets the module code
     * @return module name
     */
    public String getModuleCode() {
        return moduleCode;
    }

    /**
     * Gets the ArrayList which stores study modules
     * @return study module ArrayList
     */
    public ArrayList<StudyModule> getStudyModules() {
        return studyModules;
    }

    /**
     * Adds the wanted study module to the objects study module ArrayList
     * @param studyModule wanted study module
     */
    public void setStudyModules(StudyModule studyModule) {
        this.studyModules.add(studyModule);
    }
}

