package backend;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.HashMap;

import org.junit.Test;

public class AccountStorageTest {

	@Test
	public void test1() {
		AccountStorage ac = new AccountStorage();
		Database db = null;
		HashMap<String, User> accountList = null;
		try {
			db = new Database(ac);
			ac.setAccount("Fatih", "1234",db, false);
			ac.setAccount("Fatih", "1234",db, true);
			accountList = new HashMap<>();
			accountList.put("dlee", new User());
			ac.setAccountList(accountList);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ac.userExist("Fatih", "student");
		ac.userExist("Fatih", "teacher");
		ac.userExist("Fatih", "hi");
		
		ac.needPassword("Fatih");
		
		ac.canLogIn("Fatih", "1234");
		
		// ac.openModel("Fatih", "1234", "student");
		ac.packingAccountStorage();
		ac.packingPrivateStorage();
		
		HashMap<String, String> privateData = new HashMap<>();
		privateData.put("Fatih", "1234");
		
		// ac.openModel("Fatih", "1234", "student"); // work on this once you finish model
		
		ac.unpackingJson(accountList, privateData, db);

		
	}
	
	

}
