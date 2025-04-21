package backend;

import java.util.HashMap;

public class Assignment {
	private HashMap<String, Double> idToGrade; // First string is username, second is grade
	private double maxGrade; // Note that going over is like a bonus grade.
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
	
	public void setCategory(AssignmentType newType) {
		this.type = newType;
	}
	
	protected AssignmentType getType() {
		return type;
	}
	
	protected void setMaxGrade(double grade) {
		maxGrade = grade;
	}
	// there might be other getters it depends on the functionality of Model methods
	
}
