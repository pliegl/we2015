package model;

import org.junit.After;
import org.junit.Before;
import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.JPAPlugin;
import play.libs.F;
import play.test.FakeApplication;
import scala.Option;
import service.PersistenceService;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.start;

/**
 * Created by pl on 10.05.14.
 */
public abstract class BaseTest {

    final protected PersistenceService persistence = PersistenceService.INSTANCE;

    protected EntityManager em;

    /**
     * Setup the test
     */
    @Before
    public void setupTest() {
        FakeApplication app = fakeApplication();
        start(app);
        em = app.getWrappedApplication().plugin(JPAPlugin.class).get().em("default");
        JPA.bindForCurrentThread(em);


        emptyDatabase();


    }

    @After
    public void tearDownIntegrationTest() {
        JPA.bindForCurrentThread(null);
        em.close();
    }


    /**
     * Since we use a file-based database and not an in-memory database for this showcase we must
     * remove any old entries.
     */
    @Before
    public void emptyDatabase() {

        try  {
            JPA.withTransaction(new F.Callback0() {
                @Override
                public void invoke() throws Throwable {

                    em.getTransaction().begin();

                    Logger.info("Emptying database");
                    int count = em.createQuery("DELETE FROM Scholarship").executeUpdate();
                    Logger.info("Deleted {} scholarships", count);

                    count = em.createQuery("DELETE FROM ExamResult").executeUpdate();
                    Logger.info("Deleted {} examresults", count);

                    count = em.createQuery("DELETE FROM Course").executeUpdate();
                    Logger.info("Deleted {} examresults", count);

                    count = em.createQuery("DELETE FROM Student").executeUpdate();
                    Logger.info("Deleted {} students", count);

                    em.getTransaction().commit();

                }
            });

        }
        catch (Exception e) {
            Logger.error("Test failed", e);
            fail();
        }

    }




}
