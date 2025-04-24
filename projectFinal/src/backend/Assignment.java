/**************************************************************
 * Author: Davranbek Kadirimbetov, Benjamin Kanter,
 * 		   Fatih Bozdogan, & Behruz Ernazarov
 * Description: Represents a course assignment and
 * 	   manages the grading information for each student. It stores
 *     individual student grades using a HashMap keyed by username,
 *     tracks the maximum possible grade, and whether the assignment
 *     has been fully graded.
 **************************************************************/

package backend;

import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Assignment {
	private HashMap<String, Double> idToGrade;
	private int maxGrade;
	private boolean graded;
	private String assignmentName;
	private AssignmentType type;
	public Assignment(String assignmentName, AssignmentType type) {
		/*
		 * Description: Constructor
		 * Parameters: assignmentName: String representing the name of assignment
		 * 			type: represents type of the assignment and it is Enum
		 * Returns: None
		 */
		this.assignmentName = assignmentName;
		idToGrade = new HashMap<String, Double>();
		this.type = type;
	}
	
	public Double getStudentGrade(String username) {
		// simple getter
		return idToGrade.get(username);
	}
	 	
	public ArrayList<Double> getAllGrades() {
		/*
		 * Description: Returns all non-null student grades.
		 * Parameters: None
		 * Returns: ArrayList of Double values representing grades
		 */
		ArrayList<Double> grades = new ArrayList<>();
	    for (Double grade : idToGrade.values()) {
	    	if (grade != null) {
	    		grades.add(grade);
	        }
	    }
	    return grades;
	}
	
	protected HashMap<String, Double> getIdToGrade(){
		// simple getter
		return idToGrade;
	}
	
	public String getAssignmentName() {
		// simple getter
		return assignmentName;
	}
	
	protected double getMaxGrade() {
		// simple getter
		return maxGrade;
	}
	
	public boolean isGraded() {
		// Checks whether all students have been graded.
		return graded;
	}
	
	public void graded() {
		this.graded = true;
	}
	
	public void setType(AssignmentType Type) {
		// simple setter
		this.type = Type;
	}
	
	
	protected AssignmentType getType() {
		// simple getter
		return type;
	}
	
	protected void setMaxGrade(int grade) {
		// simple setter
		maxGrade = grade;
	}
	
	protected void gradeStudent(String courseName, Student student, double grade) {
		/*
		 * Description: Grades a student and updates their course average.
		 * Parameters: courseName - String representing the course
		 *     student - Student object
		 *     grade - double representing grade as percentage (0–100)
		 * Returns: None
		 */
		double points = this.maxGrade * grade / 100;
		student.updateStudentAverageGrades(courseName, points);
		idToGrade.put(student.getUsername(), points);
	}
	
	@JsonIgnore
	protected boolean getStudentGradeExists(String studentUsername) {
		// simple getter
		return (idToGrade.get(studentUsername) != null);
	}
	
	protected Assignment() {
		// copy constructor
		idToGrade = new HashMap<String, Double>();
	};
	
	@JsonSetter 
	protected void setidToGrade(HashMap<String, Double> idToGrade) {
		// json setter
		this.idToGrade = idToGrade;
	}
	
	@JsonSetter
	protected void setGraded(boolean graded) {
		// json setter
		this.graded = graded;
	}
	
	@JsonSetter
	protected void setAssignmentName(String assignmentName) {
		// json setter
		this.assignmentName = assignmentName;
	}
}
