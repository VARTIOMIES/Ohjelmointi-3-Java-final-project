package fi.tuni.prog3.sisu;

import java.util.ArrayList;

/**
 * This class stores information of an individual student
 */
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

    /**
     * Constructor
     * Precondition: degree has been created
     * Post-conditions: degree is not empty
     * @param name Name of the student
     * @param studentNumber Student number of the student
     * @param startingYear Starting year of the student
     * @param degree Degree of the student
     */
    public Student(String name, String studentNumber, int startingYear, Degree degree) {
        this.name = name;
        this.studentNumber = studentNumber;
        this.startingYear = startingYear;
        this.expectedEndYear = startingYear + 3;
        this.degree = degree;

        firstName = name.split(" ")[0];
        lastName = name.substring(name.lastIndexOf(" ")+1);
        attainments = new ArrayList<>();
    }

    /**
     * Gets the name of the student
     * @return name of the student
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the students student number
     * @return student number of the student
     */
    public String getStudentNumber() {
        return studentNumber;
    }

    /**
     * Gets the starting year of the student
     * @return starting year of the student
     */
    public int getStartingYear() {
        return startingYear;
    }

    /**
     * Gets the expected end year of the student
     * @return expected end year of the student
     */
    public int getExpectedEndYear() {
        return expectedEndYear;
    }

    /**
     * Gets the degree of the student
     * @return degree of the student
     */
    public Degree getDegree() {
        return degree;
    }

    /**
     * Gets the first name of the student
     * @return first name of the student
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the last name of the student
     * @return last name of the student
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the total credits of the student
     * @return credit amount
     */
    public int getTotalCredits(){
        int temp = 0;
        for (Attainment attainment : getAttainments()) {
            temp += attainment.getCourse().getCredits();
        }
        return temp;
    }

    /**
     * Gets the current degree progress
     * @return degree progress
     */
    public double getDegreeProgress(){
        int i = getTotalCredits();
        int k = degree.getCreditsMin();
        if(k > 0) {
            return (double) i / k;
        } else {
            return 0;
        }
    }

    /**
     * Changes the degree of the student
     * @param newDegree changed degree
     */
    public void changeDegree(Degree newDegree) {
        if(degree != newDegree) {
            degree = newDegree;
        } else {
            throw new IllegalArgumentException("Uusi tutkinto ei saa olla sama kuin vanha tutkinto!");
        }
    }

    public String progressString() {
        return String.format("%d/%d", getTotalCredits(), degree.getCreditsMin());
    }

    
    public ArrayList<Attainment> getAttainments() {
        return attainments;
    }

    /**
     * Adds the attainment to the attainments ArrayList
     * @param attainment wanted attainment to be added
     */
    public void addAttainment(Attainment attainment) {
        attainments.add(attainment);
    }

    /**
     * Sets the amount of same named students
     * @param howMany amount of same named students
     */
    public void setSameNamed(int howMany) {
        this.sameNamed = howMany;
    }

    /**
     * Calculates the mean for the courses that the student has completed
     * @return Returns the mean in string format
     */
    public String getMean() {
        if(attainments.size() > 0) {
            float a = 0;
            for(var att : attainments) {
                a += att.getGrade();
            }
            return String.format("   %.2f", a/attainments.size());
        }
        return "   -";
    }

    /**
     * Generates the email address of the student
     * @return email address as a string
     */
    public String getEmailAddress() {
        String emailAddress = "";
        String names = "";
        if(firstName.equals(lastName)) {
            names = "%s";
        } else {
            names = "%s.%s";
        }

        // Checking for char "'".
        if(firstName.contains("'") || lastName.contains("'")) {
            if(lastName.contains("'")) {
                emailAddress = String.format(names, firstName.toLowerCase(), lastName.toLowerCase().replace("'", ""));
            } else {
                emailAddress = String.format(names, firstName.toLowerCase().replace("'", "")
                        , lastName.toLowerCase());
            }
        }
        else if(firstName.contains("'") && lastName.contains("'")) {
            emailAddress = String.format(names, firstName.toLowerCase().replace("'", "")
                    , lastName.toLowerCase().replace("'", ""));
        } else {
            emailAddress = String.format(names, firstName.toLowerCase(), lastName.toLowerCase());
        }

        if(sameNamed >= 2) {
            emailAddress += sameNamed;
        }

        return emailAddress + "@tuni.fi";
    }
}
