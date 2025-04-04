package backend;

import java.util.HashMap;
import java.util.HashSet;

public class User { // User is both student & teacher - use this for methods used by both.
	protected String username;
	protected HashMap<String, Course> courseMap; // String is course name, of course.
	
	public User(String username) {
		this.username = username;
		courseMap = new HashMap<String, Course>();
	}
	
	@SuppressWarnings("unchecked") // courseList should never have issues with this.
	// This constructor is meant for IMPORTING. 
	protected User(String username, HashMap<String, Course> courseMap) {
		this.username = username;
		this.courseMap = (HashMap<String, Course>) courseMap.clone(); // This only makes a SHALLOW COPY.
		// Trust me, deep copies are 10x more annoying...
		// For passing to the front end, you probably have better luck converting any HashSet
		// to an arraylist and then just passing that. (No, just using arraylists isn't any faster)
	}
	
	protected HashSet<String> getCurCourses(){
		HashSet<String> curCourses = new HashSet<String>();
		for (String key : courseMap.keySet()) {
			if (!courseMap.get(key).isCompleted()) {
				curCourses.add(key);
			}
		}
		return curCourses;
	}
	protected HashSet<String> getCompletedCourses(){
		HashSet<String> completedCourses = new HashSet<String>();
		for (String key : courseMap.keySet()) {
			if (courseMap.get(key).isCompleted()) {
				completedCourses.add(key);
			}
		}
		return completedCourses;
	}
}