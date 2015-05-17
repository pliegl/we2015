package service;

import model.BaseEntity;
import model.ExamResult;
import model.Student;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Persistence service
 */
public enum PersistenceService {

    INSTANCE;


    /**
     * Merge the given entity. If there is no entity with the given ID yet, create a new one. Otherwise update the
     * existing one
     *
     * @param entity
     * @param <T>
     * @return
     */
    public <T extends BaseEntity> T merge(T entity) {
        return em().merge(entity);
    }


    /**
     * Persist the given entity. Throws an error in case the entity already exists
     *
     * @param entity
     */
    public void persist(BaseEntity entity) {
        em().persist(entity);
    }

    /**
     * Remove the entity
     * @param entity
     */
    public void remove(BaseEntity entity) {
        em().remove(entity);
    }

    /**
     * Detach the given entity from the persistence context
     * @param entity
     */
    public void detach(BaseEntity entity) {
        em().detach(entity);
    }


    /**
     * Search for the entity of the given type with the given id
     *
     * @param id          The ID of the entity
     * @param entityClazz The class of the entity
     * @param <T>
     * @return
     */
    public <T extends BaseEntity> T findEntity(Long id, Class<T> entityClazz) {
        return em().find(entityClazz, id);
    }


    /**
     * Get a list of all entities of a certain type
     *
     * @param entityClazz
     * @param <E>
     * @return
     */
    public <E extends BaseEntity> List<E> findEntity(Class<E> entityClazz) {
        Criteria c = ((Session) JPA.em().getDelegate()).createCriteria(entityClazz);
        return c.list();
    }

    /**
     * Get a list of positive exams for a certain student
     * @param student
     * @return
     */
    public List<ExamResult> getPositiveExamResults(Student student) {
        Criteria c = ((Session) JPA.em().getDelegate()).createCriteria(Student.class);
        c.createCriteria("examResults").add(Restrictions.lt("mark", 5));
        return c.list();
    }


    /**
     * Get a list of negative exams for a certain student
     * @param student
     * @return
     */
    public List<ExamResult> getNegativeExamResults(Student student) {
        Criteria c = ((Session) JPA.em().getDelegate()).createCriteria(Student.class);
        c.createCriteria("examResults").add(Restrictions.eq("mark", 5));
        return c.list();

    }

    /**
     * Get a student based on his unique registration number
     * Uses the JPA Criteria Query
     *
     * @param registrationNumber
     * @return
     */
    public Student getStudent(String registrationNumber) {

        CriteriaBuilder cb = em().getCriteriaBuilder();
        CriteriaQuery<Student> criteriaQuery = cb.createQuery(Student.class);
        Root<Student> s = criteriaQuery.from(Student.class);
        ParameterExpression<String> parameter = cb.parameter(String.class);
        criteriaQuery.select(s).where(cb.equal(s.get("registrationNumber"), parameter));

        TypedQuery<Student> typedQuery = em().createQuery(criteriaQuery);
        typedQuery.setParameter(parameter, registrationNumber);
        return typedQuery.getSingleResult();

    }

    /**
     * Get a student by his name. Shows how to use a JPQL query
     * @param studentName
     * @return
     */
    public List<Student> getStudentByName(String studentName) {
        TypedQuery<Student> studentTypedQuery = em().createQuery("SELECT s FROM Student s WHERE s.name LIKE :studentName", Student.class);

        studentTypedQuery.setParameter("studentName", studentName);

        return studentTypedQuery.getResultList();
    }


    /**
     * Return a list of all students using a NamedQuery
     * @return
     */
    public List<Student> getAllStudents() {
        List<Student> students = em().createNamedQuery("findAllStudents", Student.class).getResultList();
        return students;
    }


    /**
     * Return the entity manager
     *
     * @return
     */
    public EntityManager em() {
        return JPA.em();
    }

}
