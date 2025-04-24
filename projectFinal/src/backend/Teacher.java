/**************************************************************
 * Author: Davranbek Kadirimbetov, Benjamin Kanter,
 * 		   Fatih Bozdogan, & Behruz Ernazarov
 * Description: Simulates a teacher! Wow, there really isn't much
 * here. In fact, teachers only get usernames!
 **************************************************************/

package backend;

public class Teacher extends User {
	/* This class is basically just User but with
	 * cool paint. The distinction is helpful though,
	 * so it's not as redundant as it seems. */
	public Teacher(String user) {
		/* Basically just user's constructor. */
		super(user);
	}
	
	// JSON METHODS
	// As in, we don't use these, but the json needs them to exist...
	private Teacher() {};
}