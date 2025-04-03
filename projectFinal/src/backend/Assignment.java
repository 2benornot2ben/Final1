package backend;

import java.util.HashMap;

public class Assignment {
	private HashMap<String, Integer> idToGrade; // First string is username, second is grade
	private int maxGrade; // Note that going over is like a bonus grade.
	private boolean graded; // If ALL is graded
	private String assignmentName;
	// We need a way to distinguish between 0 (you got a 0) and 0 (it's not graded for you yet) for each student.
	// Perhaps a secondary hashmap which uses the same int studentId, but holds a bool?
	// Decide Later
	public Assignment(String assignmentName, int maxGrade) {
		this.assignmentName = assignmentName;
		this.maxGrade = maxGrade;
		idToGrade = new HashMap<String, Integer>();
	}
	
	// This is meant for IMPORTING
	protected Assignment(String assignmentName, int maxGrade,
			HashMap<String,Integer> idToGrade, boolean graded) {
		this.assignmentName = assignmentName;
		this.maxGrade = maxGrade;
		this.idToGrade = new HashMap<String, Integer>(idToGrade);
		this.graded = graded;
	}
	
	public String getAssignmentName() {
		return assignmentName;
	}
	
	// there might be other getters it depends on the functionality of Model methods
}