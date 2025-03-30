package backend;

import java.util.ArrayList;
import java.util.HashSet;

public class User { // User is both student & teacher - use this for methods used by both.
	protected String username;
	protected HashSet<Course> courseList; // Courses will have a hash; does not change as edited
}
