package fi.tuni.prog3.sisu;

import com.google.gson.JsonElement;

public class Course {
    private final String courseName;
    private final String courseCode;
    private final int credits;

    public Course(String courseName, String courseCode, int credits) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.credits = credits;
    }



    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public int getCredits() {
        return credits;
    }
}
