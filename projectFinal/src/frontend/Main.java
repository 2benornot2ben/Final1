package frontend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

import backend.*;

public class Main {
	public static void main(String[] args) throws FileNotFoundException {
		/* This function actually runs the program now. It doesn't actually
		 * do the main file manipulation, but it does let the user get there.
		 * Note that it doesn't know anything apart from user input and
		 * knowing where to ask about logging in. */
		
		Scanner getInput = new Scanner(System.in);
		//AccountStorage storage = new AccountStorage();
		//Database database = new Database(storage);
		String bleh;
		AccountStorage storage;
		Database database;
		System.out.println("Load from json? (y/n)");
		if (getInput.nextLine().toLowerCase().equals("y")) {
			try {
				bleh = Files.readString(Paths.get("savedata.json"));
				System.out.println(bleh);
				database = new ObjectMapper().readValue(bleh, Database.class);
				storage = new AccountStorage();
				database.updateUnpacking(storage);
				System.out.println("Successfully loaded.");
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error!");
				storage = new AccountStorage();
				database = new Database(storage);
			}
		} else {
			System.out.println("Loading from txt...");
			storage = new AccountStorage();
			database = new Database(storage);
		}
		boolean running = true;
		while(running) {
			System.out.println("Who are you?");
			System.out.println("1) Student");
			System.out.println("2) Teacher");
			System.out.println("3) Exit completely (Prevents a resource leak)");
			System.out.print("Enter an option you want: ");
			String option = getInput.nextLine();
			if(option.split(" ")[0].equals("1")) {
				System.out.println("You are about to log into your student account");
				System.out.print("Enter a username: ");
				String username = getInput.nextLine();
				if(storage.userExist(username, "student")) {
					if(storage.needPassword(username)) {
						System.out.print("Create your password: ");
						// need a password validation: length > 8, use of capital and small letters, use of special characters
						String password = getInput.nextLine();
						storage.setAccount(username, password, database, false);
						System.out.println("Logging in to your account.");
						storage.openModel(username, password, "student");	
					} else {
						System.out.print("Enter your password: ");
						String password = getInput.nextLine();
						if(storage.canLogIn(username, password)) {
							System.out.println("Logging in to your account.");
							storage.openModel(username, password, "student");
						} else {
							System.out.println("The username/password is incorrect");
						}	
					}
				} else {
					System.out.println("The username/password is incorrect");
				}
			} else if (option.split(" ")[0].equals("2")) {
				System.out.println("You are about to log into your teacher account");
				System.out.print("Enter a username: ");
				String username = getInput.nextLine();
				if(storage.userExist(username, "teacher")) {
					if(storage.needPassword(username)) {
						System.out.print("Create your password: ");
						// need a password validation: length > 8, use of capital and small letters, use of special characters
						String password = getInput.nextLine();
						storage.setAccount(username, password, database, true);
						System.out.println("Logging in to your account.");
						storage.openModel(username, password, "teacher");	
					} else {
						System.out.print("Enter your password: ");
						String password = getInput.nextLine();
						if(storage.canLogIn(username, password)) {
							System.out.println("Logging in to your account.");
							storage.openModel(username, password, "teacher");
						} else {
							System.out.println("The username/password is incorrect");
						}	
					}
				} else {
					System.out.println("The username/password is incorrect");
				}
			} else if (option.split(" ")[0].equals("3")) {
				// This would NOT EXIST in real life. This is just so you
				// can avoid a resource leak!
				running = false;
				getInput.close();
				database.updateForPacking(storage);
				database.JsonConversion();
				System.out.println("Converted to json!");
			} else {
				System.out.println("Invalid input.");
			}
		}
	}
}