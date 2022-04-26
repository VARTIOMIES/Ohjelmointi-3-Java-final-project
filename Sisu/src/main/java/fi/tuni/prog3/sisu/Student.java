package fi.tuni.prog3.sisu;

import java.io.IOException;
import java.util.ArrayList;

public class Student {
    private final String name;
    private final String studentNumber;
    private final int startingYear;
    private final int expectedEndYear;
    private Degree degree;
    private final String firstName;
    private final String lastName;
    private int sameNamed;
    private ArrayList<Attainment> attainments;
    private ArrayList<Course> selectedCourses;

    public Student(String name, String studentNumber, int startingYear, Degree degree) {
        this.name = name;
        this.studentNumber = studentNumber;
        this.startingYear = startingYear;
        this.expectedEndYear = startingYear + 3;
        this.degree = degree;

        firstName = name.split(" ")[0];
        lastName = name.substring(name.lastIndexOf(" ")+1);
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
        return firstName;
    }

    public String getLastName() {
        return lastName;
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


    public ArrayList<Course> getSelectedCourses() {
        return selectedCourses;
    }

    public void setSameNamed(int howMany) {
        this.sameNamed = howMany;
    }

    public String getEmailAddress() {
        String emailAddress = "";

        // Checking for char "'".
        if(firstName.contains("'") || lastName.contains("'")) {
            if(lastName.contains("'")) {
                emailAddress = String.format("%s.%s", firstName.toLowerCase(), lastName.toLowerCase().replace("'", ""));
            } else {
                emailAddress = String.format("%s.%s", firstName.toLowerCase().replace("'", "")
                        , lastName.toLowerCase());
            }
        }
        else if(firstName.contains("'") && lastName.contains("'")) {
            emailAddress = String.format("%s.%s", firstName.toLowerCase().replace("'", "")
                    , lastName.toLowerCase().replace("'", ""));
        } else {
            emailAddress = String.format("%s.%s", firstName.toLowerCase(), lastName.toLowerCase());
        }

        if(sameNamed >= 2) {
            emailAddress += sameNamed;
        }

        return emailAddress + "@tuni.fi";
    }
}
