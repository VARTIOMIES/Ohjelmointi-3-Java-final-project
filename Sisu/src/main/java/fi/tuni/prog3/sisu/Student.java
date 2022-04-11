package fi.tuni.prog3.sisu;

import java.util.ArrayList;

public class Student {
    private final String name;
    private final String studentNumber;
    private final int startingYear;
    private final Degree degree;
    private ArrayList<Attainment> attainments;

    public Student(String name, String studentNumber, int startingYear, Degree degree) {
        this.name = name;
        this.studentNumber = studentNumber;
        this.startingYear = startingYear;
        this.degree = degree;

        attainments = new ArrayList<Attainment>();
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

    public Degree getDegree() {
        return degree;
    }

    public ArrayList<Attainment> getAttainments() {
        return attainments;
    }

    public void addAttainment(Attainment attainment) {
        attainments.add(attainment);
    }
}
