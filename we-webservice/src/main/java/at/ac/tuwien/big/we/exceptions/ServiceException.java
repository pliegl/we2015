package at.ac.tuwien.big.we.exceptions;

import javax.xml.ws.WebFault;

/**
 * Created by pl
 * Exception thrown by sample service
 */
@WebFault(name="ServiceFault", targetNamespace = "http://www.big.tuwien.ac.at/we")
public class ServiceException extends Exception {


    public ServiceException(){
        super();
    }

    public ServiceException(String message) {
        super(message);
    }


}
