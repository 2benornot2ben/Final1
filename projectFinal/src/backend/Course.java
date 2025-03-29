package backend;

import java.util.ArrayList;
import java.util.HashMap;

public class Course {
	private HashMap<Integer, Assignment> assignmentList; // The integer is the hash of the Assignment.
	// No duplicate assignments! ... Why would you have a duplicate, anyways?
	private HashMap<String, Student> studentList; // The string is the username
	private Boolean completed;
	private HashMap<String, ArrayList<Student>> groupList; // String is "group name"
}
