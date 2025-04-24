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
	
	// STUDENT METHODS 
	public HashSet<String> getGradedAssignmentsUSER(String courseName, String studentUsername) {
		Course course = fullCourseMap.get(courseName);
		return course.getGradedAssignmentsUSER(studentUsername); // I believe no escaping reference here but could someone please confirm?
	}
	
	public HashSet<String> getUngradedAssignmentsUSER(String courseName, String studentUsername) {
		Course course = fullCourseMap.get(courseName);
		return course.getUngradedAssignmentsUSER(studentUsername); // I believe no escaping reference here but could someone please confirm?
	}
	
	public ArrayList<String> getStudentList(String courseName){
		ArrayList<Student> students = fullCourseMap.get(courseName).getStudentMap();
		ArrayList<String> studentUsernames = new ArrayList<String>();
		for(Student s: students) {
			studentUsernames.add(s.getUsername());
		}
		return studentUsernames;
	}
	
	public void setAssignmentGraded(String courseName, String assignment) {
		fullCourseMap.get(courseName).setGraded(assignment);
	}
	
	public double getCourseAverage(String courseName, String stu) {
		return fullCourseMap.get(courseName).getCourseAverage(stu);
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
	public HashSet<String> getGradedAssignments(String courseName) {
		Course course = fullCourseMap.get(courseName);
		return course.getGradedAssignments(); // I believe no escaping reference here but could someone please confirm?
	}
	
	public HashSet<String> getUngradedAssignments(String courseName) {
		Course course = fullCourseMap.get(courseName);
		return course.getUngradedAssignments(); // I believe no escaping reference here but could someone please confirm?
	}
	
	public ArrayList<String> getCurCoursesTeacher(String teacherUsername) {
		return teacherMap.get(teacherUsername).getCurCourses();
	}

	public ArrayList<String> getCompletedCoursesTeacher(String teacherUsername) {
		return teacherMap.get(teacherUsername).getCompletedCourses();
	}

	public boolean canCalculateStudentCurAverage(String course, String stu) {
		if(fullCourseMap.get(course).getStudentMap().contains(stu)) {
			return true;
		}
		return false;
	}

	public void addAssignment(String courseName, String assignmentName, String assignmentCategory) {
		fullCourseMap.get(courseName).addAssignment(assignmentName, assignmentCategory);
		ArrayList<Double> weights = fullCourseMap.get(courseName).getWeights();
		ArrayList<Integer> drops = fullCourseMap.get(courseName).getDrops();
		this.calculateClassAverage(fullCourseMap.get(courseName).getModeChosen(), courseName, weights, drops);
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
	
	public String addGradeForAssignment(String stuNameHolder, String assignNameHolder, double gradeNumHolderDoub, String courseName) {
		Student stu = studentMap.get(stuNameHolder);
		if (stu == null) return "Student does not exist!";
		Course course = fullCourseMap.get(courseName);
		if (course == null) return "Course does not exist!";
		if (!(stu.getCurCourses().contains(course))) return "Student not in course!";
		Assignment assign = course.getAssignmentsMap().get(assignNameHolder);
		if (assign == null) return "Assignment does not exist!";
		assign.gradeStudent(courseName,stu, gradeNumHolderDoub);
		return "Done!";
	}
	
	public String calculateStats(String courseName, String assignmentName) {
 		// for teachers
 		// calculates the averages and the medians on assignment 
 		// returns a String message e.g. "Average on Project1 is 98% and median is 97%"
 
 		// assignment, course should come as a parameter (ig)
 
 		Course course = fullCourseMap.get(courseName);
         if (course == null) {
             return "Course not found.";
         }
         Assignment assignment = course.getAssignmentsMap().get(assignmentName);
         if (assignment == null) {
             return "Assignment not found.";
         }
         ArrayList<Double> grades = course.getGradesForAssignment(assignmentName);
         if (grades.isEmpty()) {
             return "No grades available for " + assignmentName + ".";
         }
         
         // Calculate average
         double sum = 0;
         for (Double grade : grades) {
             sum += grade;
         }
         double average = (sum / grades.size()) / assignment.getMaxGrade() * 100;
 		
         
         // Calculate median
         Collections.sort(grades);
         double median;
         int size = grades.size();
         if (size % 2 == 0) {
             median = (grades.get(size / 2 - 1) + grades.get(size / 2)) / 2.0;
         } else {
             median = grades.get(size / 2);
         }
         median = (median / assignment.getMaxGrade()) * 100;
         
         return String.format("Average on %s is %.2f%% and median is %.2f%%", assignmentName, average, median);
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
	
	public boolean checkCompleted(String courseName) {
		if(this.getUngradedAssignments(courseName).size() > 0) {
			return false;
		}
		return true;
	}
	
	public void completeCourse(String courseName) {
		fullCourseMap.get(courseName).completeCourse();
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
	
	public ArrayList<Student> sortByGrades(String courseName, String assignmentName) {
 		Course course = fullCourseMap.get(courseName);
 		if (course == null || course.getAssignmentsMap().get(assignmentName) == null) {
             return null;
         }
 		
 		ArrayList<Student> students = course.getStudentMap();
         Collections.sort(students, new Comparator<Student>() {
             @Override
             public int compare(Student s1, Student s2) {
                 Double grade1 = getStudentGrade(s1.getUsername(), courseName, assignmentName);
                 Double grade2 = getStudentGrade(s2.getUsername(), courseName, assignmentName);
                 if (grade1 == null && grade2 == null) {
                     return 0;
                 } else if (grade1 == null) {
                     return 1; 
                 } else if (grade2 == null) {
                     return -1;
                 }
                 return Double.compare(grade2, grade1);
             }
         });
         
         return students;
 	}
	
	public Double getStudentGrade(String username, String courseName, String assignmentName) {
        Course course = fullCourseMap.get(courseName);
        if (course == null) {
            return null;
        }
        Assignment assignment = course.getAssignmentsMap().get(assignmentName);
        if (assignment == null) {
            return null;
        }
        return assignment.getStudentGrade(username);
    }
	
	
	public HashMap<String, ArrayList<String>> putInGroups(String courseName, int num) {
		// for teachers
		// creates groups and puts students into them.
		// return HashMap<String, ArrayList<Student>> String is group name.
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
	
	public ArrayList<String> assignFinalGrade(String courseName) {
		Course course = fullCourseMap.get(courseName);
		ArrayList<Double> weights = course.getWeights();
		ArrayList<Integer> drops = course.getDrops();
		HashMap<String, FinalGrade> finalGrades = new HashMap<String, FinalGrade>();
		HashMap<String, Assignment> assignmentMap = course.getAssignmentsMap();
		HashMap<String, Double> tempGrades = new HashMap<String, Double>();
		if(course.getModeChosen() == 1) {
			for( Assignment assignment: assignmentMap.values()) {
				for(String j : assignment.getIdToGrade().keySet()) {
					if(!tempGrades.containsKey(j)) {
						tempGrades.put(j, assignment.getIdToGrade().get(j));
					} else {
						tempGrades.put(j, tempGrades.get(j) + assignment.getIdToGrade().get(j));
					}
				}
			}
			for(String j : tempGrades.keySet()) {
				double tempTotal = tempGrades.get(j) / fullCourseMap.get(courseName).getTotalGrade() * 100;
				finalGrades.put(j, this.calculateGrade(tempTotal));
			}
		} else {
			HashMap<AssignmentType, ArrayList<Assignment>> tempAssignments = new HashMap<>();
		    for (AssignmentType type : AssignmentType.values()) {
		        tempAssignments.put(type, course.getAssignmentByType(type));
		    }
		    HashSet<String> allStudentIds = new HashSet<>();
		    for (Assignment assignment : assignmentMap.values()) {
		        allStudentIds.addAll(assignment.getIdToGrade().keySet());
		    }

		    for (String stu : allStudentIds) {
		        double finalGrade = 0;
		        for (AssignmentType type : AssignmentType.values()) {
		            ArrayList<Assignment> assignments = tempAssignments.get(type);
		            ArrayList<Double> tempGrade = new ArrayList<>();
		            for (Assignment assignment : assignments) {
		                tempGrade.add(assignment.getIdToGrade().get(stu));
		            }
		            int drop = drops.get(type.ordinal());
		            Collections.sort(tempGrade);
		            while (drop > 0 && tempGrade.size() != 0) {
		            	tempGrade.remove(0);
		            	drop--;
		            }
		            double total = 0.0;
		            for (int i = 0; i < tempGrade.size(); i++) {
		                total += tempGrade.get(i);
		            }
		            if (tempGrade.size() > 0) {
		            	total /= tempGrade.size();
		            }
		            double weight = weights.get(type.ordinal());
		            finalGrade += total * weight / 100.0;
		        }
		        finalGrades.put(stu, this.calculateGrade(finalGrade));
		    }
		}
		for(String stu : finalGrades.keySet()) {
			studentMap.get(stu).updateStudentGradeLetters(courseName, finalGrades.get(stu));
		}
		ArrayList<String> finals = new ArrayList<String>();
		for(String stu : finalGrades.keySet()) {
			String temp = "" + stu + " " +  finalGrades.get(stu);
			finals.add(temp);
			temp = "";
		}
		return finals;
	}
	
	public void calculateClassAverage(int option, String courseName, ArrayList<Double> weights, ArrayList<Integer> drops) {
		HashMap<String, Assignment> assignmentMap = fullCourseMap.get(courseName).getAssignmentsMap();
		if(option == 1) {
			//Option 1: Final Grade = Total Points Earned/Total Points Possible. Basically, all
			//the points from all the assignments are added up. (This is how I do it in CSc 335.)
			int[] typeCount = {0, 0, 0, 0, 0};
			for( Assignment assignment: assignmentMap.values()) {
				int type = assignment.getType().ordinal();
				typeCount[type] ++;
			}
			int[] maxGrades = {150, 200, 0, 0, 0};
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
			//more appropriate way needed.
			fullCourseMap.get(courseName).setTotalGrade(2*maxGrades[0]+maxGrades[1]+maxGrades[2]+maxGrades[3]+maxGrades[4]);
			fullCourseMap.get(courseName).setModeChosen(1); // New line of code here.
			
		} else {
			for (Assignment assignment: assignmentMap.values()) {
				assignment.setMaxGrade(100);
				// What's this?
				// We'll assume 0 is "not set up" btw.
			}
			fullCourseMap.get(courseName).setModeChosen(2);
			fullCourseMap.get(courseName).setDrop(drops);
			fullCourseMap.get(courseName).setWeight(weights);
		}
	}
	
	public boolean canAssignFinalGrades(String courseName) {
		if(this.getUngradedAssignments(courseName).size() > 0) {
			return false;
		}
		return true;
	}
	
	/*public void createCourses(String filename) {
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
	}*/
	
	// This function should be called on just about everything.
	// courseName should be "" if it does not relate to a specific course.
	// premissionreq works like this: 0 = student, 1 = teacher, 2 = both. It will validate they meet this number.
	/*private boolean validateAccess(String courseName, int premissionReq) {
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
	}*/
	
	public String getCurrentUsersName() {
		return personUsing.getUsername();
	}
	
	public boolean canCreateGroups(String courseName, int num) {
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
	
	private FinalGrade calculateGrade(Double grade) {
		if(grade >= 90) {
			return FinalGrade.A;
		} else if(grade >= 80) {
			return FinalGrade.B;
		} else if(grade >= 70) {
			return FinalGrade.C;
		} else if(grade >= 60) {
			return FinalGrade.D;
		} else {
			return FinalGrade.E;
		}
	}
	
	public int isSetUp(String coursename) {
		return (fullCourseMap.get(coursename).getModeChosen());
	}
}