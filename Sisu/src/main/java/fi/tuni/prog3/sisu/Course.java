package fi.tuni.prog3.sisu;

/**
 * This class stores information about specific course
 */
public class Course {
    private final String courseName;
    private final String courseCode;
    private final int credits;

    /**
     * Constructor
     * @param courseName Stores the name of the course
     * @param courseCode Stores the code of the course
     * @param credits Storest the amoun of credits
     */
    public Course(String courseName, String courseCode, int credits) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.credits = credits;
    }


    /**
     * Gets the course name
     * @return course name
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * Gets the course code
     * @return course code
     */
    public String getCourseCode() {
        return courseCode;
    }

    /**
     * Gets the amount of credits
     * @return credit amount
     */
    public int getCredits() {
        return credits;
    }
}
