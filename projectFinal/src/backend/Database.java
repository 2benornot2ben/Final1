/**************************************************************
 * Author: Davranbek Kadirimbetov, Benjamin Kanter,
 * 		   Fatih Bozdogan, & Behruz Ernazarov
 * Description: Holds all the non-duplicated stuff, and handles
 * loading from files. This includes both loading from txt
 * and json.
 **************************************************************/

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
	/* The database of every object in the program!
	 * Except for assignments, those are in the courses.
	 * You'll notice we only save 3 for saving to json: Account list,
	 * Course map, and private data (which is just a string). That's all you need
	 * to reconstruct this program's data! (Plus assignments).
	 */
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
		/* Complex constructor. Initalizes everything, then runs the
		 * four private methods to figure everything out. Expects
		 * there to be a students.txt, teachers.txt, and courses.txt. The
		 * courses.txt will contain the name of the courses, in .txt form aswell.
		 * Alternatively, loading from json is... Not done here, check below.
		 */
		accountList = new HashMap<String, User>();
		studentMap = new HashMap<String, Student>();
		teacherMap = new HashMap<String, Teacher>();
		courseMap = new HashMap<String, Course>();
		fileNames = new ArrayList<String>();
		// This one is used for readCourses
		createFileNames();
		readTeachers();
		readStudents();
		readCourses();
		storage.setAccountList(accountList);
	}
	
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
		// Returns the user when asked for a username.
		return accountList.get(username);
	}
	
	private void createFileNames() {
		/* Checks for a courses.txt. If found, it will 
		 * read the courses and save them as file names, to be
		 * read later.
		 */
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
		/* Uses the filenames we got earlier to make courses.
		 * Note that the courses use a specific format for their data. */
		HashMap<String, Student> tempStudentMap = new HashMap<String, Student>();
		HashMap<String, Assignment> tempAssignmentMap = new HashMap<String, Assignment>();
		for(int i=0; i<fileNames.size(); i++) {
			File myFile = new File(fileNames.get(i));
			Scanner myReader = new Scanner(myFile);
			String header = myReader.nextLine();
			// First line is specific to the course itself
			String[] headerInfo = header.split(",");
			String courseName = headerInfo[0];
			String teacherName = headerInfo[1];
			
			while(myReader.hasNextLine()){
				String line = myReader.nextLine();
				// Student
				if(line.split(",")[0].equals("s")) {
					String studentInfo[] = line.split(",");
					String name = studentInfo[1];
					String last = studentInfo[2];
					String username = studentInfo[3];
					tempStudentMap.put(username, studentMap.get(username));
				// Assignment
				} else {
					String assignmentInfo[] = line.strip().split(",");
					String name = assignmentInfo[1];
					String type = assignmentInfo[2];
					tempAssignmentMap.put(name, new Assignment(name, convertToEnums(type)));
				}
					
			}
			// We do it like this, to avoid duplicated references...
			Course course = new Course(courseName);
			course.setTeacher(teacherMap.get(teacherName));
			course.setAssignmentMap(tempAssignmentMap);
			course.setStudentMap(tempStudentMap);
						
			// Same here, but for the other objects.
			for (Student j : tempStudentMap.values()) {
				if (j != null) {
					j.addCourse(course);
				}
			}
			teacherMap.get(teacherName).addCourse(course);
			courseMap.put(courseName, course);
			// We reset these, since it's a loop.
			tempAssignmentMap = new HashMap<String, Assignment>();
			tempStudentMap = new HashMap<String, Student>();
			myReader.close();
		}
	}
	
	private void readTeachers() throws FileNotFoundException{
		/* Reads the teachers from teachers.txt. Teachers don't
		 * have a lot of info, so it's not that interesting. */
		File myFile = new File("teachers.txt");
		Scanner myReader = new Scanner(myFile);
		while(myReader.hasNextLine()) {
			String teacherUsername = myReader.nextLine();
			Teacher holdTea = new Teacher(teacherUsername);
			teacherMap.put(teacherUsername, holdTea);
			accountList.put(teacherUsername, holdTea);
		}
		myReader.close();
	}
	
	private void readStudents() throws FileNotFoundException{
		/* Reads the students from students.txt. Students have a decent
		 * amount of information to go over, but otherwise it's quite
		 * similar to above. */
		File myFile = new File("students.txt");
		Scanner myReader = new Scanner(myFile);
		while(myReader.hasNextLine()) {
			String studentLine = myReader.nextLine();
			String studentInfo[] = studentLine.split(",");
			// It's always in this order.
			String name = studentInfo[0];
			String last = studentInfo[1];
			String username = studentInfo[2];
			Student holdStu = new Student(name, last, username);
			studentMap.put(username, holdStu);
			accountList.put(username, holdStu);
		}
		myReader.close();
	}
	
	private AssignmentType convertToEnums(String category) {
		/* Converts a string to enums.
		 * This one actually DOES care for capitalization. */
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
	        // This should never happen
	        default:
	            throw new IllegalArgumentException("Unknown assignment type: " + category);
        }
	}
	
	public void updateForPacking(AccountStorage storage) {
		/* Packs up the database to be exported to json.
		 * Specifically, it just gets some details from the accountstorage
		 * (as they are not updated in real time), and also tells
		 * the courses to prepare for json-ification. */
		this.accountList = storage.packingAccountStorage();
		this.privateData = storage.packingPrivateStorage();
		for (Course course : courseMap.values()) {
			course.packUpReferences();
		}
	}
	
	// To be ran after a json makes this...
	public void updateUnpacking(AccountStorage storage) {
		/* Unpacks the database, assuming a json was loaded.
		 * The accountlist is directly accessed by json, so we load
		 * the teacher & studentmap from there.
		 * Same for the courses (we still need to tell them that
		 * they were just loaded, though).
		 * We then finally tell the accountstorage what happened,
		 * and give it what it needs.
		 */
		studentMap = new HashMap<String, Student>();
		teacherMap = new HashMap<String, Teacher>();
		fileNames = new ArrayList<String>();
		// Resolving student & teacher map, also informs
		for (User i : accountList.values()) {
			if (i instanceof Student) {
				studentMap.put(i.getUsername(), (Student) i);
			} else if (i instanceof Teacher) {
				teacherMap.put(i.getUsername(), (Teacher) i);
			}
			i.unPackReferences(courseMap);
		}
		// Informing courses
		for (Course i : courseMap.values()) {
			i.unPackReferences(studentMap, teacherMap);
		}
		// Informing accountstorage (and giving it important data)
		storage.unpackingJson(accountList, privateData, this); // I can't believe I can do that
		
	}
	
	public void JsonConversion(String filename) {
		/* Actually makes something into a json using
		 * jackson! Kind of complicated. */
		// Tells the accounts & courses they're about to be converted
		for (User i : accountList.values()) {
			i.packUpReferences();
		}
		for (Course i : courseMap.values()) {
			i.packUpReferences();
		}
		try {
			// Makes and configures an object mapper
			ObjectMapper objectMapper = new ObjectMapper();
			// Required to avoid errors
			objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			// Convenient for getting private details
		    objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		    // We get the string version of the json here
		    String json = objectMapper.writeValueAsString(this);
			
			File file = new File(filename);
            // If the file doesn't exist, create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file);
            // Supposedly faster
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