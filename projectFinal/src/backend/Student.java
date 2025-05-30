/**************************************************************
 * Author: Davranbek Kadirimbetov, Benjamin Kanter,
 * 		   Fatih Bozdogan, & Behruz Ernazarov
 * Description: Simulates a student! Students are complicated,
 * as they want their firstname and last name. They also need
 * grades, and just about every way to interact with them.
 **************************************************************/

package backend;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Student extends User {
	/* Students! This stores a student, which is most of
	 * the bulk of the program, when it grows big (Small programs
	 * mostly have bulk in assignments). Students need to store
	 * a fair amount of information in order to have it readily
	 * accessible without upending the entire program every time
	 * we want to get a grade. */
	private String firstName;
	private String lastName;
	private HashMap<String, Double> studentAverageGrades; // This should be all the current courses
	private HashMap<String, FinalGrade> studentGradeLetters; // This should be the past courses
	
	public Student(String first, String last, String user) {
		/* Basic constructor, sets the names, initializes the hashmaps. */
		super(user);
		this.firstName = first;
		this.lastName = last;
		studentAverageGrades = new HashMap<String, Double>();
		studentGradeLetters = new HashMap<String, FinalGrade>();
	}
	
	protected Student(Student student) {
		/* Copy constructor - only copies over names, for that is all
		 * that we need this for. It also helps prevent peering into the code. */
		super(student.getUsername());
		this.firstName = student.getFirstName();
		this.lastName = student.getLastName();
		studentAverageGrades = new HashMap<String, Double>();
		studentGradeLetters = new HashMap<String, FinalGrade>();
		this.courseMap = new HashMap<String, Course>();
		for (Course cour : student.courseMap.values()) {
			courseMap.put(cour.getCourseName(), new Course(cour));
		}
	}
	
	public String getFirstName(){
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	protected void updateStudentGradeLetters(String courseName, FinalGrade letter) {
		/* Not to be confused with setStudentGradeLetters, this just adds 1 grade. */
		studentGradeLetters.put(courseName, letter);
	}
	
	protected void updateStudentAverageGrades(String courseName, Double grade) {
		/* Updates a grade! You can also edit a grade by adding or subtracting from it. */
		if(studentAverageGrades.containsKey(courseName)) {
			studentAverageGrades.put(courseName, studentAverageGrades.get(courseName) + grade); 
		} else {
			studentAverageGrades.put(courseName, grade);
		}
	}
	
	public double calculateGPA() {
		/* This calculates the GPA, can be called anytime for it.
		 * Notably, we say 4 = A, 3 = B, 2 = C, and 1 = D.
		 * Better not get an F! */
		double totalPointsEarned = 0;
		double totalPointsPossible = courseMap.size();
		for (String key : studentGradeLetters.keySet()) {
			// FinalGrade is in a different file
			if (studentGradeLetters.get(key) == FinalGrade.A) {
				totalPointsEarned += 4;
			}
			else if (studentGradeLetters.get(key) == FinalGrade.B) {
				totalPointsEarned += 3;
			}
			else if (studentGradeLetters.get(key) == FinalGrade.C) {
				totalPointsEarned += 2;
			}
			else if (studentGradeLetters.get(key) == FinalGrade.D) {
				totalPointsEarned += 1;
			}
		}
		return totalPointsEarned / totalPointsPossible;
	}

	@JsonIgnore
	public double getCourseAverage(String courseName) {
		return this.calculateCurAverage();
	}

	public double calculateCurAverage() {
		/* Another calculator, getting the current average.
		 * Does so by adding keys to a double. */
		double totalAvg = 0;
		for (String key : studentAverageGrades.keySet()) {
			totalAvg += studentAverageGrades.get(key);
		}
		return totalAvg / studentAverageGrades.size();
	}

	@JsonIgnore
	public double getStudentAverage(String courseName) {
		return studentAverageGrades.get(courseName);
	}
	
	@JsonIgnore
	public String getPrintFormatted() {
		/* A convenient way of printing things out, used in some sorts. */
		return firstName + " " + lastName + " (" + username + ")";
	}
	
	@JsonIgnore
	public String toString() {
		/* And a method for it for extra convenience. */
		return this.getPrintFormatted();
	}
	
	// JSON METHODS
	// As in, we don't use these, but the json needs them to exist...
	private Student() {};
	
	@JsonSetter
	protected void setStudentAverageGrades(HashMap<String, Double> studentAverageGrades ) {
		this.studentAverageGrades = studentAverageGrades;
	}
	
	@JsonSetter
	protected void setStudentGradeLetters(HashMap<String, FinalGrade> studentGradeLetters) {
		this.studentGradeLetters = studentGradeLetters;
	}
}