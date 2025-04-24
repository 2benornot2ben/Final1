package backend;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

public class DatabaseTest {

	@Test
	public void test() {
		AccountStorage ac = new AccountStorage();
		try {
			Database db = new Database(ac);
			db.JsonConversion("savedata");
			db.updateForPacking(ac);
			db.updateUnpacking(ac);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
