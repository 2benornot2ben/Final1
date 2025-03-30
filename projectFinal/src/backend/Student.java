package backend;

import java.util.HashMap;
import java.util.HashSet;

public class Student extends User {
	private String firstName;
	private String lastName;
	private HashMap<String, Double> studentAverageGrades; // String is meant to be classes
	private enum Grades {A, B, C, D, E, F};
	private HashMap<String, Grades> studentGradeLetters; // Sadly not automatically updated
	
	public Student(String first, String last, String user) {
		this.firstName = first;
		this.lastName = last;
		username = user;
		courseList = new HashSet<Course>();
	}
	
	public String getFirstName(){
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public HashSet<Course> getCourseList(){
		//this should return copy of course list
	}
}
