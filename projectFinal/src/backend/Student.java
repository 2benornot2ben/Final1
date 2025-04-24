package backend;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Student extends User {
	private String firstName;
	private String lastName;
	private HashMap<String, Double> studentAverageGrades; // This should be all the current coureses
	private HashMap<String, FinalGrade> studentGradeLetters; // This should be the past courses

	// if a letter grade is not assigned, it should be a cur course
	// if a letter grade is assigned, it should be a past course


	
	public Student(String first, String last, String user) {
		super(user);
		this.firstName = first;
		this.lastName = last;
		studentAverageGrades = new HashMap<String, Double>();
		studentGradeLetters = new HashMap<String, FinalGrade>();
	}
	
	public String getFirstName(){
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	protected void updateStudentGradeLetters(String courseName, FinalGrade letter) {
		studentGradeLetters.put(courseName, letter);
	}
	
	protected void updateStudentAverageGrades(String courseName, Double grade) {
		if(studentAverageGrades.containsKey(courseName)) {
			studentAverageGrades.put(courseName, studentAverageGrades.get(courseName) + grade); 
		} else {
			studentAverageGrades.put(courseName, grade);
		}
	}
	
	public double calculateGPA() {
		double totalPointsEarned = 0;
		double totalPointsPossible = courseMap.size();
		for (String key : studentGradeLetters.keySet()) {
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
	private void setStudentGradeLetters(HashMap<String, FinalGrade> studentGradeLetters) {
		this.studentGradeLetters = studentGradeLetters;
	}
}