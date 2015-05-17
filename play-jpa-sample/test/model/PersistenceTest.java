package model;

import org.joda.time.DateTime;
import org.junit.Test;
import play.Logger;
import play.db.jpa.JPA;
import play.libs.F;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for the persitence service
 */
public class PersistenceTest extends BaseTest {



    @Test
    public void testPersistStudents() {

        JPA.withTransaction(new F.Callback0() {
            @Override
            public void invoke() throws Throwable {

                for (int i = 0; i < 3; i++) {
                    Student s = new Student();
                    s.setRegistrationNumber(String.valueOf(i));
                    s.setName("sample student " + i);
                    persistence.merge(s);
                }

                List<Student> students = persistence.findEntity(Student.class);
                assertTrue(students.size() == 3);

            }
        });

    }


    @Test
    public void testStoreAndRetrieveStudents() {

        JPA.withTransaction(new F.Callback0() {
            @Override
            public void invoke() throws Throwable {

                Student student = new Student();
                student.setName("Max Mustermann");
                student.setRegistrationNumber("0123565");
                student.setLoginTime(new DateTime(2014, 12, 12, 14, 00, 00));

                //Save the student
                persistence.persist(student);

                //Get a list of all students
                List<Student> students = persistence.findEntity(Student.class);
                //Expecting values to be correctly persisted
                assertTrue(students.size() == 1);
                student = students.get(0);
                assertEquals("0123565", student.getRegistrationNumber());

                //Retrieve a list of all students using a NamedQuery
                assertTrue(persistence.getAllStudents().size() == 1);

                //There must be exactly one student name Max Mustermann
                List<Student> studentList = persistence.getStudentByName("Max Mustermann");
                assertEquals(1, studentList.size());

                //There must be no student named foo
                studentList = persistence.getStudentByName("foo");
                assertEquals(0, studentList.size());
            }
        });


        try  {
            JPA.withTransaction(new F.Callback0() {
                @Override
                public void invoke() throws Throwable {

                    Student student = persistence.getStudent("0123565");

                    //Assign a scholarship to the student
                    Scholarship scholarship = new Scholarship();
                    scholarship.setAmount(1000);
                    scholarship.setDescription("Scholarship A");

                    student.addScholarship(scholarship);

                    student = persistence.merge(student);

                    Long scholarShipId = student.getScholarship().getId();
                    assertNotNull(scholarShipId);

                    //The student must have the scholarship assigned
                    assertEquals("Scholarship A", student.getScholarship().getDescription());

                    //Get the scholarship
                    List<Scholarship> scholarships = persistence.findEntity(Scholarship.class);
                    assertEquals(scholarShipId, scholarships.get(0).getId());

                    //Try to access the student
                    Student student1 = scholarships.get(0).getGrantedTo();
                    assertEquals(student.getId(), student1.getId());

                    student.setScholarship(null);
                    student = persistence.merge(student);
                    assertNull(student.getScholarship());

                }
            });

        }
        catch (Exception e) {
            Logger.error("Test failed", e);
           fail();
        }



        try  {
            JPA.withTransaction(new F.Callback0() {
                @Override
                public void invoke() throws Throwable {

                    Student student = persistence.getStudent("0123565");

                    //Assign a new scholarship
                    Scholarship scholarship1 = new Scholarship();
                    scholarship1.setAmount(500);
                    scholarship1.setDescription("abc");

                    student.setScholarship(scholarship1);

                    student = persistence.merge(student);

                    assertNotNull(student.getScholarship());

                }
            });

        }
        catch (Exception e) {
            Logger.error("Test failed", e);
            fail();
        }




        try  {
            JPA.withTransaction(new F.Callback0() {
                @Override
                public void invoke() throws Throwable {

                    em.getTransaction().begin();

                    //Get the existing student and his scholarship
                    Student student = persistence.getStudent("0123565");

                    //Create a new student and try to assign him the same scholarship
                    Student student2 = new Student();
                    student2.setName("Max Mustermann");
                    student2.setRegistrationNumber("29383929");
                    student2.setLoginTime(new DateTime(2014, 12, 12, 14, 00, 00));

                    //Get the scholarship of the existing student and try to assign it to the new student
                    Scholarship scholarship = student.getScholarship();
                    student2.addScholarship(scholarship);

                    //Save the student
                    student2 = persistence.merge(student2);

                    em.getTransaction().commit();

                    //scholarship must now be assigned to student 2
                    scholarship = persistence.findEntity(Scholarship.class).get(0);
                    assertEquals("29383929", scholarship.getGrantedTo().getRegistrationNumber());


                }
            });

        }
        catch (Exception e) {
            Logger.error("Test failed", e);
            fail();
        }

    }



