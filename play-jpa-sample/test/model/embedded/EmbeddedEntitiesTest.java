package model.embedded;

import model.*;
import org.joda.time.DateTime;
import org.junit.Test;
import play.db.jpa.JPA;
import play.libs.F;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by pl on 10.05.14.
 */
public class EmbeddedEntitiesTest extends BaseTest {




    @Test
    public void testStoreAndRetrieveEmbeddedEntities() {

        JPA.withTransaction(new F.Callback0() {
            @Override
            public void invoke() throws Throwable {

                EmbeddedStudent student = new EmbeddedStudent();
                student.setName("Max Mustermann");
                student.setRegistrationNumber("0123565");
                student.setLoginTime(new DateTime(2014, 12, 12, 14, 00, 00));

                //Add the scholarship
                EmbeddedScholarship embeddedScholarship = new EmbeddedScholarship();
                embeddedScholarship.setAmount(1000);
                embeddedScholarship.setDescription("Scholarship A");
                student.setScholarship(embeddedScholarship);


                //Save the student
                persistence.persist(student);

                //Get a list of all students
                List<EmbeddedStudent> students = persistence.findEntity(EmbeddedStudent.class);
                //Expecting values to be correctly persisted
                assertTrue(students.size() == 1);
                student = students.get(0);
                assertEquals("0123565", student.getRegistrationNumber());

                //Get the scholarship
                assertEquals("Scholarship A", student.getScholarship().getDescription());
            }
        });


    }




}
