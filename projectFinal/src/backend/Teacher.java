package backend;

import java.util.HashMap;

public class Teacher extends User {
	public Teacher(String user) {
		super(user);
	}
	
	// This is meant for IMPORTING
	protected Teacher(String user, HashMap<String, Course> courseList) {
		super(user, courseList);
	}
}