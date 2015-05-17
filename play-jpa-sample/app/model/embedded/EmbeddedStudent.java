package model.embedded;

import model.BaseEntity;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * Student domain class using embedded concepts for the scholarship
 */
@Entity
public class EmbeddedStudent extends BaseEntity {

    @Column(name = "matrikelNummer", unique = true)
    private String registrationNumber;

    private String name;

    @Embedded
    private EmbeddedScholarship scholarship;

    @Transient
    private DateTime loginTime;

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(DateTime loginTime) {
        this.loginTime = loginTime;
    }

    public EmbeddedScholarship getScholarship() {
        return scholarship;
    }

    public void setScholarship(EmbeddedScholarship scholarship) {
        this.scholarship = scholarship;
    }
}
