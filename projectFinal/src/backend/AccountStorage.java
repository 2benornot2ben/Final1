package backend;

import java.util.HashMap;
import java.util.Base64;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import frontend.View;

public class AccountStorage {
	private HashMap<String, User> accountList;
	private HashMap<String, String> privateData;
	private HashMap<String, Model> modelMap;

	public AccountStorage() {
		modelMap = new HashMap<String, Model>();
		privateData = new HashMap<String, String>();
	}

	protected void setAccountList(HashMap<String, User> accountList) {
		this.accountList = accountList;
	}

	public void setAccount(String username, String password, Database database, boolean isTeacher) {
		String hash = hashPassword(password);
		privateData.put(username, hash);
		User holdUser = database.returnCorrectUser(username);
		Model myModel = new Model(database, holdUser);
		modelMap.put(username, myModel);
	}

	public boolean userExist(String user, String profession) {
		if (profession.equals("teacher") && !(accountList.get(user) instanceof Teacher)) return false;
		if (profession.equals("student") && !(accountList.get(user) instanceof Student)) return false;
		return accountList.containsKey(user);
	}

	public boolean needPassword(String user) {
		return !privateData.containsKey(user);
	}

	public boolean canLogIn(String user, String password) {
		if (!privateData.containsKey(user)) {
			return false;
		}
		String storedHash = privateData.get(user);
		return verifyPassword(password, storedHash);
	}

	public void openModel(String user, String password, String person) {
		if (canLogIn(user, password)) {
			Model getModel = modelMap.get(user);
			View newView = new View(getModel, person);
		}
	}

	private String hashPassword(String password) {
		Argon2 argon2 = Argon2Factory.create();
		try {
			return argon2.hash(3, 65536, 1, password.toCharArray()); 
		} finally {
			argon2.wipeArray(password.toCharArray());
		}
	}

	private boolean verifyPassword(String password, String storedHash) {
		Argon2 argon2 = Argon2Factory.create();
		return argon2.verify(storedHash, password.toCharArray());
	}

	protected HashMap<String, User> packingAccountStorage() {
		return accountList;
	}

	protected HashMap<String, String> packingPrivateStorage() {
		return privateData;
	}

	protected void unpackingJson(HashMap<String, User> accountList, HashMap<String, String> privateData,
			Database database) {
		this.accountList = accountList;
		this.privateData = privateData;
		for (String names : privateData.keySet()) {
			User holdUser = database.returnCorrectUser(names);
			Model myModel = new Model(database, holdUser);
			modelMap.put(names, myModel);
		}
	}
}