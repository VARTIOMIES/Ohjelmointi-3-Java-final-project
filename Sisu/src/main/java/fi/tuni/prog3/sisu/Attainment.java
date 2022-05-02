package fi.tuni.prog3.sisu;

/**
 * This class stores information about completed course
 */
public class Attainment {
    private final Course course;
    private final int grade;

    /**
     * Constructor
     * Precondition: course has been created
     * Post-conditions: course is not empty
     * @param course Name of the course
     * @param grade Grade of the completed course
     */
    public Attainment(Course course, int grade) {
        this.course = course;
        this.grade = grade;
    }

    /**
     * Gets the course
     * @return course object
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Gets the grade
     * @return course grade
     */
    public int getGrade() {
        return grade;
    }
}
