package backend;

import java.util.HashMap;
import java.util.HashSet;

public class Student extends User {
	private String firstName;
	private String lastName;
	private HashMap<String, Integer> studentAverageGrades; // String is meant to be classes
	private enum Grades {A, B, C, D, E, F};
	private HashMap<String, Grades> studentGradeLetters; // Sadly not automatically updated
}
