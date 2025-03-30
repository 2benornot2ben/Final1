package backend;

import java.util.HashMap;

public class Assignment {
	private HashMap<Integer, Integer> idToGrade; // First int is studentId, second is grade
	private int maxGrade; // Note that going over is like a bonus grade.
	private boolean graded; // If ALL is graded
	private String assignmentName;
	// We need a way to distinguish between 0 (you got a 0) and 0 (it's not graded for you yet) for each student.
	// Perhaps a secondary hashmap which uses the same int studentId, but holds a bool?
	// Decide Later
	public Assignment(String assignmentName, int maxGrade) {
		this.assignmentName = assignmentName;
		this.maxGrade = maxGrade;
		idToGrade = new HashMap<Integer, Integer>();
	}
	
	public String getAssignmentName() {
		return assignmentName;
	}
	
	// there might be other getters it depends on the functionality of Model methods
}
