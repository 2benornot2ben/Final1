
package backend;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

// Security imports
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import java.security.SecureRandom;
import java.util.Base64;

import frontend.View;

public class AccountStorage {
	private HashMap<String, User> accountList; // The string is username.
	private HashMap<String, String> privateData; // this will store username and password
	private HashMap<String, Model> modelMap; // stores models of particular users
	
	public AccountStorage() {
		/* Initializes a hashmap! Useful for going faster. */
		modelMap = new HashMap<String, Model>();
		privateData = new HashMap<String, String>();
	}
	protected void setAccountList(HashMap<String, User> accountList) {
		this.accountList = accountList;
	}
	
	public void setAccount(String username, String password, Database database, boolean isTeacher) {
        byte[] salt = generateSalt(); // Generate random salt
        String saltBase64 = Base64.getEncoder().encodeToString(salt);
        String hash = hashPassword(password, salt); // Hash password with Argon2
		privateData.put(username, saltBase64 + ":" + hash);
        User holdUser = database.returnCorrectUser(username);
        Model myModel = new Model(database, holdUser);
        modelMap.put(username, myModel);
	}
	
	// if profession doesnt match "student" or "teacher", it checks everyone instead.
	public boolean userExist(String user, String profession) {
		if (profession.equals("teacher") && !(accountList.get(user) instanceof Teacher)) return false;
		if (profession.equals("student") && !(accountList.get(user) instanceof Student)) return false;
		return accountList.containsKey(user);
	}
	
	public boolean needPassword(String user){
		if(privateData.size() == 0) {
			return true;
		}
		return !privateData.containsKey(user);
	}
	
	public boolean canLogIn(String user, String password){	
		if(!privateData.containsKey(user)) {
			return false;
		}

		String storedData =  privateData.get(user);

		String[] parts = storedData.split(":", 2);

        if (parts.length != 2) {
           return false; // Invalid format
        }

        String saltBase64 = parts[0];
        String storedHash = parts[1];
        byte[] salt = Base64.getDecoder().decode(saltBase64);
        String computedHash = hashPassword(password, salt);
        return storedHash.equals(computedHash);
	}
	
	//@SuppressWarnings("unused")
	public void openModel(String user, String password, String person){
		if (canLogIn(user, password)) {
			Model getModel = modelMap.get(user);
			// Front end can call this method, so no way we're not validating this.
			View newView = new View(getModel, person);
		}
	}
	
	

	private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16]; // 128-bit salt
        random.nextBytes(salt);
        return salt;
    }
	
	// we used Agron 2 for password hashing
	
	private String hashPassword(String password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 100000, 256);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
	
	protected HashMap<String, User> packingAccountStorage() {
		return accountList;
	}
	
	protected HashMap<String, String> packingPrivateStorage() {
		return privateData;
	}
	
	// You might wanna use some flyweight something for this, but nrn, im dyin bro
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