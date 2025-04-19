package backend;

import java.io.BufferedWriter;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.JsonPointer;

import frontend.*;

public class AccountStorage {

	protected HashMap<String, User> accountList; // The string is username.
	
	protected HashMap<String, String> privateData; // this will store username and password
	
	protected HashMap<String, Model> modelMap; // stores models of particular users
	
	// This is meant for IMPORTING
	protected AccountStorage(HashMap<String, User> accountList, HashMap<String, String> privateData,
			HashMap<String, Model> modelMap) {
		this.accountList = new HashMap<String, User>(accountList);
		this.privateData = new HashMap<String, String>(privateData);
		this.modelMap = new HashMap<String, Model>(modelMap);
	}
	
	public AccountStorage() {
		/* Initializes a hashmap! Useful for going faster. */
		modelMap = new HashMap<String, Model>();
		privateData = new HashMap<String, String>();
	}
	
	protected void setAccountList(HashMap<String, User> accountList) {
		this.accountList = accountList;
	}
	
	private void setPrivateData(HashMap<String, String> privateData) {
		this.privateData = privateData;
	}
	
	private void setModelMap(HashMap<String, Model> modelMap) {
		this.modelMap = modelMap;
	}
	
	public void setAccount(String username, String password, Database database, boolean isTeacher) {
		privateData.put(username, hashPassword(password)); // more secure encryption needed
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
		if(privateData.containsKey(user)) {
			if(privateData.get(user).equals(hashPassword(password))) {
				return true;
			}
		}
		return false;
	}
	
	//@SuppressWarnings("unused")
	public void openModel(String user, String password, String person){
		if (canLogIn(user, password)) {
			Model getModel = modelMap.get(user);
			// Front end can call this method, so no way we're not validating this.
			View newView = new View(getModel, person);
		}
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
	
	public void JsonConversion() {
		try {
			//ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			//ow.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			//String json = ow.writeValueAsString(storage);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		    objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		    //String json = objectMapper.writeValueAsString(new MyDtoNoAccessors());
		    String json = objectMapper.writeValueAsString(this);
			
			File file = new File("savedata.json");
            // If the file doesn't exist, create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(json);
            bufferedWriter.close();

            System.out.println("Done!"); // This is NOT allowed. Change later.
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// FUTURE ME: It's something to do with the students not existing. Logging in as them fixes. -----------------------------------------------
	}
}