package backend;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class Database {
	private HashMap<String, User> accountList; 
	private HashMap<String, Assignment> assignmentMap;
	private HashMap<String, Student> studentMap;
	private HashMap<String, Teacher> teacherMap;
	private HashMap<String, Course> courseMap;
	private ArrayList<String> fileNames;
	
	public Database(AccountStorage storage) throws FileNotFoundException {
		accountList = new HashMap<String, User>();
		assignmentMap = new HashMap<String, Assignment>();
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
	
	// this creates the file names for each course
	private void createFileNames() {
		try (Scanner scanLine = new Scanner(new File("courses.txt"))){
			while(scanLine.hasNextLine()){
				// format we get filenames from
				String line = scanLine.nextLine();
				String courseFile = line.strip() + ".txt";
				fileNames.add(courseFile);
			}
			scanLine.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	private void readCourses() throws FileNotFoundException {
		HashMap<String, Student> tempStudentMap = new HashMap<String, Student>();
		
		
		// go over each course txt file
		for(int i=0; i<fileNames.size(); i++) {
			
			// go over the contents of each file
			File myFile = new File(fileNames.get(i));
			Scanner myReader = new Scanner(myFile);
			String header = myReader.nextLine();
			String[] headerInfo = header.split(",");
			String courseName = headerInfo[0];
			String teacherName = headerInfo[1];
			
			while(myReader.hasNextLine()){
				String studentLine = myReader.nextLine();
				String studentInfo[] = studentLine.split(",");
				String name = studentInfo[0];
				String last = studentInfo[1];
				String username = studentInfo[2];
				
				tempStudentMap.put(username, studentMap.get(username));	
			}
			Course course = new Course(courseName);
			course.setTeacher(teacherMap.get(teacherName));
			
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
			
			
			tempStudentMap = new HashMap<String, Student>();
			myReader.close();
		}
	}
	
	// create the teachers and put them into the teacher map
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
}