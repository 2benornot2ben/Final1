/**************************************************************
 * Author: Davranbek Kadirimbetov, Benjamin Kanter,
 * 		   Fatih Bozdogan, & Behruz Ernazarov
 * Description: Simulates a grade book! Has what you'd expect,
 * ie grade manipulation, assignments, teachers & students,
 * and so on. It also features the ability to load from a
 * json file (closest thing to saving we get), and secure accounts
 * encrypted with Argon2! I feel like I trust this program with
 * my passwords more than I trust the University of Arizona with
 * properly encrypting them.
 **************************************************************/

package frontend;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

import backend.*;

public class Main {
	public static void main(String[] args) throws FileNotFoundException {
		/* This function actually runs the program. It asks you to load from a json / txt,
		 * then makes proper materials to run the back end, but without risking escaping
		 * references and the such. This also lets you log in (view) and save to json. */
		
		Scanner getInput = new Scanner(System.in);
		String jsonThing;
		AccountStorage storage;
		Database database;
		System.out.println("Load from json? (y/n)");
		if (getInput.nextLine().toLowerCase().equals("y")) {
			System.out.println("Please give json file name: (Dont include the .json)");
			try {
				// Ah, the json. A bit complicated. We get the json text...
				jsonThing = Files.readString(Paths.get(getInput.nextLine().strip() + ".json"));
				// Map it into the database...
				database = new ObjectMapper().readValue(jsonThing, Database.class);
				storage = new AccountStorage();
				// Then use a custom unpacking method to get what's necessary into the storage.
				database.updateUnpacking(storage);
				System.out.println("Successfully loaded.");
			} catch (IOException e) {
				e.printStackTrace();
				// This is actually the same as if you just loaded from .txt. It's the fallback.
				System.out.println("Error!");
				storage = new AccountStorage();
				database = new Database(storage);
			}
		} else {
			// Requires specific .txt names.
			System.out.println("Loading from txt...");
			storage = new AccountStorage();
			database = new Database(storage);
		}
		boolean running = true;
		while(running) {
			System.out.println("Who are you?");
			System.out.println("1) Student");
			System.out.println("2) Teacher");
			// This would not exist in real life... Probably. I guess we technically
			// do have saving now, so it COULD? But it probably wouldn't.
			// Who cares, just take the leak prevention & be cool with it.
			System.out.println("3) Exit completely (Prevents a resource leak)");
			System.out.print("Enter an option you want: ");
			String option = getInput.nextLine();
			// We use numbers for nearly this entire program.
			if(option.split(" ")[0].equals("1")) {
				System.out.println("You are about to log into your student account");
				System.out.print("Enter a username: ");
				String username = getInput.nextLine();
				// The user must exist to log in. We can't make more students out of thin air.
				if(storage.userExist(username, "student")) {
					if(storage.needPassword(username)) {
						// When you first log in, you are asked to create your password. This will be
						// saved for future use, even after converting to and back from json.
						System.out.print("Create your password: ");
						String password = getInput.nextLine();
						storage.setAccount(username, password, database, false);
						System.out.println("Logging in to your account.");
						// We then try to log in, which SHOULD be a guaranteed success.
						storage.openModel(username, password, "student");	
					} else {
						// Now you need the previously input password. Note that passwords
						// don't have to be unique (but usernames do).
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
				// The user must exist to log in. We can't make more teachers out of thin air.
				if(storage.userExist(username, "teacher")) {
					if(storage.needPassword(username)) {
						// When you first log in, you are asked to create your password. This will be
						// saved for future use, even after converting to and back from json.
						System.out.print("Create your password: ");
						String password = getInput.nextLine();
						storage.setAccount(username, password, database, true);
						System.out.println("Logging in to your account.");
						// We then try to log in, which SHOULD be a guaranteed success.
						storage.openModel(username, password, "teacher");	
					} else {
						// Now you need the previously input password. Note that passwords
						// don't have to be unique (but usernames do).
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
				String holdInputTwice = "";
				while (running) {
					System.out.println("Would you like to save to json? (y/n)");
					holdInputTwice = getInput.nextLine().strip().toLowerCase();
					if (holdInputTwice.equals("y")) {
						System.out.println("Please give the json file name you want to save to (don't include the .json)");
						// Make sure you've input a valid json file here, of course.
						database.updateForPacking(storage);
						database.JsonConversion(getInput.nextLine().strip() + ".json");
						System.out.println("Converted to json!");
						running = false;
					} else if (holdInputTwice.equals("n")) {
						running = false;
						getInput.close();
						System.out.println("Thanks for using gradebook!");
					} else {
						// We've never had this issue, but i'd rather not have a poor sucker just die here.
						System.out.println("You're very lucky I thought someone might mistype here.");
						System.out.println("But then you'd lose everything when you might not want to. Try again.");
					}
				}
			} else {
				System.out.println("Invalid input.");
			}
		}
	}
}