package backend;

import java.util.ArrayList;
import java.util.HashMap;

public class User { // User is both student & teacher - use this for methods used by both.
	protected String username;
	protected HashMap<String, Course> courseList; // String is course name, of course.
	
	public User(String username) {
		this.username = username;
		courseList = new HashMap<String, Course>();
	}
	
	@SuppressWarnings("unchecked") // courseList should never have issues with this.
	// This constructor is meant for IMPORTING. 
	protected User(String username, HashMap<String, Course> courseList) {
		this.username = username;
		this.courseList = (HashMap<String, Course>) courseList.clone(); // This only makes a SHALLOW COPY.
		// Trust me, deep copies are 10x more annoying...
		// For passing to the front end, you probably have better luck converting any HashSet
		// to an arraylist and then just passing that. (No, just using arraylists isn't any faster)
	}
	
	protected HashMap<String, Course> getCourseList(){
		//this should return copy of course list
		return new HashMap<String, Course>(courseList);
		// Note that this is also a SHALLOW COPY, which is actually an escaping reference
		// if it reaches the frontend!
	}
}