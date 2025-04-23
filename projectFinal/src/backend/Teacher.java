package backend;

public class Teacher extends User {
	public Teacher(String user) {
		super(user);
	}
	
	// JSON METHODS
	// As in, we don't use these, but the json needs them to exist...
	private Teacher() {};
}