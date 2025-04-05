package frontend;

import java.util.HashSet;
import java.util.Scanner;

import backend.*;

public class View {
	public static void main(String[] args) {
		startGradebook();
	}
	
	public static void startGradebook() {

		Model model = new Model();
		// do your importing here


		Scanner scanner =  new Scanner(System.in);
		boolean state =  true;
		
		while(state) {
			System.out.println("\nWelcome to Gradebook!\n");
			System.out.println("Are you a student or a teacher?\n");
			System.out.println("1. Student");
			System.out.println("2. Teacher");
			System.out.println("3. Import Course List (debug)");
			System.out.println("0. EXIT");
			
			String choice =  scanner.nextLine().strip();
			
			if(choice.equals("1")) {
				System.out.println("Enter your username: ");
				String username = scanner.nextLine().strip();
				showStudentMenu(scanner, model, username);

			} else if(choice.equals("2")) {
				System.out.println("Enter your username: ");
				String teacherUsername = scanner.nextLine().strip();
				showTeacherMenu(scanner, teacherUsername, model);
			} else if (choice.equals("3")) {
				System.out.println("Please give filename (excluding .txt): ");
				String filename = scanner.nextLine().strip() + ".txt";
				model.createCourses(filename);	
			} else if(choice.equals("0")) {
                		System.out.println("Thank you for using Gradebook. Goodbye!");
                		state = false;
			} else {
				System.out.println("Invalid choice. Please try again.");
			}
		}
		
		scanner.close();
		
	}
	
	public static void showStudentMenu(Scanner scanner, Model model, String studentUsername) {
		boolean studentState = true;
		
		while(studentState) {
            		System.out.println("\nStudent Menu:\n");
			System.out.println("1. To view courses");
			System.out.println("2. To view assignments");
			System.out.println("3. To get GPA");
			System.out.println("0. Return to main menu");
			
			String choice = scanner.nextLine().strip();
			String courseName;
			
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
					showStudentMenu(scanner, model, studentUsername);
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

	public static void showTeacherMenu(Scanner scanner, String teacherUsername, Model model) {
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
		boolean teacherState = true;
		
		switch(choice) {
			case "1":
				viewCompletedCourses(model, teacherUsername);
				break;
			case "2":
				viewCurrentCourses(model, teacherUsername);
				break;
			case "3":
				// addAssignment(model, username);
				break;
			case "4":
				// removeAssignment(model, username);
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
				// sortStudentsByName(model, username);
				break;
			case "12":
				// sortStudentsByLastName(model, username);
				break;
			case "13":
				// sortStudentsByUsername(model, username);
				break;
			case "14":
				// sortStudentsByAssignment(model, username);
				break;
			case "15":
				// putStudentsInGroups(model, username);
				break;	
			case "16":
				// assignFinalGrades(model, username);
				break;
			case "17":
				// viewUngradedAssignments(model, username);
				break;
			case "18":
				// chooseModeForClassAverages(model, username);
				break;
			case "0":
				teacherState = false;
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
				showTeacherMenu(scanner, teacherUsername, model);
				break;
		}
	}

	public static void viewCompletedCourses(Model model, String teacherUsername) {
		HashSet<String> completedCourses = model.getCompletedCoursesTeacher(teacherUsername);
		for (String course : completedCourses) {
			System.out.println(course);
		}
	}
	public static void viewCurrentCourses(Model model, String teacherUsername) {
		HashSet<String> currentCourses = model.getCurCoursesTeacher(teacherUsername);
		for (String course : currentCourses) {
			System.out.println(course);	
		}
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