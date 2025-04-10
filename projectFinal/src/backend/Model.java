package backend;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Model {

	// this represesnts all the courses.
	private HashMap<String, Course> fullCourseMap;	// str is the course name.


	private HashMap<String, Student> studentMap; // str is the username

	private HashMap<String, Teacher> teacherMap; // str is the username
	
	
	// definetely needs a parameter but i dont know yet.
	public Model(Database database) {
		fullCourseMap = database.getCourseMap();
		studentMap = database.getStudentMap();
		teacherMap = database.getTeacherMap();
	}
	// We need the import version too but my brain kinda errored when I tried to make it - Ben
	
	// STUDENT METHODS 
	public HashSet<String> getGradedAssignments(String courseName) {
		
		Course course = fullCourseMap.get(courseName);
		return course.getGradedAssignments(); // I believe no escaping reference here but could someone please confirm?
	}
	
	public HashSet<String> getUngradedAssignments(String courseName) {
		Course course = fullCourseMap.get(courseName);
		return course.getUngradedAssignments(); // I believe no escaping reference here but could someone please confirm?
	}
	
	public double getCourseAverage(String courseName) {
		return fullCourseMap.get(courseName).getCourseAverage();
	}
	
	public double calculateGPA(String studentUsername) {
		Student student = studentMap.get(studentUsername);
		return student.calculateGPA();
	}

	public HashSet<String> getCurCoursesStudent(String studentUsername) {
		return studentMap.get(studentUsername).getCurCourses();
	}

	public HashSet<String> getCompletedCoursesStudent(String studentUsername) {
		return studentMap.get(studentUsername).getCompletedCourses();
	}

	// TEACHER METHODS
	public HashSet<String> getCurCoursesTeacher(String teacherUsername) {
		return teacherMap.get(teacherUsername).getCurCourses();
	}

	public HashSet<String> getCompletedCoursesTeacher(String teacherUsername) {
		return teacherMap.get(teacherUsername).getCompletedCourses();
	}

	/*
	 *  All the methods are void right now, but if they return something change it to something`s type.
	 *  This might not be final layout
	 *  You can add helper methods
	 *  
	 *  I did not include parameters because i not sure what will they be, but i included my guesses. 
	 */

	public void addAssignment() {
		// for teachers
		// adds an assignment to a course
		
		// assignment, course should come as a parameter (ig)
	}
	
	public void removeAssignment() {
		// for teachers
		// removes an assignment from a course
		
		// assignment, course should come as a parameter (ig)
	}
	
	public String addStudent(String username, String coursename) {
		// for teachers
		// adds a student to a course
		Course course = fullCourseMap.get(coursename);
		Student student = studentMap.get(username);
		if (course == null) return ("Course not found.");
		if (student == null) return ("Student not found");
		if (course.addStudent(student)) {
			student.addCourse(course);
			return ("Successfully added student");
		}
		return ("Student already in course!");
	}
	
	public String removeStudent(String username, String coursename) {
		// for teachers
		// removes a student from a course
		System.out.println(fullCourseMap);
		Course course = fullCourseMap.get(coursename);
		Student student = studentMap.get(username);
		if (course == null) return ("Course not found.");
		if (student == null) return ("Student not found");
		if (course.removeStudent(student)) {
			student.removeCourse(course);
			return ("Successfully removed student");
		}
		return ("Student not in course!");
	}
	
	public void importStudents() {
		// for teachers
		// adds students to a course
				
		// students (as a list?), course should come as a parameter (ig)
	}
	
	public String getEnrolledStudents(String coursename) {
		// for teachers
		// returns student list who are enrolled in a course
		Course course = fullCourseMap.get(coursename);
		if (course == null) return ("Course not found.");
		Iterator<Student> studentIter = course.getEnrolledStudents();
		String holdFormat = "";
		while (studentIter.hasNext()) {
			Student student = studentIter.next(); // Apparently, you have to do this first. Hopefully.
			holdFormat = holdFormat + student.getPrintFormatted() + "\n";
		}
		return holdFormat.strip();
		
	}
	
	public void addGradeForAssignment() {
		// for teachers
		// adds a grade for an assignment
		
		// student, assignment, course should come as a parameter (ig)
		
		
		// not sure how to implement this function (do we add the same grade to anyone or one at a time?)
	}
	
	public void calculateStats() {
		// for teachers
		// calculates the averages and the medians on assignment 
		// returns a String message e.g. "Average on Project1 is 98% and median is 97%"
		
		// assignment, course should come as a parameter (ig)
		
	}
	
	public double calculateStudentCurAverage(String studentUsername) {
		return studentMap.get(studentUsername).calculateCurAverage();
	}
	
	/*
	 * There are 4 sorts and each has a separate method. We can simplify it and make it as one method.
	 */
	
	public void sortByFirstName() {
		// for teachers
		// sorts students by their first name
		
		// i dont know if this method should return something.
		// course should come as a parameter (ig)
	}
	
	public void sortByLastName() {
		// for teachers
		// sorts students by their last name
				
		// i dont know if this method should return something.
		// course should come as a parameter (ig)
	}
	
	public void sortByUserName() {
		// for teachers
		// sorts students by their username
				
		// i dont know if this method should return something.
		// course should come as a parameter (ig)
	}
	
	public void sortByGrades() {
		// for teachers
		// sorts students by their grades on an assignment
				
		// i dont know if this method should return something.
		// assignment, course should come as a parameter (ig)
	}
	
	public void putInGroups() {
		// for teachers
		// creates groups and puts students into them.
		// return HashMap<String, ArrayList<Student>> String is group name.
				
		// i dont know if this method should return something.
		// course should come as a parameter (ig)
	}
	
	public void assignFinalGrade() {
		// for teachers
		// calculates a Final Grade
		
		// course should come as a parameter (ig)
	}
	
	public void calculateClassAverage(int option) { 
		// for teachers
		//Option 1: Final Grade = Total Points Earned/Total Points Possible. Basically, all
		//the points from all the assignments are added up. (This is how I do it in CSc 335.)
		//Option 2: The final grade is based on categories and percentages.
		
		// course should come as a parameter (ig)
		
	}
	
	public void setUpCategories() {
		// for teachers
		// In a course that uses categories, there should be the option to “drop” a certain number of
		// assignments with the lowest grade and to assign weights to the different categories.
		
		// course should come as a parameter (ig)
		
		// Is this necessary? - Ben
	}
	
	public void createCourses(String filename) {
		// Should this lock itself up after 1 run?
		try (Scanner scanLine = new Scanner(new File(filename))){
			while(scanLine.hasNextLine()){
				// format we get filenames from
				String line = scanLine.nextLine();
				if (!(fullCourseMap.containsKey(line))) {
					fullCourseMap.put(line, new Course(line));
				}
				// This originally forced an error if a dupe existed, but I removed it because
				// there is may be scenarios where duplicates may occur. Now it just handles it.
				// May undo depending on how rest of the code is developed
			}
			scanLine.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	public String importStudentList(String courseName, String filename) {
		Course course = fullCourseMap.get(courseName);
		if (course == null) return ("Course not found.");
		HashSet<String> studentUsernameList = new HashSet<String>();
		try (Scanner scanLine = new Scanner(new File(filename))){
			while(scanLine.hasNextLine()){
				// format we get filenames from
				String line = scanLine.nextLine();
				studentUsernameList.add(line);
			}
			scanLine.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		for (String i: studentUsernameList) {
			addStudent(courseName, i);
		}
		return ("Student(s) added!");
	}



}
