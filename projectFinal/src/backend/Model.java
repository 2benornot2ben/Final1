package backend;

import java.util.ArrayList;
import java.util.HashMap;

public class Model {
	private ArrayList<Course> fullCourseList; // ArrayList may make sense here; may not. Decide later.
	private HashMap<String, Student> studentList; // ONLY students, string is username.
	// I feel like we'd need more...
	private boolean isTeacher;
}
