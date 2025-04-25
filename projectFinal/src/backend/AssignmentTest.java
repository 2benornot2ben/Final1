package backend;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

public class AssignmentTest {

	@Test
	public void test() {
		Assignment a = new Assignment("syllabus_quiz", AssignmentType.QUIZ);
		ArrayList<Double> grades = new ArrayList<>();
        grades.add(90.0);
        HashMap<String, Double> idToGrade = new HashMap<>();
        idToGrade.put("dlee", 10.0);
        idToGrade.put("Fatih", null);
        a.setidToGrade(idToGrade);
		a.getAllGrades();
		a.getAssignmentName();
		a.getIdToGrade();
		a.getMaxGrade();
		a.getStudentGrade("Fatih");
		a.graded();
		a.isGraded();
		a.gradeStudent("CSC252", new Student("Ding", "Lee", "dlee"), 0);
		a.setType(AssignmentType.FINAL);
		a.setMaxGrade(0);
		a.getType();
		
		a.setidToGrade(null);
		a.setAssignmentName("quiz1");
		a.setGraded(false);
		Assignment aCopy = new Assignment();
		
		Assignment aCopy2 = new Assignment(new Assignment());
	}

}