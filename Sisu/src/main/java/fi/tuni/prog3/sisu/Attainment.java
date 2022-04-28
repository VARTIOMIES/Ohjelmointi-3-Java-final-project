package fi.tuni.prog3.sisu;

public class Attainment {
    private final Course course;
    private final Student student;
    private final int grade;

    public Attainment(Course course, Student student, int grade) {
        this.course = course;
        this.student = student;
        this.grade = grade;
    }

    public Course getCourse() {
        return course;
    }

    public Student getStudent() {
        return student;
    }

    public int getGrade() {
        return grade;
    }
}
