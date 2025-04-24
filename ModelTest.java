package backend;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Test;

public class ModelTest {

	@Test
	public void test() {
		AccountStorage ac = new AccountStorage();
		Database db = null;
		try {
			db = new Database(ac);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ac.setAccount("Fatih", "1234",db, false);
		Model model = new Model(db, new User("dlee"));
		model.getGradedAssignments("CSC252");
		model.getCategories("CSC252", "syllabus_quiz", "quiz");
		model.getCompletedCoursesStudent("dlee");
		model.getCompletedCoursesTeacher("jjohnson");
		model.getCurrentUsersName();
		model.getCurCoursesStudent("dlee");
		model.getCurrentUsersName();
		model.addAssignment("CSC252", "syllabus_quiz", "quiz");
		model.addStudent("dlee", "CSC252");
		model.assignFinalGrade("CSC252");
		model.addGradeForAssignment("dlee", "syllabus_quiz", 0, "CSC252");
		model.canAssignFinalGrades("CSC252");
		ArrayList<Double> weights = new ArrayList<>();
		weights.add(0.1);
		weights.add(0.2);
		weights.add(0.3);
		weights.add(0.4);
		ArrayList<Integer> drops = new ArrayList<>();
		drops.add(1);
		model.calculateClassAverage(1, "CSC252", weights, drops);
		model.calculateClassAverage(0, "CSC252", weights, drops);
		model.checkCompleted("CSC252");
		model.calculateStats("CSC252", "midterm");
		// model.calculateGPA("dlee");
		// model.calculateStudentCurAverage("dlee");
		// model.getEnrolledStudents("CSC252");

	}

}
