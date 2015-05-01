package at.ac.tuwien.big.we.resources;

import at.ac.tuwien.big.we.data.Student;
import at.ac.tuwien.big.we.data.StudentDao;
import at.ac.tuwien.big.we.exceptions.ItemNotFoundException;
import com.sun.jersey.api.Responses;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;
import java.util.List;

/**
 * Student resource
 */

@Path("/students")
public class StudentResource {


    @Context
    UriInfo uriInfo;

    /**
     * Returns a list of students currently stored on the server as XML
     * @return
     */
    @GET
    @Produces({MediaType.APPLICATION_XML})
    public List<Student> getStudentsXML() {
        return StudentDao.INSTANCE.getStudents();
    }


    /**
     * Returns a list of students currently stored on the server as JSON
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Student> getStudentsJSON() {
        return StudentDao.INSTANCE.getStudents();
    }


    /**
     * Return a certain student using his/her id using the pattern
     * /students/${id}
     *
     * @return
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Student getStudent(@PathParam("id") String id) {

        //Does the requested student exist?
        if (StudentDao.INSTANCE.doesStudentExist(id)) {
            return StudentDao.INSTANCE.getStudentById(id);
        } else {
            //Example on how to use an exception in order to return a certain response
            throw new ItemNotFoundException("Unable to find student with id " + id);
        }

    }


    /**
     * Update an existing student
     * @param student
     * @return
     */
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_XML)
    public Response updateStudent(@PathParam("id") String id, JAXBElement<Student> student) {
        Student s = student.getValue();

        //Id of student cannot be overwritten
        if (!id.equals(s.getRegisterNumber())) {
            return Response.status(Responses.CONFLICT).entity("Unable to update student. New student has a different ID than the student you want to update.").type("text/plain").build();
        }

        //Does the student exist?
        if (StudentDao.INSTANCE.doesStudentExist(id)) {
            StudentDao.INSTANCE.updateStudent(s);
            return Response.ok("Student has been updated.").build();
        } else {
            //Student does not exist - throw an error
            return Response.status(Responses.NOT_FOUND).entity("Unable to update - student does not exist").type("text/plain").build();
        }
    }

    /**
     * Update a set of students
     *
     * @param students
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public Response updateStudents(List<Student> students) {

        //Do all the students exist?
        for (Student s : students) {
            if (!StudentDao.INSTANCE.doesStudentExist(s.getRegisterNumber())) {
                return Response.status(Responses.NOT_FOUND).entity(String.format("Unable to update - student %s does not exist", s.getRegisterNumber())).type("text/plain").build();
            }
        }

        //All students fine - update
        StringBuilder names = new StringBuilder();
        for (Student s : students) {
            StudentDao.INSTANCE.updateStudent(s);
        }


        return Response.ok("Students have been updated ").build();
    }


    /**
     * Create a new student. In case the given student already exists, return an error
     *
     * @param student
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public Response createNewStudent(JAXBElement<Student> student) {
        Student s = student.getValue();

        //Student already exists - return an error
        if (StudentDao.INSTANCE.doesStudentExist(s.getRegisterNumber())) {
            return Response.status(Responses.CONFLICT).entity("Unable to create - student already exists").type("text/plain").build();
        } else {
            StudentDao.INSTANCE.updateStudent(s);
            return Response.created(uriInfo.getAbsolutePath()).entity(
                "Student " + s.getName() + " has been added.").build();
        }

    }


    /**
     * Delete a certain student
     *
     * @param id
     * @return
     */
    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_XML)
    public Response deleteStudent(@PathParam("id") String id) {

        //Does the student exist?
        if (!StudentDao.INSTANCE.doesStudentExist(id)) {
            return Response.status(Responses.NOT_FOUND).entity("Unable to delete - student does not exist").type("text/plain").build();
        } else {
            StudentDao.INSTANCE.deleteStudent(id);
            return Response.ok(String.format("Student with id %s has been deleted", id)).build();
        }

    }


    /**
     * Delete all students
     *
     * @return
     */
    @DELETE
    @Consumes(MediaType.APPLICATION_XML)
    public Response deleteStudent() {
        //Delete all students
        StudentDao.INSTANCE.deleteStudents();
        return Response.ok("All students have been deleted.").build();
    }


}
