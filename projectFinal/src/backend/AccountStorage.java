package backend;

import java.util.HashMap;

public class AccountStorage {
	private HashMap<Integer, User> accountList;
	
	public AccountStorage() {
		accountList = new HashMap<Integer, User>();
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
