package backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Course {
	private HashMap<String, Assignment> assignmentMap; // I don't think we need the hash of the assignment. Each assignment has a unique name.
	private String courseName;
	@JsonIgnore
	private Teacher teacher; // Comment: Should each course have a teacher?
	@JsonIgnore
	private HashMap<String, Student> studentMap; // The string is the username
	private Boolean completed;
	private double totalGrade;
	private HashMap<AssignmentType,Double> categories; // Assignment Type : Weight of that
	private int modeChosen = 0;
	private ArrayList<Double> weights;
	private ArrayList<Integer> drops;
	
	// This is for the json to use.
	private String teacherPacked; // Teacher username
	private ArrayList<String> studentPacked; // Student usernames
	
	public Course(String courseName) {
		this.completed = false;
		this.courseName = courseName;
		assignmentMap = new HashMap<String, Assignment>();
		studentMap = new HashMap<String, Student>();
		categories = new HashMap<>();
        categories.put(AssignmentType.MIDTERM, 300.0);
        categories.put(AssignmentType.FINAL,   200.0);
        categories.put(AssignmentType.QUIZ,    125.0);
        categories.put(AssignmentType.HW,      125.0);
        categories.put(AssignmentType.PROJECT, 250.0);
	}
	
	protected void setTotalGrade(double total) {
		totalGrade = total;
	}
	
	protected Double getTotalGrade() {
		return totalGrade;
	}
	public String getCourseName() {
		return courseName;
	}
	
	public void setStudentMap(HashMap<String, Student> studentMap) {
		this.studentMap = studentMap;
	}
	
	protected void setAssignmentMap(HashMap<String, Assignment> assignmentMap) {
		this.assignmentMap = assignmentMap;
	}
	
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	
	protected ArrayList<Assignment> getAssignmentByType(AssignmentType type){
		ArrayList<Assignment> temp = new ArrayList<Assignment>();
		for(Assignment a : assignmentMap.values()) {
			if(a.getType() == type) {
				temp.add(a);
			}
		}
		return temp;
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
	
	protected void completeCourse() {
		this.completed = true;
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
	
	protected void setGraded(String assignment) {
		assignmentMap.get(assignment).graded();
	}
	
	public HashSet<String> getGradedAssignmentsUSER(String username) {
		// go over the assignemtns and check if the assignment is graded
		// if so add it to the hashset
		// return the hashset
		HashSet<String> gradedAssignments = new HashSet<String>();
		for (String key : assignmentMap.keySet()) {
			if ((assignmentMap.get(key).getStudentGradeExists(username))) {
				gradedAssignments.add(key);
			}
		}
		return gradedAssignments;
	}
	
	public HashSet<String> getUngradedAssignmentsUSER(String username) {
		HashSet<String> ungradedAssignments = new HashSet<String>();
		for (String key : assignmentMap.keySet()) {
			if (!(assignmentMap.get(key).getStudentGradeExists(username))) {
				ungradedAssignments.add(key);
			}
		}
		return ungradedAssignments;
	}

	@JsonIgnore
	public double getCourseAverage(String stu) {
		return studentMap.get(stu).getCourseAverage(stu);
	}
	
	@JsonIgnore
	protected HashMap<String, Assignment> getAssignmentsMap(){
		return new HashMap<String, Assignment>(this.assignmentMap);
	}
  
  /*
	 * UPDATE: Behruz
	 * -  created to return student objects from studentMap
	 * to use them for sorting in Model.java
	 * */
	
	protected ArrayList<Student> getStudentMap(){
		ArrayList<Student> copyStudentMap =  new ArrayList<Student>();
		for(Student student : studentMap.values()) {
			copyStudentMap.add(student);
		}
		
		return copyStudentMap;
	}
	
	public ArrayList<Double> getGradesForAssignment(String assignmentName) {
        Assignment assignment = assignmentMap.get(assignmentName);
        if (assignment == null) {
            return new ArrayList<>();
        }
        return assignment.getAllGrades();
    }
	
	public Double getStudentGrade(String username, String assignmentName) {
        Assignment assignment = assignmentMap.get(assignmentName);
        if (assignment == null) {
            return null;
        }
        return assignment.getStudentGrade(username);
    }
	
	
	// END Behruz
	
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
	
	public void addAssignment(String assignmentName, String assignmentCategory) {
		Assignment newAssignment = new Assignment(assignmentName,this.convertToEnums(assignmentCategory));
		newAssignment.setType(this.convertToEnums(assignmentCategory));
		assignmentMap.put(assignmentName, newAssignment);
	}
	
	public void removeAssignment(String assignmentName, String assignmentCategory) {
		assignmentMap.remove(assignmentName);
	}
	
	protected int getModeChosen() {
		return modeChosen;
	}
	
	protected void setModeChosen(int modeChosen) {
		this.modeChosen = modeChosen;
	}
	
	protected void setDrop(ArrayList<Integer> drops) {
		this.drops = drops;
	}
	
	protected void setWeight(ArrayList<Double> weights) {
		this.weights = weights;
	}
	
	protected ArrayList<Double> getWeights() {
		return weights;
	}
	
	protected ArrayList<Integer> getDrops() {
		return drops;
	}
	
	private AssignmentType convertToEnums(String category) {
        switch (category.toLowerCase()) {
	        case "midterm":
	            return AssignmentType.MIDTERM;
	        case "final":
	            return AssignmentType.FINAL;
	        case "quiz":
	            return AssignmentType.QUIZ;
	        case "hw":
	            return AssignmentType.HW;
	        case "project":
	            return AssignmentType.PROJECT;
	        default:
	            throw new IllegalArgumentException("Unknown assignment type: " + category);
        }
	}
	
	
	
	
	// JSON RELATED METHODS
	// As in, if you're using it for anything other than that, hell are you doing?
	
	protected void packUpReferences() {
		studentPacked = new ArrayList<String>();
		teacherPacked = teacher.getUsername();
		for (Student i : studentMap.values()) studentPacked.add(i.getUsername());
	}
	
	// This should be called once on startup. That's it.
	protected void unPackReferences(HashMap<String, Student> studentMaps, HashMap<String, Teacher> teacherMaps) {
		for (String i : studentPacked) {
			this.studentMap.put(i, studentMaps.get(i));
		}
		ArrayList<Student> holdStudentRefs = new ArrayList<Student>();
		teacher = teacherMaps.get(teacherPacked);
	}
	
	// JSON METHODS
	// As in, we don't use these, but the json needs them to exist...
	private Course() {
		studentMap = new HashMap<String, Student>();
		assignmentMap = new HashMap<String, Assignment>();
	}
	
	// We can't fix them directly, so let's set up a fix.
	@JsonSetter
	protected void setStudentPacked(ArrayList<String> studentPacked) {
		this.studentPacked = studentPacked;
	}
	
	@JsonSetter
	protected void setTeacherPacked(String teacherPacked) {
		this.teacherPacked = teacherPacked;
	}
	
	@JsonSetter
	protected void setCategories(HashMap<AssignmentType, Double> categories) {
		this.categories = categories;
	}
	
	@JsonSetter
	protected void setWeights(ArrayList<Double> weights) {
		this.weights = weights;
	}
	
	@JsonSetter
	protected void setDrops(ArrayList<Integer> drops) {
		this.drops = drops;
	}
	
	@JsonGetter
	protected HashMap<String, Assignment> getAssignmentMap() {
		return assignmentMap;
	}
	
}
