package backend;

import java.util.HashMap;

public class AccountStorage {
	private HashMap<String, User> accountList; // The string is username.
	
	public AccountStorage() {
		accountList = new HashMap<String, User>();
	}
	
	// This is meant for IMPORTING
	protected AccountStorage(HashMap<String, User> accountList) {
		this.accountList = new HashMap<String, User>(accountList);
	}
	
	public void createAccount() {
		// creates an account
	}
	
	public void logIntoAccount() {
		// log in to an existiog account
	}
	
	public void hashPassword() {
		// uses security to hash the password
	}
}