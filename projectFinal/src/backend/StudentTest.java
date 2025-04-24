package backend;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;


public class StudentTest {

    @Test
    public void test() {
        Student s = new Student("Ding", "Lee", "dlee");
        HashMap<String, FinalGrade> studentGradeLetters = new HashMap<>();
        studentGradeLetters.put("dlee", FinalGrade.A);
        studentGradeLetters.put("dlee", FinalGrade.B);
        studentGradeLetters.put("dlee", FinalGrade.C);
        studentGradeLetters.put("dlee", FinalGrade.D);
        ArrayList<String> courseNames = new ArrayList<>();
        courseNames.add("CSC252");
        s.setCourseNames(courseNames);
        HashMap<String, Double> studentAverageGrades = new HashMap<>();
        studentAverageGrades.put("dlee", 89.8);
        s.setStudentGradeLetters(studentGradeLetters);
        s.setStudentAverageGrades(studentAverageGrades);
        s.calculateGPA();
        s.calculateCurAverage();
        s.getCompletedCourses();
        s.getFirstName();
        s.getLastName();
        s.getPrintFormatted();
        s.toString();
        s.getCourseAverage("CSC252");
        s.setStudentGradeLetters(studentGradeLetters);
        s.updateStudentGradeLetters("CSC252", FinalGrade.A);
        s.updateStudentAverageGrades("CSC252", 80.2);
        s.updateStudentAverageGrades("dlee", 89.9);
        s.getStudentAverage("CSC252");
    }

}