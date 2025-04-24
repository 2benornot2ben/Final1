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
		ArrayList<String> courseNames = new ArrayList<>();
		courseNames.add("CSC252");
		s.setCourseNames(courseNames);
		HashMap<String, Double> studentAverageGrades = new HashMap<>();
		studentAverageGrades.put("dlee", 89.8);
		s.setStudentAverageGrades(studentAverageGrades);
		// s.calculateGPA();
		// s.calculateCurAverage();
		// s.getCompletedCourses();
		
	}

}
