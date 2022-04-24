package fi.tuni.prog3.sisu;

import java.util.ArrayList;

public class Student {
    private final String name;
    private final String studentNumber;
    private final int startingYear;
    private final int expectedEndYear;
    private Degree degree;
    private ArrayList<Attainment> attainments;
    private ArrayList<Course> selectedCourses;

    public Student(String name, String studentNumber, int startingYear, Degree degree) {
        this.name = name;
        this.studentNumber = studentNumber;
        this.startingYear = startingYear;
        this.expectedEndYear = startingYear + 3;
        this.degree = degree;

        attainments = new ArrayList<Attainment>();
        selectedCourses = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public int getStartingYear() {
        return startingYear;
    }

    public int getExpectedEndYear() {
        return expectedEndYear;
    }

    public Degree getDegree() {
        return degree;
    }

    public String getFirstName() {
        return name.split(" ")[0];
    }

    public String getLastName() {
        return name.substring(name.lastIndexOf(" ")+1);
    }

    public void changeDegree(Degree newDegree) {
        if(degree != newDegree) {
            degree = newDegree;
        } else {
            throw new IllegalArgumentException("Uusi tutkinto ei saa olla sama kuin vanha tutkinto!");
        }
    }

    public ArrayList<Attainment> getAttainments() {
        return attainments;
    }

    public void addAttainment(Attainment attainment) {
        attainments.add(attainment);
    }

    public ArrayList<Course> getRecommendedCourses() {
        return degree.getCourses();
    }

    public ArrayList<Course> getSelectedCourses() {
        return selectedCourses;
    }

    public String getEmailAddress() {
        if(getLastName().contains("'") && !getFirstName().contains("'")) {
            return String.format("%s.%s@tuni.fi", getFirstName().toLowerCase(), getLastName().toLowerCase().replace("'", ""));
        }
        if(getFirstName().contains("'") && !getLastName().contains("'")) {
            return String.format("%s.%s@tuni.fi", getFirstName().toLowerCase().replace("'", "")
                    , getLastName().toLowerCase());
        }
        if(getFirstName().contains("'") && getLastName().contains("'")) {
            return String.format("%s.%s@tuni.fi", getFirstName().toLowerCase().replace("'", "")
                    , getLastName().toLowerCase().replace("'", ""));
        }
        return String.format("%s.%s@tuni.fi", getFirstName().toLowerCase(), getLastName().toLowerCase());
    }
}
