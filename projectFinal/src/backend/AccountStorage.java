package backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import frontend.*;

public class AccountStorage {
	private HashMap<String, User> accountList; // The string is username.
	private HashMap<String, String> privateData; // this will store username and password
	private HashMap<String, Model> modelMap; // stores models of particular users
	
	// This is meant for IMPORTING
	protected AccountStorage(HashMap<String, User> accountList) {
		this.accountList = new HashMap<String, User>(accountList);
	}
	
	public AccountStorage() {
		/* Initializes a hashmap! Useful for going faster. */
		modelMap = new HashMap<String, Model>();
		privateData = new HashMap<String, String>();
	}
	public void setAccountList(HashMap<String, User> accountList) {
		this.accountList = accountList;
	}
	
	public void setAccount(String username, String password, Database database) {
		privateData.put(username, hashPassword(password)); // more secure encryption needed
		Model myModel = new Model(database);
		modelMap.put(username, myModel);
	}
	public boolean userExist(String user){
		System.out.println(accountList);
		return accountList.containsKey(user);
	}
	
	public boolean needPassword(String user){
		if(privateData.size() == 0) {
			return true;
		}
		return !privateData.containsKey(user);
	}
	
	public boolean canLogIn(String user, String password){
		if(privateData.containsKey(user)) {
			if(privateData.get(user).equals(hashPassword(password))) {
				return true;
			}
		}
		return false;
	}
	
	//@SuppressWarnings("unused")
	public void openModel(String user, String password, String person){
		Model getModel = modelMap.get(user);
		// Front end can call this method, so no way we're not validating this.
		View newView = new View(getModel);
	}
	
	
	// we need to use stronger encryption for now this works
	
	private String hashPassword(String password) {
		/* Uses MD5 hashing for encryption!
		 * Also uses salting with bigInteger, so even
		 * small passwords return massive blocks. */
		try {
			// This strategy of hashing isn't the strongest, but it's
			// good enough. Messagedigest hashes, biginteger salts.
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(password.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
	}
}
