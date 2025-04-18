package frontend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import backend.*;

public class View {
	public View(Model model, String role) {
		startGradebook(model, role);
	}
	
	public static void startGradebook(Model model, String role) {
		
		// Model comes imported with all the courses
		

		Scanner scanner =  new Scanner(System.in);
		boolean running =  true;
		if (role.equals("teacher")) showTeacherMenu(scanner, model);
		else showStudentMenu(scanner, model);		
	}
	
	
	// view for the student
	public static void showStudentMenu(Scanner scanner, Model model) {
		boolean studentState = true;
		String studentUsername = model.getCurrentUsersName();
		System.out.println("Welcome " + studentUsername);
		String courseName = ""; // we'll want this soon probably
		while(studentState) {
            System.out.println("\nStudent Menu:\n");
			System.out.println("1. To view courses");
			System.out.println("2. To view assignments");
			System.out.println("3. To get GPA");
			System.out.println("0. Return to main menu");
			
			String choice = scanner.nextLine().strip();
			
			switch(choice) {
				case "1":
					viewCourses(scanner, model, studentUsername);
					break;
				case "2":
					System.out.println("Enter course name: ");
					courseName = scanner.nextLine().strip();
					viewAssignments(scanner, model, courseName);
					break;
				case "3":
					System.out.println("Enter course name: ");
					courseName = scanner.nextLine().strip();
					getClassAverage(model, courseName);
					break;
				case "4":
					getGpa(model, studentUsername);
					break;
				case "0":
					studentState = false;
					break;
				default:
					System.out.println("Invalid choice. Please try again.");
					//showStudentMenu(scanner, model, studentUsername);
					break;
			}
		}
	}
	
	public static void viewCourses(Scanner scanner, Model model, String studentUsername) {
		System.out.println();
		System.out.println("Course Menu:");
		System.out.println();
		System.out.println("1. To view current courses");
		System.out.println("2. To view completed courses");
		System.out.println("3. Get class average");
		
		String choice = scanner.nextLine().strip();
		
		switch(choice) {
			case "1":
				System.out.println("Current courses:");
				HashSet<String> courses = model.getCurCoursesStudent(studentUsername);
				for (String course : courses) {
					System.out.println(course);
				}
				break;
				
			case "2":
				System.out.println("Completed courses:");
				HashSet<String> completedCourses = model.getCompletedCoursesStudent(studentUsername);
				for (String course : completedCourses) {
					System.out.println(course);
				}
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
				break;
		}
	}
	
	public static void viewAssignments(Scanner scanner, Model model, String courseName) {
		System.out.println("1. To view graded assignments");
		System.out.println("2. To view ungraded assignments");
		
		String choice = scanner.nextLine().strip();
		
		if(choice.equals("1")) {
			System.out.println("Graded assignments:");
			HashSet<String> gradedAssignments = model.getGradedAssignments(courseName);
			for (String assignment : gradedAssignments) {
				System.out.println(assignment);
			}

		} else if(choice.equals("2")) {
			System.out.println("Ungraded assignments:");
			HashSet<String> ungradedAssignments = model.getUngradedAssignments(courseName);
			for (String assignment : ungradedAssignments) {
				System.out.println(assignment);
			}

		} else {
			System.out.println("Invalid choice. Please try again.");
		}
	}
	
	public static void getClassAverage(Model model, String courseName) {
		double avg = model.getCourseAverage(courseName);
		System.out.println("Class average for " + courseName + ": " + avg);
	}

