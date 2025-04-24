package backend;

import static org.junit.Assert.*;

import org.junit.Test;

public class AssignmentTest {

	@Test
	public void test() {
		Assignment a = new Assignment("syllabus_quiz", AssignmentType.QUIZ);
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
		
		// a.getStudentGradeExists("dlee");
	}

}
