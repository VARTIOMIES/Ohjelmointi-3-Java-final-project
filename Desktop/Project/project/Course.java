package project;

public class Course {
    private final String courseName;
    private final String courseCode;
    private final Teacher teacher;
    private final int credits;

    public Course(String courseName, String courseCode, Teacher teacher, int credits) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.teacher = teacher;
        this.credits = credits;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public int getCredits() {
        return credits;
    }
}
