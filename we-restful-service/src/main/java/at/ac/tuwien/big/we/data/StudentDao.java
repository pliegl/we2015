package at.ac.tuwien.big.we.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Holds the students, currently stored on the server
 */
public enum StudentDao {

    INSTANCE;


    private static final Logger LOG = LoggerFactory.getLogger(StudentDao.class.getName());


    private static boolean INITIALIZED = false;

    //Students are stored using their register number as unique key
    private HashMap<String, Student> students = new HashMap<String, Student>();


    /**
     * Delete a given student
     *
     * @param id
     */
    public void deleteStudent(String id) {
        if (students.containsKey(id)) {
            students.remove(id);
            LOG.info("Student with id {} has been deleted.", id);
        }
    }

    /**
     * Delete all students
     */
    public void deleteStudents() {
        students.clear();
    }


    /**
     * Update an existing student. If the student does not exist yet, add it anyway
     *
     * @param student
     */
    public void updateStudent(Student student) {

        if (students.containsKey(student.getRegisterNumber())) {
            LOG.info("Student with id {} has been updated.", student.getRegisterNumber());
        }
        else {
            LOG.info("Student with id {} has been added.", student.getRegisterNumber());
        }

        students.put(student.getRegisterNumber(), student);
    }


    /**
     * Return a list of existing students
     *
     * @return
     */
    public List<Student> getStudents() {

        //If the list is empty, prepopulate it with some values
        if (!INITIALIZED) {

            Student student1 = new Student();
            student1.setRegisterNumber("1");
            student1.setName("Jack Byrnes");
            students.put(student1.getRegisterNumber(), student1);

            Student student2 = new Student();
            student2.setRegisterNumber("2");
            student2.setName("Dina Byrnes");
            students.put(student2.getRegisterNumber(), student2);

            Student student3 = new Student();
            student3.setRegisterNumber("3");
            student3.setName("Mr. Jinx");
            students.put(student3.getRegisterNumber(), student3);

            INITIALIZED = true;

        }


        return new LinkedList<Student>(students.values());
    }

    /**
     * Return a specific student
     *
     * @param id
     * @return
     */
    public Student getStudentById(String id) {

        if (students.containsKey(id)) {
            return students.get(id);
        }

        return null;
    }

    /**
     * Return whether the given student exists or not
     * @param id
     * @return
     */
    public boolean doesStudentExist(String id) {
        if (students.containsKey(id)) {
            return true;
        }
        else {
            return false;
        }
    }


}
