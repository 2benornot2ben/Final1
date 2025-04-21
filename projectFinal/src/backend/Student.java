package backend;

import java.util.HashMap;

public class Student extends User {
	private String firstName;
	private String lastName;
	
	// this is for the current courses
	private HashMap<String, Double> studentAverageGrades; // String courseName : double avgGrade
	private enum Grades {A, B, C, D, E, F};
	private HashMap<String, Grades> studentGradeLetters; // String courseName : enum grade

	// if a letter grade is not assigned, it should be a cur course
	// if a letter grade is assigned, it should be a past course


	
	public Student(String first, String last, String user) {
		super(user);
		this.firstName = first;
		this.lastName = last;
	}
	
	// This is meant for IMPORTING
	protected Student(String first, String last, String user, HashMap<String, Course> courseMap) {
		super(user, courseMap);
		this.firstName = first;
		this.lastName = last;
	}
	
	public String getFirstName(){
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	public double calculateGPA() {
		double totalPointsEarned = 0;
		double totalPointsPossible = 4*courseMap.size();
		for (String key : studentGradeLetters.keySet()) {
			if (studentGradeLetters.get(key) == Grades.A) {
				totalPointsEarned += 4;
			}
			else if (studentGradeLetters.get(key) == Grades.B) {
				totalPointsEarned += 3;
			}
			else if (studentGradeLetters.get(key) == Grades.C) {
				totalPointsEarned += 2;
			}
			else if (studentGradeLetters.get(key) == Grades.D) {
				totalPointsEarned += 1;
			}
		}
		return totalPointsEarned / totalPointsPossible;
	}

	public double getCourseAverage(String courseName) {
		return courseMap.get(courseName).getCourseAverage();
	}

	public double calculateCurAverage() {
		double totalAvg = 0;
		for (String key : studentAverageGrades.keySet()) {
			totalAvg += studentAverageGrades.get(key);
		}
		return totalAvg / studentAverageGrades.size();
	}

	public double getStudentAverage(String courseName) {
		return studentAverageGrades.get(courseName);
	}
	
	public String getPrintFormatted() {
		return firstName + " " + lastName + " (" + username + ")";
	}
	
	public String toString() {
		return this.getPrintFormatted();
	}
}
