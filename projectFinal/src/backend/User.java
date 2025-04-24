/**************************************************************
 * Author: Davranbek Kadirimbetov, Benjamin Kanter,
 * 		   Fatih Bozdogan, & Behruz Ernazarov
 * Description: Simulates a user! This object is only extended
 * from, ideally no person should ever be a "User". Still,
 * this has some methods shared between Teacher & Student.
 **************************************************************/

package backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

// Ah yes, when loading from a json, we need to identify if it's a student or teacher.
// A smart programmer can just look at it and figure it out, but jackson is not a smart
// programmer (no shade, that's just the jar name), so we need to give it a specific extra
// line so it can figure itself out. Simply, when saving, another "variable" is saved to
// the json which looks like the others, something like "type": "student". When it tries
// to load to User, it will notice the type here, reference it with what it's loading,
// and if it finds a match, it will go to that class instead.
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Student.class, name = "student"),
        @JsonSubTypes.Type(value = Teacher.class, name = "teacher")
})
public class User {
	/* This class is simply extended from to make Teacher and Student
	 * not as complicated, as well as storage & conversion of them.
	 * No non-extended user should ever exist in normal runtime. */
	protected String username;
	@JsonIgnore
	protected HashMap<String, Course> courseMap; // String is course name, of course.
	
	// This is for the json to use.
	private ArrayList<String> courseNames; // Names of the courses
	
	public User(String username) {
		/* Initializes the username, and the coursemap. */
		this.username = username;
		courseMap = new HashMap<String, Course>();
	}
	
	protected ArrayList<String> getCurCourses(){
		/* Returns the current courses's names. Note that the keys
		 * are their names, so very convenient. Must not be completed. */
		ArrayList<String> curCourses = new ArrayList<String>();
		for (String key : courseMap.keySet()) {
			if (!courseMap.get(key).isCompleted()) {
				curCourses.add(key);
			}
		}
		return curCourses;
	}
	protected ArrayList<String> getCompletedCourses(){
		/* Returns the current completed courses's names. Note that the keys
		 * are their names, so very convenient. Must be completed. */
		ArrayList<String> completedCourses = new ArrayList<String>();
		for (String key : courseMap.keySet()) {
			if (courseMap.get(key).isCompleted()) {
				completedCourses.add(key);
			}
		}
		return completedCourses;
	}
	
	public String getUsername() {
		return username;
	}
	
	protected void addCourse(Course course) {
		/* Adds a course, setting the key to the course name. */
		courseMap.put(course.getCourseName(), course);
	}
	
	protected void removeCourse(Course course) {
		/* Removes a course, using the course's name. */
		courseMap.remove(course.getCourseName());
	}

	@Override
	public int hashCode() {
		/* Because all usernames are unique, this is valid
		 * for a unique hashcode. */
		return Objects.hash(username);
	}

	@Override
	public boolean equals(Object obj) {
		/* Because all usernames are unique, comparing their
		 * usernames is good enough to know if they are the same. */
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(username, other.username);
	}
	
	// JSON RELATED METHODS
	// As in, if you're using it for anything other than that, hell are you doing?
	
	protected void packUpReferences() {
		/* Sets the courseNames to be used for json packing. */
		courseNames = new ArrayList<>(courseMap.keySet());
	}
	
	protected void unPackReferences(HashMap<String, Course> courseMap) {
		/* Can use courseNames to fill courses. Vital for avoiding
		 * infinity mirrors & duplicates with json. */
		for (String i : courseNames) {
			this.courseMap.put(i, courseMap.get(i));
		}
	}
	
	// JSON METHODS
	// As in, we don't use these, but the json needs them to exist...
	protected User() {courseMap = new HashMap<String, Course>();};
	
	@JsonSetter
	protected void setCourseNames(ArrayList<String> courseNames) {
		this.courseNames = courseNames;
	}
}