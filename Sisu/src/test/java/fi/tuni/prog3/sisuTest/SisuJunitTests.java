package fi.tuni.prog3.sisuTest;/*
    @author - Onni Merila , onni.merila@tuni.fi , H299725
 */

import com.google.gson.JsonArray;
import fi.tuni.prog3.sisu.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class SisuJunitTests {

    @ParameterizedTest
    @MethodSource("courseArgumentProvider")
    public void testAttainmentGetters(String name,String code,int credits){
        int testGrade = 0;
        Course testCourse = new Course(name,code,credits);

        Attainment testAttainment = new Attainment(
                testCourse,
                testGrade
        );
        assertEquals(testCourse,testAttainment.getCourse());
        assertEquals(testGrade,testAttainment.getGrade());
    }

    @ParameterizedTest
    @MethodSource("courseArgumentProvider")
    public void testCourseGetters(String name,String code,int credits){
        Course testCourse = new Course(name,code,credits);

        assertEquals(name,testCourse.getCourseName());
        assertEquals(code,testCourse.getCourseCode());
        assertEquals(credits,testCourse.getCredits());

    }

    static Stream<Arguments> courseArgumentProvider(){
        return Stream.of(
                arguments("Koodaus","COMP.CS",1),
                arguments("TestiNimi","TestiKoodi",3),
                arguments("Mankeloinnin alkeet","LOS.MANG.els.100",5)
        );

    }

    @ParameterizedTest
    @MethodSource("degreeArgumentProvider")
    public void testDegreeGetters(String id,String name,int credits){

        Degree testDegree = new Degree(id,name,credits);

        assertEquals(id,testDegree.getGroupId());
        assertEquals(name,testDegree.getName());
        assertEquals(credits,testDegree.getCreditsMin());

    }

    @ParameterizedTest
    @MethodSource("degreeArgumentProvider")
    public void testDegreeModules(String id,String name,int credits) {

        Degree testDegree = new Degree(id, name, credits);
        assertThrows(IOException.class, testDegree::readAPI);

    }


    static Stream<Arguments> degreeArgumentProvider(){
        return Stream.of(
                arguments("12345","TestiNimi",180),
                arguments("ASdasf","_Asd-ASD",0),
                arguments("0000","LEzgo",200)
        );
    }


    @Test
    public void testModules(){

        String moduleName = "Testi";
        com.google.gson.JsonArray moduleRules = new JsonArray();
        ArrayList<Course> courses = new ArrayList<>();
        Course course1 = new Course("","",5);
        courses.add(course1);
        StudyModule testModule = new StudyModule(moduleName,moduleRules,courses);
        Course course2 = new Course("2","",2);
        testModule.setCourses(course2);

        assertEquals(moduleName,testModule.getStudyModuleName());
        assertEquals(moduleRules,testModule.getModuleRules());
        assertEquals(courses.size(),testModule.getCourses().size());
        for (int i = 0;i<courses.size();i++){
            assertEquals(courses.get(i).getCourseName(),testModule.getCourses().get(i).getCourseName());
            assertEquals(courses.get(i).getCourseCode(),testModule.getCourses().get(i).getCourseCode());
        }
        assertEquals(courses,testModule.getCourses());

    }

    @ParameterizedTest
    @MethodSource("studentArgumentProvider")
    public void testStudentGetters(String name,String number,int year,Degree degree){
        Student testStudent = new Student(name,number,year,degree);

        assertEquals(name,testStudent.getName());
        assertEquals(number,testStudent.getStudentNumber());
        assertEquals(year,testStudent.getStartingYear());
        assertEquals(degree,testStudent.getDegree());
    }

    @ParameterizedTest
    @MethodSource("studentArgumentProvider")
    public void testStudentAddAttainment(String name,String number,int year,Degree degree){
        Student testStudent = new Student(name,number,year,degree);
        int testGrade = 2;
        String testCourseName = "name";
        String testCourseCode = "code";
        int testCredits = 1;
        ArrayList<Attainment> testList= new ArrayList<>();


        Course testCourse = new Course(testCourseName,testCourseCode,testCredits);
        Attainment testAttainment = new Attainment(testCourse,testGrade);
        testList.add(testAttainment);

        testStudent.addAttainment(testAttainment);

        ArrayList<Attainment> studentAttainments = testStudent.getAttainments();

        for (int i = 0;i<testList.size();i++){
            assertEquals(testList.get(i),studentAttainments.get(i));
        }


    }

    static Stream<Arguments> studentArgumentProvider(){
        return Stream.of(
                arguments("Pertti Perälä","123512512",2010,new Degree("id","name",200)),
                arguments("Jarmo Juoppo","H1923011",1950,new Degree("id","name",200)),
                arguments("Jeesus Kristus","66666666",0,new Degree("id","name",999))
        );
    }


}
