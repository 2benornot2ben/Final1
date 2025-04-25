package backend;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

public class CourseTest {
    @Test
    public void test_01() {
        Course c = new Course("CSC252");
        c.setTotalGrade(0);
        assertTrue(c.getTotalGrade() == 0);
        assertTrue(c.getModeChosen() == 0);
        c.setModeChosen(5);
        assertTrue(c.getModeChosen() == 5);
        assertEquals(c.getCourseName(), "CSC252");
    }
    
    @Test
    public void test_02() {
    	Course c = new Course("CSC252");
    	Student stu = new Student("A", "B", "AB");
    	c.addStudent(stu);
    	assertEquals(c.getStudentMap().size(), 1);
    	assertEquals(c.getStudentMap().get(0), stu);
    	assertTrue(c.isEnrolled(stu));
    	c.addStudent(stu);
    	c.removeStudent(stu);
    	assertFalse(c.isEnrolled(stu));
    	c.removeStudent(stu);
    }
    
    @Test
    public void test_03() {
    	Course c = new Course("CSC252");
    	Teacher tea = new Teacher("A");
    	c.setTeacher(tea);
    	assertTrue(c.getTeacher().equals(tea));
    }
    
    @Test
    public void test_04() {
    	Course c = new Course("CSC252");
    	HashMap<String, Student> stuMap = new HashMap<String, Student>();
    	HashMap<String, Assignment> assignMap = new HashMap<String, Assignment>();
    	stuMap.put("A", new Student("A", "B", "AB"));
    	stuMap.put("C", new Student("C", "D", "CD"));
    	assignMap.put("Final1", new Assignment("Final1", AssignmentType.FINAL));
    	assignMap.put("Final2", new Assignment("Final2", AssignmentType.FINAL));
    	c.setAssignmentMap(assignMap);
    	c.setStudentMap(stuMap);
    	assertEquals(c.getAssignmentsMap().size(), 2);
    	assertEquals(c.getStudentMap().size(), 2);
    }
    
    @Test
    public void test_05() {
    	Course c = new Course("CSC252");
    	Course d = new Course("CSC252");
    	Course e = new Course("CSC345");
    	Student stu = new Student("A", "B", "AB");
    	assertFalse(c.equals(null));
    	assertFalse(c.equals(stu));
    	assertTrue(c.equals(d));
    	assertFalse(c.equals(e));
    	assertTrue(c.equals("CSC252"));
    	assertTrue(c.equals(c));
    }

    @Test
    public void test_06() {
    	Course c = new Course("CSC252");
    	Course d = new Course("CSC252");
    	assertEquals(c.hashCode(), d.hashCode());
    }
    
    @Test
    public void test_07() {
    	Course c = new Course("CSC252");
    	assertFalse(c.isCompleted());
    	c.completeCourse();
    	assertTrue(c.isCompleted());
    }
    
