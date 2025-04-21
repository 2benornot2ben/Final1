package backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

public class Model {

	// this represesnts all the courses.
	private HashMap<String, Course> fullCourseMap;	// str is the course name.
	private HashMap<String, Student> studentMap; // str is the username
	private HashMap<String, Teacher> teacherMap; // str is the username
	private User personUsing;
	
	
	// definetely needs a parameter but i dont know yet.
	public Model(Database database, User person) {
		fullCourseMap = database.getCourseMap();
		studentMap = database.getStudentMap();
		teacherMap = database.getTeacherMap();
		personUsing = person;
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

	public ArrayList<String> getCurCoursesStudent(String studentUsername) {
		return studentMap.get(studentUsername).getCurCourses();
	}

	public ArrayList<String> getCompletedCoursesStudent(String studentUsername) {
		return studentMap.get(studentUsername).getCompletedCourses();
	}

	// TEACHER METHODS
	public ArrayList<String> getCurCoursesTeacher(String teacherUsername) {
		return teacherMap.get(teacherUsername).getCurCourses();
	}

	public ArrayList<String> getCompletedCoursesTeacher(String teacherUsername) {
		return teacherMap.get(teacherUsername).getCompletedCourses();
	}

	/*
	 *  All the methods are void right now, but if they return something change it to something`s type.
	 *  This might not be final layout
	 *  You can add helper methods
	 *  
	 *  I did not include parameters because i not sure what will they be, but i included my guesses. 
	 */

	public void addAssignment(String courseName, String assignmentName, String assignmentCategory) {
		fullCourseMap.get(courseName).addAssignment(assignmentName, assignmentCategory);
	}
	
	public void removeAssignment(String courseName, String assignmentName, String assignmentCategory) {
		fullCourseMap.get(courseName).removeAssignment(assignmentName, assignmentCategory);
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
	
	public ArrayList<Student> sortByFirstName(String courseName) {
		Course course = fullCourseMap.get(courseName);
		if(course == null) {
			return null;
		}
		
		ArrayList<Student> students = course.getStudentMap();
		Collections.sort(students, new Comparator<Student>() {
	        @Override
	        public int compare(Student s1, Student s2) {
	            return s1.getFirstName().compareToIgnoreCase(s2.getFirstName());
	        }
	    });
		
	    return students;
	}
	
	public ArrayList<Student> sortByLastName(String courseName) {
		Course course = fullCourseMap.get(courseName);
		if(course == null) {
			return null;
		}
		
		ArrayList<Student> students = course.getStudentMap();
		Collections.sort(students, new Comparator<Student>() {
	        @Override
	        public int compare(Student s1, Student s2) {
	            return s1.getLastName().compareToIgnoreCase(s2.getLastName());
	        }
	    });
		
		return students;
	}
	
	public ArrayList<Student> sortByUserName(String courseName) {
		Course course = fullCourseMap.get(courseName);
		if(course == null) {
			return null;
		}
		
		ArrayList<Student> students = course.getStudentMap();
		Collections.sort(students, new Comparator<Student>() {
	        @Override
	        public int compare(Student s1, Student s2) {
	            return s1.getUsername().compareToIgnoreCase(s2.getUsername());
	        }
	    });
		
		return students;
	}
	
	public void sortByGrades() {
		// for teachers
		// sorts students by their grades on an assignment
				
		// i dont know if this method should return something.
		// assignment, course should come as a parameter (ig)
	}
	
	public HashMap<String, ArrayList<String>> putInGroups(String courseName, int num) {
		ArrayList<Student> studentMap = fullCourseMap.get(courseName).getStudentMap();	
	    HashMap<String, ArrayList<String>> groupMap = new HashMap<>();
	    for (int i = 0; i < num; i++) {
	        groupMap.put("Group " + (i + 1), new ArrayList<>());
	    }
	    for (int i = 0; i < studentMap.size(); i++) {
	        Student student = studentMap.get(i);
	        String groupName = "Group " + ((i % num) + 1);
	        groupMap.get(groupName).add(student.getFirstName() + " " + student.getLastName() + " " + student.getUsername());
	    }
	    return groupMap;
	}
	
	public void assignFinalGrade(String courseName, int mode, ArrayList<Double> weights, ArrayList<Integer> drops) {
		Course course = fullCourseMap.get(courseName);
		HashMap<String, FinalGrade> finalGrades = new HashMap<String, FinalGrade>();
		HashMap<String, Assignment> assignmentMap = course.getAssignmentsMap();
		HashMap<String, Double> tempGrades = new HashMap<String, Double>();
		if(mode == 1) {
			for( Assignment assignment: assignmentMap.values()) {
				for(String j : assignment.getIdToGrade().keySet()) {
					if(!tempGrades.containsKey(j)) {
						tempGrades.put(j, assignment.getIdToGrade().get(j));
					} else {
						tempGrades.put(j, tempGrades.get(j) + assignment.getIdToGrade().get(j));
					}
				}
			}
			System.out.println(tempGrades);
		}
	}
	
	public void calculateClassAverage(int option, String courseName) { 
		//Option 2: The final grade is based on categories and percentages.
		
		// course should come as a parameter (ig)
		HashMap<String, Assignment> assignmentMap = fullCourseMap.get(courseName).getAssignmentsMap();
		if(option == 1) {
			//Option 1: Final Grade = Total Points Earned/Total Points Possible. Basically, all
			//the points from all the assignments are added up. (This is how I do it in CSc 335.)
			int[] typeCount = {0, 0, 0, 0, 0};
			for( Assignment assignment: assignmentMap.values()) {
				int type = assignment.getType().ordinal();
				typeCount[type] ++;
			}
			double[] maxGrades = {150, 200, 0, 0, 0};
			if(typeCount[2] != 0) {
				maxGrades[2] = 200 / typeCount[2];
			}
			if(typeCount[3] != 0) {
				maxGrades[3] = 100 / typeCount[3];
			}
			if(typeCount[4] != 0) {
				maxGrades[4] = 200 / typeCount[4];
			}
			for( Assignment assignment: assignmentMap.values()) {
				int type = assignment.getType().ordinal();
				assignment.setMaxGrade(maxGrades[type]);
			}
			fullCourseMap.get(courseName).setTotalGrade(maxGrades[0]+maxGrades[1]+maxGrades[2]+maxGrades[3]+maxGrades[4]);
		} else {
			for( Assignment assignment: assignmentMap.values()) {
				assignment.setMaxGrade(100);
			}
		}
	}
	
	public boolean canAssignFinalGrades(String courseName) {
		if(this.getUngradedAssignments(courseName).size() > 0) {
			return false;
		}
		return true;
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
	
	// This function should be called on just about everything.
	// courseName should be "" if it does not relate to a specific course.
	// premissionreq works like this: 0 = student, 1 = teacher, 2 = both. It will validate they meet this number.
	private boolean validateAccess(String courseName, int premissionReq) {
		if (!(courseName.equals(""))) {
			if (personUsing instanceof Teacher) {
				// Note this is a not
				if (!(fullCourseMap.get(courseName).getTeacher().equals(personUsing))) return false;
			} else {
				// Also a not
				if (!(fullCourseMap.get(courseName).isEnrolled((Student) personUsing))) return false;
			}
		}
		if (personUsing instanceof Teacher && premissionReq < 1) return false;
		if (personUsing instanceof Student && premissionReq == 1) return false;
		return true;
	}
	
	public String getCurrentUsersName() {
		return personUsing.getUsername();
	}
	
	public boolean canCreateGroups(String courseName, int num) {
		System.out.println("a:" + fullCourseMap.get(courseName));
		if(num <= fullCourseMap.get(courseName).getStudentMap().size()) {
			return true;
		}
		return false;
	}
	
	// This method was actually abandoned, but I imagine it might be used later.
	public boolean getIsTeacher() {
		return (personUsing instanceof Teacher);
	}
	
	public void getCategories(String courseName, String assignmentName, String category) {
		fullCourseMap.get(courseName).addAssignment(assignmentName, category);
	}
}
