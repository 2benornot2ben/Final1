package frontend;

import java.util.Scanner;

import backend.*;

public class View {
	public static void main(String[] args) {
		startGradebook();
	}
	
	public static void startGradebook() {
		Scanner scanner =  new Scanner(System.in);
		boolean state =  true;
		
		while(state) {
			System.out.println("\nWelcome to Gradebook!\n");
			System.out.println("Are you a student or a teacher?\n");
			System.out.println("1. Student");
			System.out.println("2. Teacher");
			System.out.println("0. EXIT");
			
			String choice =  scanner.nextLine().strip();
			
			if(choice.equals("1")) {
				showStudentMenu(scanner);
			}else if(choice.equals("2")) {
				showTeacherMenu(scanner);
			} else if(choice.equals("0")) {
                		System.out.println("Thank you for using Gradebook. Goodbye!");
                		state = false;
			}else {
				System.out.println("Invalid choice. Please try again.");
			}
		}
		
		scanner.close();
		
	}
	
	public static void showStudentMenu(Scanner scanner) {
		boolean studentState = true;
		
		while(studentState) {
            		System.out.println("\nStudent Menu:\n");
			System.out.println("1. To view courses");
			System.out.println("2. To view assignments");
			System.out.println("3. To get class average");
			System.out.println("4. To get GPA");
			System.out.println("0. Return to main menu");
			
			String choice = scanner.nextLine().strip();
			
			switch(choice) {
				case "1":
					viewCourse(scanner);
					break;
				case "2":
					viewAssignment(scanner);
					break;
				case "3":
					getClassAverage();
					break;
				case "4":
					getGpa();
					break;
				case "0":
					studentState = false;
					break;
				default:
					System.out.println("Invalid choice. Please try again.");
					showStudentMenu(scanner);
					break;
			}
		}
	}
	
	public static void viewCourse(Scanner scanner) {
		System.out.println("1. To view current courses");
		System.out.println("2. To view completed courses");
		
		String choice = scanner.nextLine().strip();
		
		if(choice.equals("1")) {
			// return current courses;
		} else if(choice.equals("2")) {
			// return completed courses;
		} else {
			System.out.println("Invalid choice. Please try again.");
		}
	}
	
	public static void viewAssignment(Scanner scanner) {
		System.out.println("1. To view graded assignments");
		System.out.println("2. To view ungraded assignments");
		
		String choice = scanner.nextLine().strip();
		
		if(choice.equals("1")) {
			// return graded assignments;
		} else if(choice.equals("2")) {
			// return ungraded assignments;
		}else {
			System.out.println("Invalid choice. Please try again.");
		}
	}
	
	public static void getClassAverage() {
		// return class average;
	}

	public static void getGpa() {
		// return gpa;
	}

	public static void showTeacherMenu(Scanner scanner) {
        	System.out.println("\nTeacher Menu:\n");
	}
}