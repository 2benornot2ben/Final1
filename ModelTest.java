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
		model.calculateGPA("dlee");
		model.calculateStudentCurAverage("dlee");
		model.getEnrolledStudents("CSC252");
		model.removeAssignment("CSC252", "syllabus_quiz", "quiz");
		model.canCalculateStudentCurAverage("CSC252", "dlee");
		model.toString();
		model.sortByFirstName("CSC252");
		model.sortByGrades("CSC252", "syllabus_quiz");
		model.sortByGrades("CSC252", "Hw1");
		model.sortByLastName("CSC252");
		model.sortByUserName("CSC252");
		model.setAssignmentGraded("CSC252", "Hw1");
		model.canCalculateStudentCurAverage("CSC252", "dlee");
		model.checkCompleted("CSC252");
		model.completeCourse("CSC252");
		model.equals(drops);
		model.equals(model);
		model.equals(null);
		model.getCurCoursesStudent("dlee");
		model.getCurCoursesTeacher("jjohnson");
		model.getIsTeacher();
		model.isSetUp("CSC252");
		model.getStudentGrade("dlee", "CSC252", "Hw1");
		model.getUngradedAssignments("CSC252");
		model.getUngradedAssignmentsUSER("CSC252", "dlee");
		model.getStudentGrade("dlee", "CSC252", "Hw1");
		
		model.canCreateGroups("CSC252", 2);
		model.canCreateGroups("CSC252", 20);
		
		model.putInGroups("CSC252", 2);

		model.addAssignment("CSC252", "Hw1", "HW");
		model.removeAssignment("CSC252", "Hw1", "HW");
		
		model.getStudentList("CSC252");
		
		model.getGradedAssignmentsUSER("CSC252", "dlee");

		model.getCourseAverage("CSC252", "dlee");

		model.removeStudent("sgupta", "CSC252");
		
		model.removeStudent("devbehruz", "CSC252");
		
		model.removeStudent("lwilson", "CSC252");
		
		model.addStudent("sgupta", "CSC252");
		
		model.canCalculateStudentCurAverage("CSC252", "sgupta");
		
		model.addAssignment("CSC252", "Hw2", "HW");
		
		model.calculateStats("CSC252", "Hw2");
		
		model.canCalculateStudentCurAverage("CSC252", "kpatel");
		
		model.assignFinalGrade("CSC252");

		model.calculateStats(null, "Hw1");
		
		model.sortByLastName(null);
		
		model.sortByUserName(null);
		
		model.checkCompleted("CSC252");
		
		
		model.addGradeForAssignment("jsmith", "Hw1", 50, "CSC335");
		model.addGradeForAssignment("mwillams", "Hw1", 50, "CSC335");

		model.addGradeForAssignment("jsmith", "Hw2", 60, "CSC335");
		model.addGradeForAssignment("mwillams", "Hw2", 50, "CSC335");

		
		
		model.addGradeForAssignment("jsmith", "Hw3", 30, "CSC335");
		model.addGradeForAssignment("mwillams", "Hw3", 50, "CSC335");

		
		model.addGradeForAssignment("jsmith", "Hw4", 20, "CSC335");
		model.addGradeForAssignment("mwillams", "Hw4", 50, "CSC335");

		
		
		model.addGradeForAssignment("jsmith", "Hw5", 10, "CSC335");
		model.addGradeForAssignment("mwillams", "Hw5", 50, "CSC335");


		
		model.calculateStats("CSC335", "Hw2");
		model.calculateStats("CSC252", "Hw3");


		model.sortByFirstName(null);
		
		model.calculateClassAverage(1, "CSC335", weights, drops);
		
		model.assignFinalGrade("CSC335");


	}

}
