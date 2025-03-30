package backend;

import java.util.HashSet;

public class Teacher extends User {
	public Teacher(String user) {
		username = user;
		courseList = new HashSet<Course>();
	}
	
	public HashSet<Course> getCourseList(){
		//this should return copy of course list
	}
}
