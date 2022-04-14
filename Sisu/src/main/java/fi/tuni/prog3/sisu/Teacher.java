package fi.tuni.prog3.sisu;

import java.util.ArrayList;

public class Teacher {
    private final String name;
    private final ArrayList<Course> taughtCourses;

    public Teacher(String name) {
        this.name = name;

        taughtCourses = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<Course> getTaughtCourses() {
        return taughtCourses;
    }
}