    @Test
    public void testPersistSameObjectTwice() {

        try  {
            JPA.withTransaction(new F.Callback0() {
                @Override
                public void invoke() throws Throwable {

                    //Persist an empty student
                    Student s = new Student();
                    s.setName("foo");
                    s.setRegistrationNumber("bar");
                    persistence.persist(s);

                    //Get the managed student
                    s = persistence.getStudent("bar");

                    //Add an exam result
                    ExamResult examResult = new ExamResult();
                    examResult.setMark(2);
                    examResult.setExam("Web Engineering");
                    s.addExamResult(examResult);

                    //Try to persist the student again (although it is already persisted)
                    //Nothing happens with the student itself, but the changes to
                    //the examresult must be propagated
                    persistence.persist(s);

                    //Exam result is expected to be managed as well (i.e., it has a PK)
                    assertNotNull(s.getExamResults().get(0).getId());
                    Logger.info("Examresult stored with id {}", s.getExamResults().get(0).getId());

                    //Detach the student
                    persistence.detach(s);

                    //Try to persist it again (causes an exception)
                    persistence.persist(s);

                }
            });
            fail();
        }
        catch (Exception e) {
            Logger.info(e.getMessage(), e);
        }
    }


    @Test
    public void testStoreAndRetrieveExamResults() {


        try  {
            JPA.withTransaction(new F.Callback0() {
                @Override
                public void invoke() throws Throwable {

                    //Create a new Student with two exams
                    Student s = new Student();
                    s.setName("Student A");
                    s.setRegistrationNumber("1699394");

                    //Assign two exam results
                    ExamResult examResult = new ExamResult();
                    examResult.setExam("Web Engineering");
                    examResult.setMark(1);
                    s.addExamResult(examResult);

                    ExamResult examResult1 = new ExamResult();
                    examResult1.setExam("Model Engineering");
                    examResult1.setMark(2);
                    s.addExamResult(examResult1);

                    ExamResult examResult2 = new ExamResult();
                    examResult2.setExam("Model Engineering");
                    examResult2.setMark(5);
                    s.addExamResult(examResult2);


                    //Store the student
                    persistence.persist(s);

                    //Get the student and check if the exam results are correctly persisted
                    s = persistence.getStudent("1699394");
                    assertEquals(3, s.getExamResults().size());

                    //There shall be two correct examresult
                    assertEquals(2, persistence.getPositiveExamResults(s).size());

                    //There shall be 1 negative examresult
                    assertEquals(1, persistence.getNegativeExamResults(s).size());


                }
            });

        }
        catch (Exception e) {
            Logger.error("Test failed", e);
            fail();
        }


    }



    @Test
    public void testStoreAndRetrieveCourses() {


        try  {
            JPA.withTransaction(new F.Callback0() {
                @Override
                public void invoke() throws Throwable {


                    //Create a new student
                    Student s = new Student();
                    s.setName("Student A");
                    s.setRegistrationNumber("1699394");


                    //Create 5 courses
                    for (int i = 0; i < 5; i++) {
                        Course course = new Course();
                        course.setCourseNumber(String.valueOf(i));
                        course.setTitle("Sample course " + i);

                        //Add the course to the student
                        s.addCourse(course);
                    }

                    //Persist the student and the courses
                    s = persistence.merge(s);


                    assertEquals(5, s.getCourses().size());



                }
            });

        }
        catch (Exception e) {
            Logger.error("Test failed", e);
            fail();
        }


    }



    @Test
    public void testRemoveStudentAndCourses() {


        try  {
            JPA.withTransaction(new F.Callback0() {
                @Override
                public void invoke() throws Throwable {


                    //Create a new student
                    Student s = new Student();
                    s.setName("Student A");
                    s.setRegistrationNumber("1699394");


                    //Create 5 courses
                    for (int i = 0; i < 5; i++) {
                        Course course = new Course();
                        course.setCourseNumber(String.valueOf(i));
                        course.setTitle("Sample course " + i);

                        //Add the course to the student
                        s.addCourse(course);
                    }

                    //Persist the student and the courses
                    s = persistence.merge(s);


                    assertEquals(5, s.getCourses().size());

                    //Get a fresh copy of the student
                    s = persistence.getStudent("1699394");

                    //Remove the student
                    persistence.remove(s);

                    //All courses should be removed as well
                    List<Course> courses = persistence.findEntity(Course.class);
                    assertEquals(0, courses.size());



                }
            });

        }
        catch (Exception e) {
            Logger.error("Test failed", e);
            fail();
        }


    }






}