    @Test
    public void test_08() {
    	User t = new User();
    	AccountStorage a = new AccountStorage();
    	Database d;
    	Model m;
		try {
			d = new Database(a);
			m = new Model(d, t);
			
			ArrayList<Double> weights = new ArrayList<Double>();
			ArrayList<Integer> drops = new ArrayList<Integer>();
			for (int i = 0; i < 5; i++) {
				weights.add(0.0);
			}
			for (int i = 0; i < 3; i++) {
				drops.add(0);
			}
			assertEquals(m.getUngradedAssignments("CSC252").size(), 14);
			m.calculateClassAverage(10, "CSC252", weights, drops);
			m.addGradeForAssignment("dlee", "Final", 10, "CSC252");
			m.addGradeForAssignment("kpatel", "Final", 10, "CSC252");
			m.addGradeForAssignment("sgupta", "Final", 10, "CSC252");
			m.addGradeForAssignment("jchen", "Final", 10, "CSC252");
			m.setAssignmentGraded("CSC252", "Final");
			assertEquals(m.getGradedAssignments("CSC252").size(), 1);
			assertEquals(m.getUngradedAssignments("CSC252").size(), 13);
			assertEquals(m.getGradedAssignmentsUSER("CSC252", "dlee").size(), 1);
			assertEquals(m.getUngradedAssignmentsUSER("CSC252", "dlee").size(), 13);
			//m.assignFinalGrade("CSC252");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Test
    public void test_09() {
    	Course c = new Course("CSC252");
    	c.addAssignment("Final1", "Final");
    	c.addAssignment("Final2", "Final");
    	assertEquals(c.getAssignmentsMap().size(), 2);
    	c.removeAssignment("Final2");
    	assertEquals(c.getAssignmentsMap().size(), 1);
    }
    
    @Test
    public void test_10() {
    	Course c = new Course("CSC252");
    	c.addAssignment("Final1", "Final");
    	c.addAssignment("Midterm1", "Midterm");
    	c.addAssignment("Quiz1", "Quiz");
    	c.addAssignment("Hw1", "Hw");
    	c.addAssignment("Project1", "Project");
    	try {
    		c.addAssignment("Bleh", "Bleh");
    		fail();
    	} catch (IllegalArgumentException e) {
    		assertTrue(true);
    	}
    }
    
    @Test
    public void test_11() {
    	Course c = new Course("CSC252");
    	ArrayList<Double> weights = new ArrayList<Double>();
		ArrayList<Integer> drops = new ArrayList<Integer>();
		for (int i = 0; i < 5; i++) {
			weights.add(0.0);
		}
		for (int i = 0; i < 3; i++) {
			drops.add(0);
		}
		c.setWeight(weights);
		c.setDrop(drops);
		assertEquals(c.getWeights(), weights);
		assertEquals(c.getDrops(), drops);
    }
    
    @Test
    public void test_12() {
    	Course c = new Course("CSC252");
    	HashMap<String, Student> stuList = new HashMap<String, Student>();
    	HashMap<String, Teacher> teaList = new HashMap<String, Teacher>();
    	stuList.put("AB", new Student("A", "B", "AB"));
    	teaList.put("A", new Teacher("A"));
    	c.setTeacher(new Teacher("A"));
    	c.setStudentMap(stuList);
    	
    	c.packUpReferences();
    	c.unPackReferences(stuList, teaList);
    }
    
    @Test
    public void test_13() {
    	// Mostly dedicated to json
    	Course c = new Course("CSC252");
    	ArrayList<String> stuPack = new ArrayList<String>();
    	c.setStudentPacked(stuPack);
    	c.setTeacherPacked("teachername!");
    	ArrayList<Double> weights = new ArrayList<Double>();
		ArrayList<Integer> drops = new ArrayList<Integer>();
    	for (int i = 0; i < 5; i++) {
			weights.add(0.0);
		}
		for (int i = 0; i < 3; i++) {
			drops.add(0);
		}
    	c.setWeights(weights);
    	c.setDrops(drops);
    	HashMap<AssignmentType, Double> catagories = new HashMap<AssignmentType, Double>();
    	catagories.put(AssignmentType.FINAL, 25.0);
    	c.setCategories(catagories);
    	c.getAssignmentMap();
    }
    
    @Test
    public void test_14() {
    	Course c = new Course("CSC252");
    	Student stu = new Student("A", "B", "AB");
    	c.addStudent(stu);
    	assertTrue(c.getEnrolledStudents() instanceof Iterator);
    }
    
    @Test
    public void test_15() {
    	Course c = new Course("CSC252");
    	c.addAssignment("Final1", "Final");
    	c.addAssignment("Final2", "Final");
    	assertEquals(c.getAssignmentByType(AssignmentType.FINAL).size(), 2);
    	assertEquals(c.getAssignmentByType(AssignmentType.PROJECT).size(), 0);
    }
    
    @Test
    public void test_16() {
    	Course c = new Course("CSC252");
    	assertTrue(c.getGradesForAssignment("Final").isEmpty());
    }
    
    @Test
    public void test_17() {
    	Course c = new Course(new Course("CSC252"));
    	Course c2 = new Course();
    	c.getStudentGrade("lwilson", "Hw1");
    }
}