	public static void getGpa(Model model, String username) {
		double gpa = model.calculateGPA(username);
		System.out.println("Student " + username + " GPA: " + gpa);
	}

	
	// view for the teacher
	public static void showTeacherMenu(Scanner scanner, Model model) {
		boolean teacherState = true;
		String teacherUsername = model.getCurrentUsersName();
		System.out.println("Welcome " + teacherUsername);
		String courseName = ""; // we'll want this soon probably
		
		
		boolean modeChosen = true;
		int mode;
		if(!modeChosen) {
			mode = chooseMode(model, teacherUsername, scanner);
			if(mode > 0) {
				modeChosen = true;
			}
		}
		
		// COMMENT BY FATIH: In the future, we need to put this into a relevant place, but for not it's good.
		ArrayList<String> courses = model.getCurCoursesTeacher(teacherUsername);
		System.out.println("Your current courses:");
		int j = 1;
		for(String course: courses) {
			System.out.println(j + ") " + course);
			j++;
		}
		System.out.print("Enter a course name: ");
		
		String nameCourse = scanner.nextLine();
		
		nameCourse = courses.get(Integer.parseInt(nameCourse.split(" ")[0]) - 1);

		if(!courses.contains(nameCourse)) {
			System.out.println("The course " + nameCourse + " does not exist.");
			teacherState = false;
		} else {
			courseName = nameCourse;
		}
		
		while (teacherState) {
			System.out.println("\nTeacher Menu:\n");
			System.out.println("1. View completed courses)");
			System.out.println("2. View current courses");
			System.out.println("3. Add assignment to a course");
			System.out.println("4. Remove assignment from a course");
			System.out.println("5. Add student to a course");
			System.out.println("6. Remove student from a course");
			System.out.println("7. Import list of students from file");
			System.out.println("8. View students enrolled in a course");
			System.out.println("8. Add grades for students for an assignment");
			System.out.println("9. Calculate class averages and medians on assignments");
			System.out.println("10. Calculate a student's current average");
			System.out.println("11. Sort students by first name");
			System.out.println("12. Sort students by last name");
			System.out.println("13. Sort students by username");
			System.out.println("14. Sort students by grades on an assignment");
			System.out.println("15. Put students in groups");
			System.out.println("16. Assign final grades (A, B, C, D, E) to students");
			System.out.println("17. View ungraded assignments");
			System.out.println("18. Choose a mode for calculating class averages");
			System.out.println("0. Return to main menu");
			
			String choice = scanner.nextLine().strip();
			
			switch(choice) {
				case "1":
					System.out.println("Completed courses: ");
					viewCompletedCourses(model, teacherUsername);
					break;
				case "2":
					System.out.println("Current courses: ");
					viewCurrentCourses(model, teacherUsername);
					break;
				case "3":
					System.out.println("What's the name of the assignment you want to add?");
					String assignmentName = scanner.nextLine().strip();
					
					System.out.println("Select the category");
					String assignmentCategory = scanner.nextLine().strip();
					addAssignment(model, courseName, assignmentName, assignmentCategory);
					break;
				case "4":
					System.out.println("What's the name of the assignment you want to remove?");
					String assignmentNameToRemove = scanner.nextLine().strip();
					
					System.out.println("Select the category");
					String assignmentCategoryToRemove = scanner.nextLine().strip();
					removeAssignment(model, courseName, assignmentNameToRemove, assignmentCategoryToRemove);
					break;
				case "5":
					addStudent(model, teacherUsername, scanner);
					break;
				case "6":
					removeStudent(model, teacherUsername, scanner);
					break;
				case "7":
					// importStudents(model, username);
					break;	
				case "8":
					viewStudentsEnrolled(model, teacherUsername, scanner);
					break;
				case "9":
					// addGrades(model, username);
					break;
				case "10":
					System.out.println("Enter student username: ");
					String studentUsername = scanner.nextLine().strip();
					calculateStudentCurAverage(model, studentUsername);
					break;
				case "11":
					System.out.println("Enter course name: ");
					String courseNameForSortFirst = scanner.nextLine().strip();
					ArrayList<Student> sortedStudentsByFirst = model.sortByFirstName(courseNameForSortFirst);
					if (sortedStudentsByFirst == null) {
				        System.out.println("Course not found.");
				    } else if (sortedStudentsByFirst.isEmpty()) {
				        System.out.println("No students enrolled in " + courseNameForSortFirst);
				    } else {
				        System.out.println("Students sorted by first name:");
				        for (Student student : sortedStudentsByFirst) {
				            System.out.println(student.getPrintFormatted());
				        }
				    }
					break;
				case "12":
					System.out.println("Enter course name: ");
					String courseNameForSortLast = scanner.nextLine().strip();
					ArrayList<Student> sortedStudentsByLast = model.sortByFirstName(courseNameForSortLast);
					if (sortedStudentsByLast == null) {
				        System.out.println("Course not found.");
				    } else if (sortedStudentsByLast.isEmpty()) {
				        System.out.println("No students enrolled in " + courseNameForSortLast);
				    } else {
				        System.out.println("Students sorted by last name:");
				        for (Student student : sortedStudentsByLast) {
				            System.out.println(student.getPrintFormatted());
				        }
				    }
					break;
				case "13":
					System.out.println("Enter course name: ");
					String courseNameForSortUser = scanner.nextLine().strip();
					ArrayList<Student> sortedStudentsByUser= model.sortByFirstName(courseNameForSortUser);
					if (sortedStudentsByUser == null) {
				        System.out.println("Course not found.");
				    } else if (sortedStudentsByUser.isEmpty()) {
				        System.out.println("No students enrolled in " + courseNameForSortUser);
				    } else {
				        System.out.println("Students sorted by username:");
				        for (Student student : sortedStudentsByUser) {
				            System.out.println(student.getPrintFormatted());
				        }
				    }
					break;
				case "14":
					// sortStudentsByAssignment(model, username);
					break;
				case "15":
					// putStudentsInGroups(model, username);
					System.out.println("How many groups do you want: ");
					String num = scanner.nextLine().strip();
					if(isNumeric(num)) {
						if(model.canCreateGroups(courseName, Integer.parseInt(num))) {
							HashMap<String, ArrayList<String>> groups = model.putInGroups(courseName, Integer.parseInt(num));
							for(String key : groups.keySet()) {
								System.out.println(key + ":");
								for(int i = 0; i < groups.get(key).size(); i++) {
									System.out.println("" + (i+1) +" "+ groups.get(key).get(i));
								}
							}
						}
						else {
							System.out.println("Cannot create groups. Number of groups is more than number of students.");
						}
					} else {
						System.out.println("Your input is invalid");
					}
					break;	
				case "16":
					// assignFinalGrades(model, username);
					break;
				case "17":
					System.out.println("Enter course name: ");
					String viewUngradedAssignments = scanner.nextLine().strip();
					HashSet<String> ungradedAssignments= model.getUngradedAssignments(viewUngradedAssignments);
					if (ungradedAssignments == null || ungradedAssignments.isEmpty()) {
				        System.out.println("Ungraded assignments not found.");
				    } else {
				        System.out.println("Ungraded assignments:");
				        for (String assignment : ungradedAssignments) {
				            System.out.println(assignment);
				        }
				    }
					break;
				case "18":
					// chooseModeForClassAverages(model, username);
					ArrayList<String> courses1 = model.getCurCoursesTeacher(teacherUsername);
					if(courses1.size() == 0) {
						System.out.println("You are not teaching any course");
					} else {
						System.out.println("Your current courses:");
						int i = 1;
						for(String course: courses1) {
							System.out.println(i + ") " + course);
							i++;
						}
						System.out.print("Enter a course name: ");
						String nameCourse1 = scanner.nextLine();
						if(!courses.contains(nameCourse1)) {
							System.out.println("The course " + nameCourse1 + " does not exist.");
						} else {
							System.out.println();
							System.out.println("Modes for calculating class averages:");
							System.out.println("1) Final Grade = Total Points Earned/Total Points Possible");
							System.out.println("2) The final grade is based on categories and percentages");
							System.out.print("Choose an option: ");
							//model.addTeacher(teacherUsername); // for debugging!
							String option = scanner.nextLine();
							
							System.out.println("Courses: \n" + courses + "\n"); // for debugging
							if(option.strip().equals("1")) {
								// Final Grade = Total Points Earned/Total Points Possible
								//model.calculateClassAverage(1, courseName, teacherUsername);
								
								
							} else if(option.strip().equals("2")) {
								// The final grade is based on categories and percentages
								// Set up categories
								System.out.println("Mode 2 debug");
							} else {
								System.out.println("Invalid input!");
							}
						}
					}

					break;
				case "0":
					teacherState = false;
					break;
				default:
					System.out.println("Invalid choice. Please try again.");
					//showTeacherMenu(scanner, teacherUsername, model);
					break;
			}
		}
	}
	
