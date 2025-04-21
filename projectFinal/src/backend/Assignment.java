package backend;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Assignment {
	private HashMap<String, Double> idToGrade; // First string is username, second is grade
	private int maxGrade; // Note that going over is like a bonus grade.
	private boolean graded; // If ALL is graded
	private String assignmentName;
	private AssignmentType type;
	// We need a way to distinguish between 0 (you got a 0) and 0 (it's not graded for you yet) for each student.
	// Perhaps a secondary hashmap which uses the same int studentId, but holds a bool?
	// Decide Later
	public Assignment(String assignmentName, AssignmentType type) {
		this.assignmentName = assignmentName;
		//this.maxGrade = maxGrade;
		idToGrade = new HashMap<String, Double>();
		this.type = type;
	}
	
	// This is meant for IMPORTING
	protected Assignment(String assignmentName, int maxGrade,
			HashMap<String,Double> idToGrade, boolean graded) {
		this.assignmentName = assignmentName;
		this.maxGrade = maxGrade;
		this.idToGrade = new HashMap<String, Double>(idToGrade);
		this.graded = graded;
	}
	
	protected HashMap<String, Double> getIdToGrade(){
		return idToGrade;
	}
	
	public String getAssignmentName() {
		return assignmentName;
	}
	
	protected double getMaxGrade() {
		return maxGrade;
	}
	
	public boolean isGraded() {
		return graded;
	}
	
	public void setType(AssignmentType Type) {
		this.type = Type;
	}
	
	
	protected AssignmentType getType() {
		return type;
	}
	
	protected void setMaxGrade(int grade) {
		maxGrade = grade;
	}
	
	protected void gradeStudent(Student student, double grade) {
		idToGrade.put(student.getUsername(), grade);
		// This actually replaces if one's in already, so we're fine.
	}
	
	@JsonIgnore
	protected boolean getStudentGradeExists(String studentUsername) {
		return (idToGrade.get(studentUsername) != null);
		// Apparently, I have to do it like this. Funny, right?
	}
	// there might be other getters it depends on the functionality of Model methods
	
	// JSON METHODS
	// As in, we don't use these, but the json needs them to exist..
	
	private Assignment() {
		idToGrade = new HashMap<String, Double>();
	};
	
	@JsonSetter
	private void setidToGrade(HashMap<String, Double> idToGrade) {
		this.idToGrade = idToGrade;
	}
	
	@JsonSetter
	private void setGraded(boolean graded) {
		this.graded = graded;
	}
	
	@JsonSetter
	private void setAssignmentName(String assignmentName) {
		this.assignmentName = assignmentName;
	}
}