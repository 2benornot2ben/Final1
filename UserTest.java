package backend;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

public class UserTest {

	@Test
	public void test() {
		User user = new User("akim");
		user.getCompletedCourses();
		user.getCurCourses();
		user.getUsername();
		user.addCourse(new Course("CSC252"));
		user.removeCourse(new Course("CSC252"));
		user.hashCode();
		user.equals(user);
		user.equals(null);
		user.equals(new User("CSC335"));
		user.equals(new Student("Ding", "Lee", "dlee"));
		HashMap<String, Course> courseMap = new HashMap<>();
		Course course1 = new Course("CSC252");
		course1.completeCourse();
		courseMap.put("CSC252", course1);
		user.courseMap = courseMap;
		user.packUpReferences();
		user.unPackReferences(courseMap);
		User uCopy = new User();
		ArrayList<String> courseNames = new ArrayList<>();
		courseNames.add("CSC252");
		user.setCourseNames(courseNames);
		user.getCurCourses();
		user.getCompletedCourses();
		
	}

}