	private static int chooseMode(Model model, String teacherUsername, Scanner scanner) {
		ArrayList<String> courses = model.getCurCoursesTeacher(teacherUsername);
		if(courses.size() == 0) {
			System.out.println("You are not teaching any course");
		} else {
			System.out.println("Your current courses:");
			int i = 1;
			for(String course: courses) {
				System.out.println(i + ") " + course);
				i++;
			}
			System.out.print("Enter a course name: ");
			String nameCourse = scanner.nextLine();
			if(!courses.contains(nameCourse)) {
				System.out.println("The course " + nameCourse + " does not exist.");
			} else {
				System.out.println();
				System.out.println("Modes for calculating class averages:");
				System.out.println("1) Final Grade = Total Points Earned/Total Points Possible");
				System.out.println("2) The final grade is based on categories and percentages");
				System.out.print("Choose an option: ");
				//model.addTeacher(teacherUsername); // for debugging!
				String option = scanner.nextLine();
				
				System.out.println("Courses: \n" + courses + "\n"); // for debugging
				if(option.strip().equals("1")) {
					// Final Grade = Total Points Earned/Total Points Possible
					//model.calculateClassAverage(1, courseName, teacherUsername);
					model.calculateClassAverage(1, nameCourse);					
					return 1;
					
				} else if(option.strip().equals("2")) {
					// The final grade is based on categories and percentages
					// Set up categories
					model.calculateClassAverage(2, nameCourse);
					return 2;
				} else {
					System.out.println("Invalid input!");
				}
			}
		}
		return 0;
	}

	
	private static boolean isNumeric(String num) {
		/* This functions as an easy way to see if what the user gave
		 * is in fact an integer without crashing the program.
		 * Useful for having them pick an option via number. */
		boolean numeric = true;
        try {
            Integer.parseInt(num);
        } catch (NumberFormatException e) {
            numeric = false;
        }
        return numeric;
	}
	public static void viewCompletedCourses(Model model, String teacherUsername) {
		ArrayList<String> completedCourses = model.getCompletedCoursesTeacher(teacherUsername);
		for (String course : completedCourses) {
			System.out.println(course);
		}
	}
	public static void viewCurrentCourses(Model model, String teacherUsername) {
		ArrayList<String> currentCourses = model.getCurCoursesTeacher(teacherUsername);
		for (String course : currentCourses) {
			System.out.println(course);	
		}
	}
	
