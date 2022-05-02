package fi.tuni.prog3.sisu;

public class Attainment {
    private final Course course;
    private final int grade;

    public Attainment(Course course, int grade) {
        this.course = course;
        this.grade = grade;
    }

    public Course getCourse() {
        return course;
    }

    public int getGrade() {
        return grade;
    }
}
