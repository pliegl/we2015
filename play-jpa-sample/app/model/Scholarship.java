package model;

import javax.persistence.*;

/**
 *  Scholarship domain class
 */
@Entity
public class Scholarship extends BaseEntity {

    private String description;

    private Integer amount;

    @JoinColumn(name = "student_id", unique = true)
    @OneToOne
    private Student grantedTo;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Student getGrantedTo() {
        return grantedTo;
    }

    public void setGrantedTo(Student grantedTo) {
        this.grantedTo = grantedTo;
    }
}
