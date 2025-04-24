package backend;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


public class Database {
	private HashMap<String, User> accountList;
	@JsonIgnore
	private HashMap<String, Student> studentMap;
	@JsonIgnore
	private HashMap<String, Teacher> teacherMap;
	private HashMap<String, Course> courseMap;
	@JsonIgnore
	private ArrayList<String> fileNames;
	private HashMap<String, String> privateData; // Not worth initializing, for now.
	
	public Database(AccountStorage storage) throws FileNotFoundException {
		accountList = new HashMap<String, User>();
		studentMap = new HashMap<String, Student>();
		teacherMap = new HashMap<String, Teacher>();
		courseMap = new HashMap<String, Course>();
		fileNames = new ArrayList<String>();
		createFileNames();
		readTeachers();
		readStudents();
		readCourses();
		storage.setAccountList(accountList);
	}
	
	// How is this ok.
	public HashMap<String, Course> getCourseMap() {
		return courseMap;
	}
	
	public HashMap<String, Teacher> getTeacherMap(){
		return teacherMap;
	}
	
	public HashMap<String, Student> getStudentMap(){
		return studentMap;
	}
	
	protected User returnCorrectUser(String username) {
		return accountList.get(username);
	}
	
	private void createFileNames() {
		try (Scanner scanLine = new Scanner(new File("courses.txt"))){
			while(scanLine.hasNextLine()){
				// format we get filenames from
				String line = scanLine.nextLine();
				String albumFile = line.strip() + ".txt";
				fileNames.add(albumFile);
			}
			scanLine.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	private void readCourses() throws FileNotFoundException {
		HashMap<String, Student> tempStudentMap = new HashMap<String, Student>();
		HashMap<String, Assignment> tempAssignmentMap = new HashMap<String, Assignment>();
		for(int i=0; i<fileNames.size(); i++) {
			File myFile = new File(fileNames.get(i));
			Scanner myReader = new Scanner(myFile);
			String header = myReader.nextLine();
			String[] headerInfo = header.split(",");
			String courseName = headerInfo[0];
			String teacherName = headerInfo[1];
			
			while(myReader.hasNextLine()){
				String line = myReader.nextLine();
				if(line.split(",")[0].equals("s")) {
					String studentInfo[] = line.split(",");
					String name = studentInfo[1];
					String last = studentInfo[2];
					String username = studentInfo[3];
					tempStudentMap.put(username, studentMap.get(username));
				} else {
					String assignmentInfo[] = line.strip().split(",");
					String name = assignmentInfo[1];
					String type = assignmentInfo[2];
					tempAssignmentMap.put(name, new Assignment(name, convertToEnums(type)));
				}
					
			}
			Course course = new Course(courseName);
			course.setTeacher(teacherMap.get(teacherName));
			// there might be an error.
			course.setAssignmentMap(tempAssignmentMap);
			// there might be an error.
			course.setStudentMap(tempStudentMap);
						
			for (Student j : tempStudentMap.values()) {
				if (j != null) {
					j.addCourse(course);
				}
			}
			teacherMap.get(teacherName).addCourse(course);
			//assignment also should be here
			courseMap.put(courseName, course);
			tempAssignmentMap = new HashMap<String, Assignment>();
			tempStudentMap = new HashMap<String, Student>();
			myReader.close();
		}
	}
	
	private void readTeachers() throws FileNotFoundException{
		File myFile = new File("teachers.txt");
		Scanner myReader = new Scanner(myFile);
		while(myReader.hasNextLine()) {
			String teacherUsername = myReader.nextLine();
			Teacher holdTea = new Teacher(teacherUsername);
			teacherMap.put(teacherUsername, holdTea);
			accountList.put(teacherUsername, holdTea);
		}
	}
	
	private void readStudents() throws FileNotFoundException{
		File myFile = new File("students.txt");
		Scanner myReader = new Scanner(myFile);
		while(myReader.hasNextLine()) {
			String studentLine = myReader.nextLine();
			String studentInfo[] = studentLine.split(",");
			String name = studentInfo[0];
			String last = studentInfo[1];
			String username = studentInfo[2];
			Student holdStu = new Student(name, last, username);
			studentMap.put(username, holdStu);
			accountList.put(username, holdStu);
		}
	}
	
	private AssignmentType convertToEnums(String category) {
        switch (category) {
	        case "MIDTERM":
	            return AssignmentType.MIDTERM;
	        case "FINAL":
	            return AssignmentType.FINAL;
	        case "QUIZ":
	            return AssignmentType.QUIZ;
	        case "HW":
	            return AssignmentType.HW;
	        case "PROJECT":
	            return AssignmentType.PROJECT;
	        default:
	            throw new IllegalArgumentException("Unknown assignment type: " + category);
        }
	}
	
	public void updateForPacking(AccountStorage storage) {
		this.accountList = storage.packingAccountStorage();
		this.privateData = storage.packingPrivateStorage();
		for (Course course : courseMap.values()) {
			course.packUpReferences();
		}
	}
	
	// To be ran after a json makes this...
	public void updateUnpacking(AccountStorage storage) {
		studentMap = new HashMap<String, Student>();
		teacherMap = new HashMap<String, Teacher>();
		fileNames = new ArrayList<String>();
		for (User i : accountList.values()) {
			if (i instanceof Student) {
				studentMap.put(i.getUsername(), (Student) i);
			} else if (i instanceof Teacher) {
				teacherMap.put(i.getUsername(), (Teacher) i);
			}
			i.unPackReferences(courseMap);
		}
		for (Course i : courseMap.values()) {
			i.unPackReferences(studentMap, teacherMap);
		}
		storage.unpackingJson(accountList, privateData, this); // LMAO i can do that??
		
	}
	
	public void JsonConversion(String filename) {
		for (User i : accountList.values()) {
			i.packUpReferences();
		}
		for (Course i : courseMap.values()) {
			i.packUpReferences();
		}
		try {
			//ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			//ow.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			//String json = ow.writeValueAsString(storage);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		    objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		    //String json = objectMapper.writeValueAsString(new MyDtoNoAccessors());
		    String json = objectMapper.writeValueAsString(this);
			
			File file = new File(filename);
            // If the file doesn't exist, create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(json);
            bufferedWriter.close();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/* You'd like to know more about json, right? I can tell you.
	 * Or atleast, tell you enough so you can roughly explain what's going on here.
	 * First, know that json does horribly with pointers. You'll notice that every time I want to
	 * refer to an object which has more than 1 reference, I keep some main class as it's only actual reference,
	 * and the rest are some text that can be interpreted by code to MAKE a pointer after the json is done loading.
	 * Also, it demands METHOD NAMES. Those json methods at the bottom of some files? They will break if you
	 * change their name! I think. (Might be based on just var names)
	 * Oh yes, the subtypes in User. It's nearly identical to adding a var - it's stored the same, actually.
	 * But when it reads the file, it also considers the typing thing at the top, and if it finds a matching
	 * "var" to it, then it goes to where it declares. Very simple & surprisingly primitive, but works.
	 * PS: JSON typically looks like a complex structure because of its normal format. But uh, this one
	 * doesn't have the format, so if you open it up it'll look very bunched up. But once you look a bit
	 * into it, you'll see that it's just a million hashmaps! That's right! That's all json is!
	 * Just a hashmap who's key is a value name, and value is the... Well, value!
	 * Everything is just that! Literally, every single thing.
	 */
	
	// JSON METHODS
	// As in, we don't use these, but the json needs them to exist...
	private Database() {};
	
	@JsonSetter
	private void setAccountList(HashMap<String, User> accountList) {
		this.accountList = accountList;
	}
	
	@JsonSetter
	private void setPrivateData(HashMap<String, String> privateData) {
		this.privateData = privateData;
	}
	
	
	@JsonSetter
	private void setCourseMap(HashMap<String, Course> courseMap) {
		this.courseMap = courseMap;
	}
}