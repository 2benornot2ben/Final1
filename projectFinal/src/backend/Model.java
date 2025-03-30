package backend;

import java.util.ArrayList;
import java.util.HashMap;

public class Model {
	private ArrayList<Course> fullCourseList; // ArrayList may make sense here; may not. Decide later.
	private HashMap<String, Student> studentList; // ONLY students, string is username.
	// I feel like we'd need more...
	private boolean isTeacher;
	
	
	// definetely needs a parameter but i dont know yet.
	public Model() {
		fullCourseList = new ArrayList<Course>();
		studentList = new HashMap<String, Student>();
	}
	
	
	/*
	 *  All the methods are void right now, but if they return something change it to something`s type.
	 *  This might not be final layout
	 *  You can add helper methods
	 *  
	 *  I did not include parameters because i not sure what will they be, but i included my guesses. 
	 */
	
	
	
	
	
	public void getGradedAssignments() {
		// for students
		// returns graded assignments
		
		// ! watch for escaping reference
	}
	
	public void getUngradedAssignments() {
		// for students & teachers
		// returns ungraded assignments
		
		// ! watch for escaping reference
	}
	
	public void getCourseAverage() {
		// for students
		// returns average of the course based on the graded assignments
		
		// ! watch for escaping reference
	}
	
	public void calculateGPA() {
		// for students
		// calculates the GPA based on the final grades of completed tasks.
		// return double
	}
	
	public void addAssignment() {
		// for teachers
		// adds an assignment to a course
		
		// assignment, course should come as a parameter (ig)
	}
	
	public void removeAssignment() {
		// for teachers
		// removes an assignment from a course
		
		// assignment, course should come as a parameter (ig)
	}
	
	public void addStudent() {
		// for teachers
		// adds a student to a course
				
		// student, course should come as a parameter (ig)
	}
	
	public void removeStudent() {
		// for teachers
		// removes a student from a course
				
		// student, course should come as a parameter (ig)
	}
	
	public void importStudents() {
		// for teachers
		// adds students to a course
				
		// students (as a list?), course should come as a parameter (ig)
	}
	
	public void getEnrolledStudents() {
		// for teachers
		// returns student list who are enrolled in a course
	
		// course should come as a parameter (ig)
		
		// ! watch for escaping reference
	}
	
	public void addGradeForAssignment() {
		// for teachers
		// adds a grade for an assignment
		
		// student, assignment, course should come as a parameter (ig)
		
		
		// not sure how to implement this function (do we add the same grade to anyone or one at a time?)
	}
	
	public void calculateStats() {
		// for teachers
		// calculates the averages and the medians on assignment 
		// returns a String message e.g. "Average on Project1 is 98% and median is 97%"
		
		// assignment, course should come as a parameter (ig)
		
	}
	
	public void calculateStudentAverage() {
		// for teachers
		// calculates student average on a course from graded assignments
		// returns double.
		
		// this method might be the same as getCourseAverage() if so then remove one of them.
		
		// student, course should come as a parameter (ig)
	}
	
	
	/*
	 * There are 4 sorts and each has a separate method. We can simplify it and make it as one method.
	 */
	
	public void sortByFirstName() {
		// for teachers
		// sorts students by their first name
		
		// i dont know if this method should return something.
		// course should come as a parameter (ig)
	}
	
	public void sortByLastName() {
		// for teachers
		// sorts students by their last name
				
		// i dont know if this method should return something.
		// course should come as a parameter (ig)
	}
	
	public void sortByUserName() {
		// for teachers
		// sorts students by their username
				
		// i dont know if this method should return something.
		// course should come as a parameter (ig)
	}
	
	public void sortByGrades() {
		// for teachers
		// sorts students by their grades on an assignment
				
		// i dont know if this method should return something.
		// assignment, course should come as a parameter (ig)
	}
	
	public void putInGroups() {
		// for teachers
		// creates groups and puts students into them.
		// return HashMap<String, ArrayList<Student>> String is group name.
				
		// i dont know if this method should return something.
		// course should come as a parameter (ig)
	}
	
	public void assignFinalGrade() {
		// for teachers
		// calculates a Final Grade
		
		// course should come as a parameter (ig)
	}
	
	public void calculateClassAverage(int option) { 
		// for teachers
		//Option 1: Final Grade = Total Points Earned/Total Points Possible. Basically, all
		//the points from all the assignments are added up. (This is how I do it in CSc 335.)
		//Option 2: The final grade is based on categories and percentages.
		
		// course should come as a parameter (ig)
		
	}
	
	public void setUpCategories() {
		// for teachers
		// In a course that uses categories, there should be the option to “drop” a certain number of
		// assignments with the lowest grade and to assign weights to the different categories.
		
		// course should come as a parameter (ig)
	}
}
