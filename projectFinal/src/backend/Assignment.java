package backend;

import java.util.HashMap;

public class Assignment {
	private HashMap<Integer, Integer> idToGrade; // First int is studentId, second is grade
	// Fatih's comment: why not use a String username here?

	
	private int maxGrade; // Note that going over is like a bonus grade.
	// Fatih's comment: what do you mean here? Let's not write the details yet
	
	private boolean graded; // If ALL is graded
	// We need a way to distinguish between 0 (you got a 0) and 0 (it's not graded for you yet) for each student.
	// Perhaps a secondary hashmap which uses the same int studentId, but holds a bool?
	// Decide Later
	// Fatih's comment: Perhaps
}
