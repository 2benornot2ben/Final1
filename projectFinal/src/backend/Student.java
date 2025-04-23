package backend;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Student extends User {
	private String firstName;
	private String lastName;
	private HashMap<String, Double> studentAverageGrades; // This should be all the current coureses
	private enum Grades {A, B, C, D, E, F};
	private HashMap<String, Grades> studentGradeLetters; // This should be the past courses

	// if a letter grade is not assigned, it should be a cur course
	// if a letter grade is assigned, it should be a past course


	
	public Student(String first, String last, String user) {
		super(user);
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

	@JsonIgnore
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

	@JsonIgnore
	public double getStudentAverage(String courseName) {
		return studentAverageGrades.get(courseName);
	}
	
	@JsonIgnore
	public String getPrintFormatted() {
		return firstName + " " + lastName + " (" + username + ")";
	}
	
	@JsonIgnore
	public String toString() {
		return this.getPrintFormatted();
	}
	
	// JSON METHODS
	// As in, we don't use these, but the json needs them to exist...
	private Student() {};
	
	@JsonSetter
	private void setStudentAverageGrades(HashMap<String, Double> studentAverageGrades ) {
		this.studentAverageGrades = studentAverageGrades;
	}
	
	@JsonSetter
	private void setStudentGradeLetters(HashMap<String, Grades> studentGradeLetters) {
		this.studentGradeLetters = studentGradeLetters;
	}
}