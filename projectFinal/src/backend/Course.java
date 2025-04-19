package backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Course {
	protected HashMap<String, Assignment> assignmentMap; // I don't think we need the hash of the assignment. Each assignment has a unique name.
	protected String courseName;
	
	protected Teacher teacher; // Comment: Should each course have a teacher?
	protected HashMap<String, Student> studentMap; // The string is the username
	protected Boolean completed;
	protected HashMap<String, ArrayList<Student>> groupList; // String is "group name"
	
	public Course(String courseName) {
		this.completed = false;
		this.courseName = courseName;
		assignmentMap = new HashMap<String, Assignment>();
		studentMap = new HashMap<String, Student>();
		groupList = new HashMap<String, ArrayList<Student>>(); // This could be an arraylist if you think
		// groups should be ordered by "number". We are contractually obligated to consider this.
	}
	
	// This is meant to be used for IMPORTING.
	protected Course(String courseName, HashMap<String, Assignment> assignmentMap,
			HashMap<String, Student> studentMap, HashMap<String, ArrayList<Student>> groupMap) {
		this.completed = false; // May be changed later
		this.courseName = courseName;
		this.assignmentMap = assignmentMap; // escaping reference; will handle it later when we actually use it.
		this.studentMap = studentMap; // escaping reference; will handle it later when we actually use it.
		this.groupList = groupMap; // escaping reference; will handle it later when we actually use it.
	}
	
	public String getCourseName() {
		return courseName;
	}
	
	public void setStudentMap(HashMap<String, Student> studentMap) {
		this.studentMap = studentMap;
	}
	
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		// Check if it's a COURSE or STRING
	    if (obj == null || (getClass() != obj.getClass() && !(obj instanceof String))) return false;
	    String initString = "";
	    // If it's a class, we need it's name.
	    if (getClass() == obj.getClass()) initString = ((Course) obj).getCourseName();
	    // Else, assume the name was provided.
	    else initString = (String) obj;
	    // Why are we only using the name? ... Because this would be hell if we needed more details.
	    return courseName.equals(initString);
	}
	
	@Override
	public int hashCode() {
		/* Hashcode override. Uses ONLY the courseName, so no duplicate courses. */
	    return courseName.hashCode();
	}


	public boolean isCompleted() {
		return completed;
	}
	
	// this is absolutely an encapsulation issue
	public HashSet<String> getGradedAssignments() {
		// go over the assignemtns and check if the assignment is graded
		// if so add it to the hashset
		// return the hashset
		HashSet<String> gradedAssignments = new HashSet<String>();
		for (String key : assignmentMap.keySet()) {
			if (assignmentMap.get(key).isGraded()) {
				gradedAssignments.add(key);
			}
		}
		return gradedAssignments;
	}
	
	public HashSet<String> getUngradedAssignments() {
		HashSet<String> ungradedAssignments = new HashSet<String>();
		for (String key : assignmentMap.keySet()) {
			if (!assignmentMap.get(key).isGraded()) {
				ungradedAssignments.add(key);
			}
		}
		return ungradedAssignments;
	}

	@JsonIgnore
	public double getCourseAverage() {
		double totalAvg = 0;
		for (String key : studentMap.keySet()) {
			totalAvg += studentMap.get(key).getStudentAverage(courseName);
		}
		return totalAvg / studentMap.size();
	}
  
  /*
	 * UPDATE: Behruz
	 * -  created to return student objects from studentMap
	 * to use them for sorting in Model.java
	 * */
	
	public ArrayList<Student> getStudentMap(){
		ArrayList<Student> copyStudentMap =  new ArrayList<Student>();
		for(Student student : studentMap.values()) {
			copyStudentMap.add(student);
		}
		
		return copyStudentMap;
	}
	
	protected boolean addStudent(Student student) {
		String stuUser = student.getUsername();
		if (studentMap.containsKey(stuUser)) return false;
		studentMap.put(stuUser, student);
		return true;
	}
	
	protected boolean removeStudent(Student student) {
		String stuUser = student.getUsername(); // This could be made to just use a string if necessary.
		if (!(studentMap.containsKey(stuUser))) return false;
		studentMap.remove(stuUser);
		return true;
	}
	
	protected Iterator<Student> getEnrolledStudents() {
		return studentMap.values().iterator();
	}
	
	protected Teacher getTeacher() {
		return teacher;
	}
	
	public boolean isEnrolled(Student student) {
		return studentMap.containsKey(student.getUsername());
	}

	// public double getStudentAverage(String studentUsername) {
	// 	return studentMap.get(studentUsername).getStudentAverage(courseName);
	// }
	
	// JSON METHODS
	// As in, we don't use these, but the json needs them to exist...
	private Course() {};
	
	@JsonSetter
	private void setAssignmentMap(HashMap<String, Assignment> assignmentMap) {
		this.assignmentMap = assignmentMap;
	}
	
	@JsonSetter
	private void setStudentMap(Student[] studentMap ) {
		HashMap<String, Student> makeHashMap = new HashMap<String, Student>(studentMap.length);
		for (Student i : studentMap) {
			makeHashMap.put(i.getUsername(), i);
			if (i.courseMap == null) i.courseMap = new HashMap<String, Course>();
			if (!(i.courseMap.containsKey(courseName))) i.addCourse(this);
		}
		this.studentMap = makeHashMap;
	}
	
	@JsonSetter
	private void setGroupList(HashMap<String, ArrayList<Student>> groupList) {
		this.groupList = groupList;
	}
}