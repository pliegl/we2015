package at.ac.tuwien.big.we.exceptions;

import com.sun.jersey.api.Responses;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Created by pl on 02.05.14.
 * Custom exception to be returned in case an item is accessed, which does not exist
 */
public class ItemNotFoundException extends WebApplicationException {


    public ItemNotFoundException() {
        super(Responses.notFound().build());
    }

    public ItemNotFoundException(String message) {
        super(Response.status(Responses.NOT_FOUND).entity(message).type("text/plain").build());


    }

}
