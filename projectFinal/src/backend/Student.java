package backend;

import java.util.HashMap;

public class Student extends User {
	private String firstName;
	private String lastName;
	private HashMap<String, Double> studentAverageGrades; // String is the class's name
	private enum Grades {A, B, C, D, E, F};
	private HashMap<String, Grades> studentGradeLetters; // Sadly not automatically updated
	
	public Student(String first, String last, String user) {
		super(user);
		this.firstName = first;
		this.lastName = last;
	}
	
	// This is meant for IMPORTING
	protected Student(String first, String last, String user, HashMap<String, Course> courseList) {
		super(user, courseList);
		this.firstName = first;
		this.lastName = last;
	}
	
	public String getFirstName(){
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
}