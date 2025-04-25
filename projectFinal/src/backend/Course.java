/**************************************************************
 * Author: Davranbek Kadirimbetov, Benjamin Kanter,
 * 		   Fatih Bozdogan, & Behruz Ernazarov
 * Description: Represents a course in java form!
 * Contains a lot of relevant information, like assignments,
 * students, total grades, weights, drops, etc.
 * Interestingly, this is the only class (apart from database)
 * that you'll see holding other classes in the json - it hold assignments.
 * Why? Because nothing else points to assignments, so we can assume
 * no duplicating will happen, and assignments point to nothing,
 * so there's no infinity mirror issue.
 **************************************************************/

package backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Course {
	/* A course object, representing everything in a class!
	 * Like a programming class or.. Whatever! It's the google classroom of
	 * random java projects! Also stores a lot of info. */
	private HashMap<String, Assignment> assignmentMap; // Every assignment has a unique name in each course (but not necessarily across courses)
	private String courseName;
	@JsonIgnore
	private Teacher teacher;
	@JsonIgnore
	private HashMap<String, Student> studentMap; // The string is the username
	private Boolean completed; // If the course is done, basically.
	private double totalGrade;
	private HashMap<AssignmentType,Double> categories; // Assignment Type : Weight of that
	private int modeChosen = 0;
	private ArrayList<Double> weights; // The position in the arraylist determines where it applies
	private ArrayList<Integer> drops; // Drops can only apply to 3 of the categories
	
	// This is for the json to use.
	private String teacherPacked; // Teacher username
	private ArrayList<String> studentPacked; // Student usernames
	
	public Course(String courseName) {
		/* Not so standard constructor. Only needs the course name, the rest
		 * of the stuff set here is practically the "default" values. Remember
		 * that the json uses a different method for loading, so this is okay. */
		this.completed = false;
		this.courseName = courseName;
		assignmentMap = new HashMap<String, Assignment>();
		studentMap = new HashMap<String, Student>();
		categories = new HashMap<>();
		// Standard grade distribution, changeable though.
        categories.put(AssignmentType.MIDTERM, 300.0);
        categories.put(AssignmentType.FINAL,   200.0);
        categories.put(AssignmentType.QUIZ,    125.0);
        categories.put(AssignmentType.HW,      125.0);
        categories.put(AssignmentType.PROJECT, 250.0);
	}
	
	protected Course(Course course) {
		/* Copy constructor for course. Only copies over what is needed
		 * for sorting functions. */
		this.assignmentMap = course.getAssignmentsMapCopy();
		// Yeah, that's... All we need.
	}
	
	protected void setTotalGrade(double total) {
		totalGrade = total;
	}
	
	protected HashMap<String, Assignment> getAssignmentsMapCopy() {
		HashMap<String, Assignment> hashStore = new HashMap<String, Assignment>();
		for (Assignment assign: assignmentMap.values()) {
			hashStore.put(assign.getAssignmentName(), new Assignment(assign));
		}
		return hashStore;
	}
	
	protected Double getTotalGrade() {
		return totalGrade;
	}
	public String getCourseName() {
		return courseName;
	}
	
	public void setStudentMap(HashMap<String, Student> studentMap) {
		this.studentMap = studentMap;
	}
	
	protected void setAssignmentMap(HashMap<String, Assignment> assignmentMap) {
		this.assignmentMap = assignmentMap;
	}
	
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	
	protected ArrayList<Assignment> getAssignmentByType(AssignmentType type){
		/* This method, when given a type, gives you every assignment of that type.
		 * Handy for categorically sorting & certain instructions. */
		ArrayList<Assignment> temp = new ArrayList<Assignment>();
		for(Assignment a : assignmentMap.values()) {
			// Note we need the values, the keys are just strings.
			if(a.getType() == type) {
				temp.add(a);
			}
		}
		return temp;
	}
	
	@Override
	public boolean equals(Object obj) {
		/* A equals method, only checking if they have the same name. Interestingly,
		 * this ALSO accepts as being equal to a string (ie: String equals Coursename).
		 * Actually pretty convenient. */
		if (this == obj) return true;
		// Check if it's a COURSE or STRING
	    if (obj == null || (getClass() != obj.getClass() && !(obj instanceof String))) return false;
	    String initString = "";
	    // If it's a class, we need it's name.
	    if (getClass() == obj.getClass()) initString = ((Course) obj).getCourseName();
	    // Else, assume the name was provided.
	    else initString = (String) obj;
	    // Why are we only using the name? ... Because this would be hell if we needed more details.
	    return courseName.equals(initString);
	}
	
	@Override
	public int hashCode() {
		/* Hashcode override. Uses ONLY the courseName, so no duplicate courses. */
	    return courseName.hashCode();
	}


	public boolean isCompleted() {
		return completed;
	}
	
	public HashSet<String> getGradedAssignments() {
		/* Goes over the assignments, and checks if they're graded.
		 * If they are? Add it to the return set.
		 * Note that we return assignment names, and because they're unique,
		 * a hash set is okay. */
		HashSet<String> gradedAssignments = new HashSet<String>();
		for (String key : assignmentMap.keySet()) {
			if (assignmentMap.get(key).isGraded()) {
				gradedAssignments.add(key);
			}
		}
		return gradedAssignments;
	}
	
	protected void completeCourse() {
		// There is no "uncomplete course", as that makes no sense.
		this.completed = true;
	}
	
	public HashSet<String> getUngradedAssignments() {
		/* Goes over the assignments, and checks if they're ungraded.
		 * If they are? Add it to the return set.
		 * Note that we return assignment names, and because they're unique,
		 * a hash set is okay. */
		HashSet<String> ungradedAssignments = new HashSet<String>();
		for (String key : assignmentMap.keySet()) {
			if (!assignmentMap.get(key).isGraded()) {
				ungradedAssignments.add(key);
			}
		}
		return ungradedAssignments;
	}
	
	protected void setGraded(String assignment) {
		// Sets an assignment object as graded. Again, there is no ungraded setter.
		assignmentMap.get(assignment).graded();
	}
	
	public HashSet<String> getGradedAssignmentsUSER(String username) {
		/* Same as the previous one, but this time, we care about
		 * the USER. Goes over THEIR assignments in the course, and 
		 * returns if THEIR grade exists, not just if the entire
		 * course is graded. */
		HashSet<String> gradedAssignments = new HashSet<String>();
		for (String key : assignmentMap.keySet()) {
			if ((assignmentMap.get(key).getStudentGradeExists(username))) {
				gradedAssignments.add(key);
			}
		}
		return gradedAssignments;
	}
	
	public HashSet<String> getUngradedAssignmentsUSER(String username) {
		/* Same as the previous one, but this time, we care about
		 * the USER. Goes over THEIR assignments in the course, and 
		 * returns if THEIR grade doesn't exist, not just if the entire
		 * course is ungraded. */
		HashSet<String> ungradedAssignments = new HashSet<String>();
		for (String key : assignmentMap.keySet()) {
			if (!(assignmentMap.get(key).getStudentGradeExists(username))) {
				ungradedAssignments.add(key);
			}
		}
		return ungradedAssignments;
	}

	@JsonIgnore
	public double getCourseAverage(String stu) {
		return studentMap.get(stu).getCourseAverage(stu);
	}
	
	@JsonIgnore
	protected HashMap<String, Assignment> getAssignmentsMap(){
		return new HashMap<String, Assignment>(this.assignmentMap);
	}
	
	protected ArrayList<Student> getStudentMap(){
		/* This tries to avoid giving a hashmap, since where it's used
		 * would be inconvenient if it was. */
		ArrayList<Student> copyStudentMap =  new ArrayList<Student>();
		for(Student student : studentMap.values()) {
			copyStudentMap.add(student);
		}
		
		return copyStudentMap;
	}
	
	protected ArrayList<Student> getStudentMapCopier(){
		/* This tries to avoid giving a hashmap, since where it's used
		 * would be inconvenient if it was. */
		ArrayList<Student> copyStudentMap =  new ArrayList<Student>();
		for(Student student : studentMap.values()) {
			copyStudentMap.add(new Student(student));
		}
		
		return copyStudentMap;
	}
	
	public ArrayList<Double> getGradesForAssignment(String assignmentName) {
		/* Returns assignment grades. Also handles the case of the assignment
		 * not existing, in which it obviously could not be graded. */
        Assignment assignment = assignmentMap.get(assignmentName);
        if (assignment == null) {
            return new ArrayList<>();
        }
        return assignment.getAllGrades();
    }
	
	public Double getStudentGrade(String username, String assignmentName) {
		/* Returns assignment grade for a student. Also handles the case of the assignment
		 * not existing, in which it obviously could not be graded. */
        Assignment assignment = assignmentMap.get(assignmentName);
        if (assignment == null) {
            return null;
        }
        return assignment.getStudentGrade(username);
    }
	
	protected boolean addStudent(Student student) {
		/* Adds a student to the course! Also updates the student
		 * itself to have the course, and then returns if they were already in it. */
		String stuUser = student.getUsername();
		if (studentMap.containsKey(stuUser)) return false;
		studentMap.put(stuUser, student);
		return true;
	}
	
	protected boolean removeStudent(Student student) {
		/* Removes a student to from course! Also updates the student
		 * itself to remove the course, and then returns if they were even in it. */
		String stuUser = student.getUsername();
		if (!(studentMap.containsKey(stuUser))) return false;
		studentMap.remove(stuUser);
		return true;
	}
	
	protected Iterator<Student> getEnrolledStudents() {
		// A nice iterator for enrolled students, for more sane usage.
		return studentMap.values().iterator();
	}
	
	protected Teacher getTeacher() {
		return teacher;
	}
	
	public boolean isEnrolled(Student student) {
		/* Returns if a student is in the course. */
		return studentMap.containsKey(student.getUsername());
	}
	
	public void addAssignment(String assignmentName, String assignmentCategory) {
		/* Adds a new assignment, made by itself, only with the name & a category as a string.
		 * Figures out the category based on the string, then makes the new assignment itself. */
		Assignment newAssignment = new Assignment(assignmentName,this.convertToEnums(assignmentCategory));
		newAssignment.setType(this.convertToEnums(assignmentCategory));
		assignmentMap.put(assignmentName, newAssignment);
	}
	
	public boolean removeAssignment(String assignmentName) {
		/* Removes an assignment based on it's name. Remember: Assignment names in each course are unique. */
		return (assignmentMap.remove(assignmentName) != null);
	}
	
	protected int getModeChosen() {
		return modeChosen;
	}
	
	protected void setModeChosen(int modeChosen) {
		this.modeChosen = modeChosen;
	}
	
	protected void setDrop(ArrayList<Integer> drops) {
		this.drops = drops;
	}
	
	protected void setWeight(ArrayList<Double> weights) {
		this.weights = weights;
	}
	
	protected ArrayList<Double> getWeights() {
		return weights;
	}
	
	protected ArrayList<Integer> getDrops() {
		return drops;
	}
	
	private AssignmentType convertToEnums(String category) {
		/* Figures out the enum based on a string. Practically just
		 * matches it to the enum text, and returns what lines up. */
		// We use toLowerCase() for convenience
        switch (category.toLowerCase()) {
	        case "midterm":
	            return AssignmentType.MIDTERM;
	        case "final":
	            return AssignmentType.FINAL;
	        case "quiz":
	            return AssignmentType.QUIZ;
	        case "hw":
	            return AssignmentType.HW;
	        case "project":
	            return AssignmentType.PROJECT;
	        default:
	            throw new IllegalArgumentException("Unknown assignment type: " + category);
        }
	}
	
	// JSON RELATED METHODS
	// As in, if you're using it for anything other than that, what are you doing?
	
	protected void packUpReferences() {
		/* Packs up the course to exporting to a json file. We have to do this
		 * because certain elements in here (ie: anything related to other objects) risk
		 * making an infinity mirror or copies if we don't. I suppose this could be considered
		 * "upcasting"? Still, it's reconstructable in unPackReferences, due to hashmaps enforcing
		 * no duplicates, which is very, very convenient. */
		studentPacked = new ArrayList<String>();
		teacherPacked = teacher.getUsername();
		for (Student i : studentMap.values()) studentPacked.add(i.getUsername());
	}
	
	// This should be called once on startup. That's it.
	protected void unPackReferences(HashMap<String, Student> studentMaps, HashMap<String, Teacher> teacherMaps) {
		/* Takes the vars made in packUpReferences and turns them back into the more
		 * usable versions. Because we have convenient spots of no duplicates, we can accurately recreate
		 * the students & teachers with just their usernames. */
		for (String i : studentPacked) {
			this.studentMap.put(i, studentMaps.get(i));
		}
		teacher = teacherMaps.get(teacherPacked);
	}
	
	// JSON METHODS
	// As in, we don't use these, but the json needs them to exist...
	protected Course() {
		studentMap = new HashMap<String, Student>();
		assignmentMap = new HashMap<String, Assignment>();
	}
	
	// We can't fix them directly, so let's set up a fix.
	@JsonSetter
	protected void setStudentPacked(ArrayList<String> studentPacked) {
		this.studentPacked = studentPacked;
	}
	
	@JsonSetter
	protected void setTeacherPacked(String teacherPacked) {
		this.teacherPacked = teacherPacked;
	}
	
	@JsonSetter
	protected void setCategories(HashMap<AssignmentType, Double> categories) {
		this.categories = categories;
	}
	
	@JsonSetter
	protected void setWeights(ArrayList<Double> weights) {
		this.weights = weights;
	}
	
	@JsonSetter
	protected void setDrops(ArrayList<Integer> drops) {
		this.drops = drops;
	}
	
	@JsonGetter
	protected HashMap<String, Assignment> getAssignmentMap() {
		return assignmentMap;
	}
	
}