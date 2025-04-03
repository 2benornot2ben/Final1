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
		groupList = new HashMap<String, ArrayList<Student>>(); // This could be an arraylist if you think
		// groups should be ordered by "number". We are contractually obligated to consider this.
	}
	
	// This is meant to be used for IMPORTING.
	protected Course(String courseName, HashMap<Integer, Assignment> assignmentList,
			HashMap<String, Student> studentList, HashMap<String, ArrayList<Student>> groupList) {
		this.courseName = courseName;
		this.assignmentList = new HashMap<Integer, Assignment>(assignmentList);
		this.studentList = new HashMap<String, Student>(studentList);
		this.groupList = new HashMap<String, ArrayList<Student>>(groupList);
	}
	
	public String getCourseName() {
		return courseName;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		// Check if it's a COURSE or STRING
	    if (obj == null || (getClass() != obj.getClass() && !(obj instanceof String))) return false;
	    String initString = "";
	    // If it's a class, we need it's name.
	    if (getClass() == obj.getClass()) initString = ((Course) obj).getCourseName();
	    // Else, assume the name was provided.
	    else initString = (String) obj;
	    // Why are we only using the name? ... Because this would be hell if we needed more details.
	    return courseName.equals(initString);
	}
	
	@Override
	public int hashCode() {
		/* Hashcode override. Uses ONLY the courseName, so no duplicate courses. */
	    return courseName.hashCode();
	}
	
	// there might be other getters it depends on the functionality of Model methods
}