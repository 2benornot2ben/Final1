package backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Student.class, name = "student"),
        @JsonSubTypes.Type(value = Teacher.class, name = "teacher")
})
public class User { // User is both student & teacher - use this for methods used by both.
	protected String username;
	@JsonIgnore
	protected HashMap<String, Course> courseMap; // String is course name, of course.
	
	// This is for the json to use.
	private ArrayList<String> courseNames; // Names of the courses
	
	public User(String username) {
		this.username = username;
		courseMap = new HashMap<String, Course>();
	}
	
	protected ArrayList<String> getCurCourses(){
		ArrayList<String> curCourses = new ArrayList<String>();
		for (String key : courseMap.keySet()) {
			if (!courseMap.get(key).isCompleted()) {
				curCourses.add(key);
			}
		}
		return curCourses;
	}
	protected ArrayList<String> getCompletedCourses(){
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
	
	// Unsure if these 2 should be protected or not
	protected void addCourse(Course course) {
		courseMap.put(course.getCourseName(), course);
	}
	
	protected void removeCourse(Course course) {
		courseMap.remove(course.getCourseName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(username);
	}

	@Override
	public boolean equals(Object obj) {
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
		courseNames = new ArrayList<>(courseMap.keySet());
	}
	
	protected void unPackReferences(HashMap<String, Course> courseMap) {
		//courseMap = new HashMap<String, Course>();
		for (String i : courseNames) {
			this.courseMap.put(i, courseMap.get(i));
		}
	}
	
	// JSON METHODS
	// As in, we don't use these, but the json needs them to exist...
	// This one, specifically, needs to be protected. Why? That's... Actually a good question, but it does.
	protected User() {courseMap = new HashMap<String, Course>();};
	
	@JsonSetter
	private void setCourseNames(ArrayList<String> courseNames) {
		this.courseNames = courseNames;
	}
}