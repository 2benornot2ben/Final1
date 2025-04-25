/**************************************************************
 * Author: Davranbek Kadirimbetov, Benjamin Kanter,
 * 		   Fatih Bozdogan, & Behruz Ernazarov
 * Description: The model which contains... A decent lot of info!
 * Interestingly, it is NOT saved when you export to json. Still,
 * this is what's called from the view to do most every one of
 * it's functions.
 **************************************************************/

package backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Model {
	/* What the view uses to interact with the back end!
	 * Note that every variable here is regrettable with the database,
	 * so this doesn't need to be saved when exporting to json. */

	// this represents all the courses.
	private HashMap<String, Course> fullCourseMap;	// str is the course name.
	private HashMap<String, Student> studentMap; // str is the username
	private HashMap<String, Teacher> teacherMap; // str is the username
	private User personUsing;
	
	
	public Model(Database database, User person) {
		/* Initializes the model! Evidentally, just uses
		 * the database to get it's information, aswell as a person
		 * for whoever's using it. */
		fullCourseMap = database.getCourseMap();
		studentMap = database.getStudentMap();
		teacherMap = database.getTeacherMap();
		personUsing = person;
	}
	
	// STUDENT METHODS 
	public HashSet<String> getGradedAssignmentsUSER(String courseName, String studentUsername) {
		/* Gives a hashset of graded assignment names, for a student in a course. */
		Course course = fullCourseMap.get(courseName);
		return course.getGradedAssignmentsUSER(studentUsername);
	}
	
	public HashSet<String> getUngradedAssignmentsUSER(String courseName, String studentUsername) {
		/* Gives a hashset of ungraded assignment names, for a student in a course. */
		Course course = fullCourseMap.get(courseName);
		return course.getUngradedAssignmentsUSER(studentUsername); // I believe no escaping reference here but could someone please confirm?
	}
	
	public ArrayList<String> getStudentList(String courseName) {
		/* Gives an arraylist of the student list in a course.
		 * More specifically, their usernames. */
		ArrayList<Student> students = fullCourseMap.get(courseName).getStudentMap();
		ArrayList<String> studentUsernames = new ArrayList<String>();
		for(Student s: students) {
			studentUsernames.add(s.getUsername());
		}
		return studentUsernames;
	}
	
	public void setAssignmentGraded(String courseName, String assignment) {
		/* Sets an assignment to "graded". Basically a statement saying that all grades are in for it. */
		fullCourseMap.get(courseName).setGraded(assignment);
	}
	
	public double getCourseAverage(String courseName, String stu) {
		/* Returns the average of a student in a course. */
		return fullCourseMap.get(courseName).getCourseAverage(stu);
	}
	
	public double calculateGPA(String studentUsername) {
		/* Returns the GPA of a student. */
		Student student = studentMap.get(studentUsername);
		return student.calculateGPA();
	}

	public ArrayList<String> getCurCoursesStudent(String studentUsername) {
		/* Returns the course names that a student is in. */
		return studentMap.get(studentUsername).getCurCourses();
	}

	public ArrayList<String> getCompletedCoursesStudent(String studentUsername) {
		/* Returns the course names of completed courses that the student was in. */
		return studentMap.get(studentUsername).getCompletedCourses();
	}

	// TEACHER METHODS
	public HashSet<String> getGradedAssignments(String courseName) {
		/* Returns every assignment in a course that was marked as graded. */
		Course course = fullCourseMap.get(courseName);
		return course.getGradedAssignments();
	}
	
	public HashSet<String> getUngradedAssignments(String courseName) {
		/* Returns every assignment in a course not marked as graded. */
		Course course = fullCourseMap.get(courseName);
		return course.getUngradedAssignments();
	}
	
	public ArrayList<String> getCurCoursesTeacher(String teacherUsername) {
		/* Returns every course that a teacher is in. */
		return teacherMap.get(teacherUsername).getCurCourses();
	}

	public ArrayList<String> getCompletedCoursesTeacher(String teacherUsername) {
		/* Returns every course that a teacher had completed. */
		return teacherMap.get(teacherUsername).getCompletedCourses();
	}

	public boolean canCalculateStudentCurAverage(String course, String stu) {
		/* Checks if a student is in a course, even if the name is a little on the nose */
		if(fullCourseMap.get(course).getStudentMap().contains(studentMap.get(stu))) {
			return true;
		}
		return false;
	}

	public void addAssignment(String courseName, String assignmentName, String assignmentCategory) {
		/* Adds an assignment to a course. Needs the name and catagory. */
		fullCourseMap.get(courseName).addAssignment(assignmentName, assignmentCategory);
		ArrayList<Double> weights = fullCourseMap.get(courseName).getWeights();
		ArrayList<Integer> drops = fullCourseMap.get(courseName).getDrops();
		this.calculateClassAverage(fullCourseMap.get(courseName).getModeChosen(), courseName, weights, drops);
	}
	
	public boolean removeAssignment(String courseName, String assignmentName) {
		/* removes an assignment from a course. Just needs the name. */
		return fullCourseMap.get(courseName).removeAssignment(assignmentName);
	}
	
	public String addStudent(String username, String coursename) {
		/* Adds a student to a course. Has a lot of error handling. */
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
		/* Removes a student from a course. Also has a lot of error handling. */
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
		/* Returns a string of all enrolled students in a course. */
		Course course = fullCourseMap.get(coursename);
		if (course == null) return ("Course not found.");
		// Uses an iterator
		Iterator<Student> studentIter = course.getEnrolledStudents();
		String holdFormat = "";
		while (studentIter.hasNext()) {
			Student student = studentIter.next(); // Apparently, you have to do this first. Hopefully.
			holdFormat = holdFormat + student.getPrintFormatted() + "\n";
		}
		return holdFormat.strip();
		
	}
	
	public String addGradeForAssignment(String stuNameHolder, String assignNameHolder, double gradeNumHolderDoub, String courseName) {
		/* Adds a grade to a student for an assignment. Checks to make sure it's valid before doing so. */
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
 		 /* Calculates the averages and medians on an assignment.
 		  * Also makes sure it's a valid check. */
		 Course course = fullCourseMap.get(courseName);
         if (course == null) {
             return "Course not found.";
         }
         Assignment assignment = course.getAssignmentsMap().get(assignmentName);
         if (assignment == null) {
             return "Assignment not found.";
         }
         ArrayList<Double> grades = course.getGradesForAssignment(assignmentName);
         // This only looks for the existing grades
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
        	 // Proper way of getting a median in an even length list
             median = (grades.get(size / 2 - 1) + grades.get(size / 2)) / 2.0;
         } else {
             median = grades.get(size / 2);
         }
         median = (median / assignment.getMaxGrade()) * 100;
         
         return String.format("Average on %s is %.2f%% and median is %.2f%%", assignmentName, average, median);
 	}
	
	public double calculateStudentCurAverage(String studentUsername) {
		/* Return's a student's current average across courses. */
		return studentMap.get(studentUsername).calculateCurAverage();
	}
	
	public ArrayList<Student> sortByFirstName(String courseName) {
		/* Returns an arraylist of students... But everything here is COPIED,
		 * and it's NOT how it's stored internally. The courses inside
		 * are also COPIED, and so are the assignments!
		 * In more relevant news, it also sorts it before returning it, by
		 * the first name of the student.*/
		Course course = fullCourseMap.get(courseName);
		if(course == null) {
			return null;
		}
		
		ArrayList<Student> students = course.getStudentMapCopier();
		Collections.sort(students, new Comparator<Student>() {
	        @Override
	        public int compare(Student s1, Student s2) {
	            return s1.getFirstName().compareToIgnoreCase(s2.getFirstName());
	        }
	    });
		
	    return students;
	}
	
	public ArrayList<Student> sortByLastName(String courseName) {
		/* Returns an arraylist of students... But everything here is COPIED,
		 * and it's NOT how it's stored internally. The courses inside
		 * are also COPIED, and so are the assignments!
		 * In more relevant news, it also sorts it before returning it, by
		 * the last name of the student.*/
		Course course = fullCourseMap.get(courseName);
		if(course == null) {
			return null;
		}
		
		ArrayList<Student> students = course.getStudentMapCopier();
		Collections.sort(students, new Comparator<Student>() {
	        @Override
	        public int compare(Student s1, Student s2) {
	            return s1.getLastName().compareToIgnoreCase(s2.getLastName());
	        }
	    });
		
		return students;
	}
	
	public boolean checkCompleted(String courseName) {
		/* Returns if a course is completed or not. */
		if(this.getUngradedAssignments(courseName).size() > 0) {
			return false;
		}
		return true;
	}
	
	public void completeCourse(String courseName) {
		/* Marks a course as completed, or at least, tries to. */
		fullCourseMap.get(courseName).completeCourse();
	}
	
	public ArrayList<Student> sortByUserName(String courseName) {
		/* Returns an arraylist of students... But everything here is COPIED,
		 * and it's NOT how it's stored internally. The courses inside
		 * are also COPIED, and so are the assignments!
		 * In more relevant news, it also sorts it before returning it, by
		 * the username of the student.*/
		Course course = fullCourseMap.get(courseName);
		if(course == null) {
			return null;
		}
		
		ArrayList<Student> students = course.getStudentMapCopier();
		Collections.sort(students, new Comparator<Student>() {
	        @Override
	        public int compare(Student s1, Student s2) {
	            return s1.getUsername().compareToIgnoreCase(s2.getUsername());
	        }
	    });
		
		return students;
	}
	
	public ArrayList<Student> sortByGrades(String courseName, String assignmentName) {
		/* Returns an arraylist of students... But everything here is COPIED,
		 * and it's NOT how it's stored internally. The courses inside
		 * are also COPIED, and so are the assignments!
		 * In more relevant news, it also sorts by the grades before
		 * printing it. */
 		Course course = fullCourseMap.get(courseName);
 		if (course == null || course.getAssignmentsMap().get(assignmentName) == null) {
             return null;
         }
 		
 		ArrayList<Student> students = course.getStudentMapCopier();
         Collections.sort(students, new Comparator<Student>() {
             @Override
             public int compare(Student s1, Student s2) {
                 Double grade1 = getStudentGrade(s1.getUsername(), courseName, assignmentName);
                 Double grade2 = getStudentGrade(s2.getUsername(), courseName, assignmentName);
                 // Might be ungraded, so check those.
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
		/* Returns the student's grade on an assignment as a double. */
        Course course = fullCourseMap.get(courseName);
        // Returns null if it's no good
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
		/* Creates groups and puts students into them.
		 * Note that the groups are not saved. */
		ArrayList<Student> studentMap = fullCourseMap.get(courseName).getStudentMap();	
	    HashMap<String, ArrayList<String>> groupMap = new HashMap<>();
	    for (int i = 0; i < num; i++) {
	    	// Every group map starts with this
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
		/* Assigns a final grade to a given course.
		 * Has a lot to cover. */
		Course course = fullCourseMap.get(courseName);
		ArrayList<Double> weights = course.getWeights();
		ArrayList<Integer> drops = course.getDrops();
		HashMap<String, FinalGrade> finalGrades = new HashMap<String, FinalGrade>();
		HashMap<String, Assignment> assignmentMap = course.getAssignmentsMap();
		HashMap<String, Double> tempGrades = new HashMap<String, Double>();
		if(course.getModeChosen() == 1) {
			// First, gets all the grades in an assignment mapped to their students,
			// in one biggol hashmap. It basically just mashes the grades together.
			for( Assignment assignment: assignmentMap.values()) {
				for(String j : assignment.getIdToGrade().keySet()) {
					if(!tempGrades.containsKey(j)) {
						tempGrades.put(j, assignment.getIdToGrade().get(j));
					} else {
						tempGrades.put(j, tempGrades.get(j) + assignment.getIdToGrade().get(j));
					}
				}
			}
			// Then averages them out to get a percentage
			for(String j : tempGrades.keySet()) {
				double tempTotal = tempGrades.get(j) / fullCourseMap.get(courseName).getTotalGrade() * 100;
				finalGrades.put(j, this.calculateGrade(tempTotal));
			}
		} else {
			// Otherwise, it splits them by categories instead.
			HashMap<AssignmentType, ArrayList<Assignment>> tempAssignments = new HashMap<>();
		    for (AssignmentType type : AssignmentType.values()) {
		        tempAssignments.put(type, course.getAssignmentByType(type));
		    }
		    // Has to be a bit more careful with what it does.
		    HashSet<String> allStudentIds = new HashSet<>();
		    for (Assignment assignment : assignmentMap.values()) {
		        allStudentIds.addAll(assignment.getIdToGrade().keySet());
		    }
		    
		    // Goes by student for this as well.
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
		            // We use the drops after a sort, so it always drops the lowest.
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
		            // Then we use weight to determine how much it matters
		            double weight = weights.get(type.ordinal());
		            finalGrade += total * weight / 100.0;
		        }
		        finalGrades.put(stu, this.calculateGrade(finalGrade));
		    }
		}
		// It comes here no matter the path. It then updates every student's grade letters.
		for(String stu : finalGrades.keySet()) {
			studentMap.get(stu).updateStudentGradeLetters(courseName, finalGrades.get(stu));
		}
		// And returns their final grades to be printed
		ArrayList<String> finals = new ArrayList<String>();
		for(String stu : finalGrades.keySet()) {
			String temp = "" + stu + " " +  finalGrades.get(stu);
			finals.add(temp);
			temp = "";
		}
		return finals;
	}
	
	public void calculateClassAverage(int option, String courseName, ArrayList<Double> weights, ArrayList<Integer> drops) {
		/* Calculate's a class's average in a course. Takes weights and drops aswell. */
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
			// (These can very, basically...)
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
			fullCourseMap.get(courseName).setTotalGrade(2*maxGrades[0]+maxGrades[1]+maxGrades[2]+maxGrades[3]+maxGrades[4]);
			fullCourseMap.get(courseName).setModeChosen(1);
			
		} else {
			for (Assignment assignment: assignmentMap.values()) {
				assignment.setMaxGrade(100);
			}
			// Uses other functions to avoid this place from becoming a monster
			fullCourseMap.get(courseName).setModeChosen(2);
			fullCourseMap.get(courseName).setDrop(drops);
			fullCourseMap.get(courseName).setWeight(weights);
		}
	}
	
	public boolean canAssignFinalGrades(String courseName) {
		/* Checks if you can assign the final grades, by seeing if there's any ungraded assignments. */
		if(this.getUngradedAssignments(courseName).size() > 0) {
			return false;
		}
		return true;
	}
	
	public String getCurrentUsersName() {
		return personUsing.getUsername();
	}
	
	public boolean canCreateGroups(String courseName, int num) {
		/* Returns if there's enough students for the group number you want. */
		if(num <= fullCourseMap.get(courseName).getStudentMap().size()) {
			return true;
		}
		return false;
	}
	
	private FinalGrade calculateGrade(Double grade) {
		/* Returns a calculation of the final grade, based on the grade percentage. */
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
		/* Returns if a course has a mode chosen for it. */
		return (fullCourseMap.get(coursename).getModeChosen());
	}
}