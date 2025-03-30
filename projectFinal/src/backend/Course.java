package backend;

import java.util.ArrayList;
import java.util.HashMap;

public class Course {
	private HashMap<Integer, Assignment> assignmentList; // The integer is the hash of the Assignment.
	private String courseName;
	// No duplicate assignments! ... Why would you have a duplicate, anyways?
	private HashMap<String, Student> studentList; // The string is the username
	private Boolean completed;
	private HashMap<String, ArrayList<Student>> groupList; // String is "group name"
	
	public Course(String courseName) {
		this.courseName = courseName;
		assignmentList = new HashMap<Integer, Assignment>();
		studentList = new HashMap<String, Student>();
		groupList = new HashMap<String, ArrayList<Student>>();
	}
	
	public String getCourseName() {
		return courseName;
	}
	
	// there might be other getters it depends on the functionality of Model methods
}