	public static void addAssignment(Model model, String courseName, String assignmentName, String assignmentType) {
		model.addAssignment(courseName, assignmentName, assignmentType);
		System.out.println("Assignment \"" + assignmentName + "\" of type " + assignmentType +
                " has successfully been added to course \"" + courseName + "\".");
	}
	
	public static void removeAssignment(Model model, String courseName, String assignmentName, String assignmentType) {
		model.removeAssignment(courseName, assignmentName, assignmentType);
		System.out.println("Assignment \"" + assignmentName + "\" of type " + assignmentType +
                " has successfully been removed from the course \"" + courseName + "\".");
	}

	public static void calculateStudentCurAverage(Model model, String studentUsername) {
		double avg = model.calculateStudentCurAverage(studentUsername);
		System.out.println("Student " + studentUsername + " average: " + avg);
	}
	
	public static void addStudent(Model model, String username, Scanner scanner) {
		System.out.println("Student Username: "); // username is not what we want btw, but might be used l8r
		String choiceName = scanner.nextLine().strip();
		System.out.println("Course name: "); // This is using PARAMETER method. Delete this comment if it becomes irrelevant.
		String choiceCourse = scanner.nextLine().strip();
		System.out.println(model.addStudent(choiceName, choiceCourse));
	}
	
	public static void removeStudent(Model model, String username, Scanner scanner) {
		System.out.println("Student Username: "); // username is not what we want btw, but might be used l8r
		String choiceName = scanner.nextLine().strip();
		System.out.println("Course name: "); // This is using PARAMETER method. Delete this comment if it becomes irrelevant.
		String choiceCourse = scanner.nextLine().strip();
		System.out.println(model.removeStudent(choiceName, choiceCourse));
	}
	
	public static void viewStudentsEnrolled(Model model, String username, Scanner scanner) {
		System.out.println("Course name: "); // This is using PARAMETER method. Delete this comment if it becomes irrelevant.
		String choiceCourse = scanner.nextLine().strip();
		System.out.println(model.getEnrolledStudents(choiceCourse));
	}
	
	public static void importStudents(Model model, String username, Scanner scanner) {
		System.out.println("Course name: "); // This is using PARAMETER method. Delete this comment if it becomes irrelevant.
		String choiceCourse = scanner.nextLine().strip();
		System.out.println("File Name (exclude .txt): "); // This is using PARAMETER method. Delete this comment if it becomes irrelevant.
		String choiceFileName = scanner.nextLine().strip() + ".txt";
		model.importStudentList(choiceCourse, choiceFileName);
	}

}