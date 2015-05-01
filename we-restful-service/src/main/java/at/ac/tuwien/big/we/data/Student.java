package at.ac.tuwien.big.we.data;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Student domain object
 */
@XmlRootElement
public class Student {


    //The register number aka Matrikelnummer
    private String registerNumber;
    //Name of the student
    private String name;


    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
