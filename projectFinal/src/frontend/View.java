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
		System.out.println("\nWelcome " + studentUsername);
		String courseName = ""; // we'll want this soon probably
		while(studentState) {
            System.out.println("\nStudent Menu:\n");
			System.out.println("1. To view courses");
			System.out.println("2. To view assignments");
			System.out.println("3. To get course average");
			System.out.println("4. To get GPA");
			System.out.println("0. Return to main menu");
			
			String choice = scanner.nextLine().strip();
			
			switch(choice) {
				case "1":
					viewCourses(scanner, model, studentUsername);
					break;
				case "2":
					System.out.println("Enter course name: ");
					courseName = scanner.nextLine().strip();
					viewAssignments(scanner, model, courseName, studentUsername);
					break;
				case "3":
					System.out.println("Enter course name: ");
					courseName = scanner.nextLine().strip();
					getClassAverage(model, courseName, studentUsername);
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
		
		String choice = scanner.nextLine().strip();
		
		switch(choice) {
			case "1":
				System.out.println("Current courses:");
				ArrayList<String> courses = model.getCurCoursesStudent(studentUsername);
				for (String course : courses) {
					System.out.println(course);
				}
				break;
				
			case "2":
				System.out.println("Completed courses:");
				ArrayList<String> completedCourses = model.getCompletedCoursesStudent(studentUsername);
				for (String course : completedCourses) {
					System.out.println(course);
				}
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
				break;
		}
	}
	
	public static void viewAssignments(Scanner scanner, Model model, String courseName, String stuUsername) {
		System.out.println("1. To view graded assignments");
		System.out.println("2. To view ungraded assignments");
		
		String choice = scanner.nextLine().strip();
		
		if(choice.equals("1")) {
			System.out.println("Graded assignments:");
			HashSet<String> gradedAssignments = model.getGradedAssignmentsUSER(courseName, stuUsername);
			for (String assignment : gradedAssignments) {
				System.out.println(assignment);
			}

		} else if(choice.equals("2")) {
			System.out.println("Ungraded assignments:");
			HashSet<String> ungradedAssignments = model.getUngradedAssignmentsUSER(courseName, stuUsername);
			for (String assignment : ungradedAssignments) {
				System.out.println(assignment);
			}

		} else {
			System.out.println("Invalid choice. Please try again.");
		}
	}
	
	public static void getClassAverage(Model model, String courseName, String stu) {
		double avg = model.getCourseAverage(courseName, stu);
		System.out.println("Class average for " + courseName + ": " + avg);
	}

	public static void getGpa(Model model, String username) {
		double gpa = model.calculateGPA(username);
		if(gpa == 0) {
			System.out.println("Student " + username + " does not have GPA yet.");
		} else {
			System.out.println("Student " + username + " GPA: " + gpa);
		}
	}

	
	// view for the teacher
	public static void showTeacherMenu(Scanner scanner, Model model) {
		boolean teacherState = true;
		String teacherUsername = model.getCurrentUsersName();
		System.out.println("Welcome " + teacherUsername);
		String courseName = ""; // we'll want this soon probably
		boolean skip = false;
		
		// COMMENT BY FATIH: In the future, we need to put this into a relevant place, but for not it's good.
		ArrayList<String> courses = model.getCurCoursesTeacher(teacherUsername);
		System.out.println("\nYour current courses:");
		int j = 1;
		for(String course: courses) {
			System.out.println(j + ") " + course);
			j++;
		}
		System.out.print("Select the course (Enter a number): ");
		String numCourse;
		while(true) {
			numCourse = scanner.nextLine();
			if(isNumeric(numCourse)) {
				if((Integer.parseInt(numCourse.split(" ")[0]) - 1) < courses.size()) {
					numCourse = courses.get(Integer.parseInt(numCourse.split(" ")[0]) - 1);
					break;
				} else {
					System.out.println("Wrong input");
					System.out.print("Select the course (Enter a number): ");
				}
				
			} else {
				System.out.println("Wrong input");
				System.out.print("Select the course (Enter a number): ");
			}
		}
		if(!courses.contains(numCourse)) {
			System.out.println("The course " + numCourse + " does not exist.");
			teacherState = false;
		} else {
			courseName = numCourse;
		}
		
		if ((model.isSetUp(courseName) == 0)) {
			chooseMode(model, teacherUsername, scanner, courseName);
		}
		
		while (teacherState) {
			if (skip) {
				System.out.println("You may now enter a new command, or enter something else to show the list again.");
				System.out.println("(IE: Type a letter, or just enter nothing.)");
			} else {
				System.out.println("\nTeacher Menu:\n");
				System.out.println("1. View completed courses");
				System.out.println("2. View current courses");
				System.out.println("3. Add assignment to a course");
				System.out.println("4. Remove assignment from a course");
				System.out.println("5. Add student to a course");
				System.out.println("6. Remove student from a course");
				System.out.println("7. View students enrolled in a course");
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
				System.out.println("0. Return to main menu");
				skip = true;
			}
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
					addStudent(model, teacherUsername, scanner, courseName);
					break;
				case "6":
					removeStudent(model, teacherUsername, scanner, courseName);
					break;	
				case "7":
					viewStudentsEnrolled(model, teacherUsername, courseName);
					break;
				case "8":
					boolean gradedFully = true;
					System.out.println("Ungraded assignments for " + courseName);
					HashSet<String> currentAssignments = model.getUngradedAssignments(courseName);
					if (currentAssignments == null || currentAssignments.isEmpty()) {
				        System.out.println("None, course completed");
				        break;
				    } else {
				        for (String assignment : currentAssignments) {
				            System.out.println(assignment);
				        }
				    }
					System.out.println("Enter assignment name: ");
					String assignNameHolder = scanner.nextLine().strip();
					while(!currentAssignments.contains(assignNameHolder)) {
						System.out.println("Enter assignment name: ");
						assignNameHolder = scanner.nextLine().strip();
					}
					ArrayList<String> studentUsernames = model.getStudentList(courseName);
					for(int k = 0; k < studentUsernames.size(); k++) {
						String stuNameHolder = studentUsernames.get(k);
						System.out.print("Enter grade in percentage(1-100) for " + stuNameHolder + ": ");
						String gradeNumHolder = scanner.nextLine().strip();
						if (isNumeric(gradeNumHolder)) {
							double gradeNumHolderDoub = Double.parseDouble(gradeNumHolder);
							while(gradeNumHolderDoub > 100 || gradeNumHolderDoub < 0) {
								System.out.print("Enter grade in percentage(1-100) for " + stuNameHolder + ": ");
								gradeNumHolder = scanner.nextLine().strip();
								gradeNumHolderDoub = Double.parseDouble(gradeNumHolder);
							}
							addGrades(model, stuNameHolder, assignNameHolder, gradeNumHolderDoub, scanner, courseName);
						} else {
							System.out.println("Invalid grade...");
							gradedFully = false;
							break;
						}
					}	
					if(gradedFully) {
						model.setAssignmentGraded(courseName, assignNameHolder);
						if(model.checkCompleted(courseName)) {
							model.completeCourse(courseName);
						}
					}
					break;
				case "9":
					
					// Calculates class averages and medians on an assignment

	                System.out.println("Enter assignment name: ");

	                String assignmentNameForStats = scanner.nextLine().strip();

	                String statsResult = model.calculateStats(courseName, assignmentNameForStats);

	                System.out.println(statsResult);

	                break;
				case "10":
					System.out.println("Enter student username: ");
					String studentUsername = scanner.nextLine().strip();
					calculateStudentCurAverage(courseName,model, studentUsername);
					break;
				case "11":
					
					// Sorts students by their First Name 
					
					ArrayList<Student> sortedStudentsByFirst = model.sortByFirstName(courseName);
					if (sortedStudentsByFirst == null) {
				        System.out.println("Course not found.");
				    } else if (sortedStudentsByFirst.isEmpty()) {
				        System.out.println("No students enrolled in " + courseName);
				    } else {
				        System.out.println("Students sorted by first name:");
				        for (Student student : sortedStudentsByFirst) {
				            System.out.println(student.getPrintFormatted());
				        }
				    }
					break;
				case "12":
					
					// Sorts students by their Last Name 
					
					ArrayList<Student> sortedStudentsByLast = model.sortByLastName(courseName);
					if (sortedStudentsByLast == null) {
				        System.out.println("Course not found.");
				    } else if (sortedStudentsByLast.isEmpty()) {
				        System.out.println("No students enrolled in " + courseName);
				    } else {
				        System.out.println("Students sorted by last name:");
				        for (Student student : sortedStudentsByLast) {
				            System.out.println(student.getPrintFormatted());
				        }
				    }
					break;
				case "13":
					
					// Sorts students by their Username
					
					ArrayList<Student> sortedStudentsByUser= model.sortByUserName(courseName);
					if (sortedStudentsByUser == null) {
				        System.out.println("Course not found.");
				    } else if (sortedStudentsByUser.isEmpty()) {
				        System.out.println("No students enrolled in " + courseName);
				    } else {
				        System.out.println("Students sorted by username:");
				        for (Student student : sortedStudentsByUser) {
				            System.out.println(student.getPrintFormatted());
				        }
				    }
					break;
				case "14":
					
					// Sorts students by their grades on an assignment  
					
					System.out.println("Enter the assingment: ");
					String assignmentNameForSort = scanner.nextLine().strip();

					ArrayList<Student> sortedStudentsByGrade = model.sortByGrades(courseName, assignmentNameForSort);

					if(sortedStudentsByGrade == null) {	
				        System.out.println("Course or assingment not found.");

					}else if (sortedStudentsByGrade.isEmpty()) {
				        System.out.println("No students enrolled in " + courseName); 

					} else {
						System.out.println("Students sorted by grades on " + assignmentNameForSort + ":");

	                    for (Student student : sortedStudentsByGrade) {
	                    	Double grade = model.getStudentGrade(student.getUsername(), courseName, assignmentNameForSort);

	                        String gradeDisplay;

	                        if (grade != null) {
	                            gradeDisplay = String.format("%.2f", grade);
	                        } else {
	                            gradeDisplay = "Not graded";
	                        }
	                        System.out.println(student.getPrintFormatted() + " - Grade: " + gradeDisplay);
	                    }
					}
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
					if(model.canAssignFinalGrades(courseName)) {
						ArrayList<String> finalGrades = model.assignFinalGrade(courseName);
						for(int i = 0; i < finalGrades.size(); i++) {
							System.out.println(finalGrades.get(i));
						}
					} else {
						System.out.println("You cannot assign final grades because the course is not completed");
					}
					break;
				case "17":
					
					// To view ungraded assignments on the course
					HashSet<String> ungradedAssignments= model.getUngradedAssignments(courseName);
					if (ungradedAssignments == null || ungradedAssignments.isEmpty()) {
				        System.out.println("Ungraded assignments not found.");
				    } else {
				        System.out.println("Ungraded assignments:");
				        for (String assignment : ungradedAssignments) {
				            System.out.println(assignment);
				        }
				    }
					break;
				case "0":
					teacherState = false;
					break;
				default:
					System.out.println("Invalid choice. Please try again.");
					skip = false;
					break;
			}
		}
	}
	
	private static void addGrades(Model model, String stuNameHolder, String assignNameHolder, double gradeNumHolderDoub,
			Scanner scanner, String courseName) {
		System.out.println(model.addGradeForAssignment(stuNameHolder, assignNameHolder, gradeNumHolderDoub, courseName));
		
	}

	private static void chooseMode(Model model, String teacherUsername, Scanner scanner, String nameCourse) {
		System.out.println("\nYou need to choose mode for calculating class average.");
		ArrayList<String> courses = model.getCurCoursesTeacher(teacherUsername);
		if(!courses.contains(nameCourse)) {
			System.out.println("The course " + nameCourse + " does not exist.");
		} else {
			System.out.println();
			System.out.println("Modes for calculating class averages:");
			System.out.println("1) Final Grade = Total Points Earned/Total Points Possible");
			System.out.println("2) The final grade is based on categories and percentages");
			System.out.print("Choose an option: ");
			String option = scanner.nextLine();
			if(option.strip().equals("1")) {
				// Final Grade = Total Points Earned/Total Points Possible
				ArrayList<Double> weights = new ArrayList<Double>();
				ArrayList<Integer> drops = new ArrayList<Integer>();
				model.calculateClassAverage(1, nameCourse, weights, drops);	
				
			} else if(option.strip().equals("2")) {
				// The final grade is based on categories and percentages
				// Set up categories
				String[] assignmentTypes = {"Midterms", "Final Exam", "Quizes", "Homeworks", "Projects"};
				System.out.println("\nAssign weights for following categories:");
				ArrayList<Double> weights = new ArrayList<Double>();
				for(int j = 0; j < 5; j++) {
					System.out.print("Enter a weight for " + assignmentTypes[j] + ": ");
					String weight = scanner.nextLine();
					while(!isNumeric(weight)) {
						// further validation needed (what if total weight > 100)
						System.out.println("Enter a valid number: ");
						weight = scanner.nextLine();
					}
					double tempWeight = Double.parseDouble(weight);
					weights.add(tempWeight);
				}
				ArrayList<Integer> drops = new ArrayList<Integer>();
				drops.add(0);
				drops.add(0);
				System.out.println("Now you need to enter number of drops for assignments.");
				for(int j = 2; j < 5; j++) {
					System.out.print("Enter a number of drops for " + assignmentTypes[j] + ": ");
					String drop = scanner.nextLine();
					while(!isNumeric(drop)) {
						// further validation needed (what if total weight > 100)
						System.out.println("Enter a valid number: ");
						drop = scanner.nextLine();
					}
					int tempDrop = Integer.parseInt(drop);
					drops.add(tempDrop);
				}
				model.calculateClassAverage(2, nameCourse, weights, drops);
			} else {
				System.out.println("Invalid input!");
			}
		}
	}

	
	private static boolean isNumeric(String num) {
		/* This functions as an easy way to see if what the user gave
		 * is in fact an integer without crashing the program.
		 * Useful for having them pick an option via number. */
		boolean numeric = true;
        try {
            Double.parseDouble(num);
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

	public static void calculateStudentCurAverage(String course,Model model, String studentUsername) {
		if(model.canCalculateStudentCurAverage(course,studentUsername)) {
			double avg = model.calculateStudentCurAverage(studentUsername);
			System.out.println("Student " + studentUsername + " average: " + avg);
		} else {
			System.out.println("Student does not exist in this course");
		}
		
	}
	
	public static void addStudent(Model model, String username, Scanner scanner, String courseName) {
		System.out.println("Student Username: "); // username is not what we want btw, but might be used l8r
		String choiceName = scanner.nextLine().strip();
		System.out.println(model.addStudent(choiceName, courseName));
	}
	
	public static void removeStudent(Model model, String username, Scanner scanner, String courseName) {
		System.out.println("Student Username: "); // username is not what we want btw, but might be used l8r
		String choiceName = scanner.nextLine().strip();
		System.out.println(model.removeStudent(choiceName, courseName));
	}
	
	public static void viewStudentsEnrolled(Model model, String username, String courseName) {
		System.out.println(model.getEnrolledStudents(courseName));
	}

}