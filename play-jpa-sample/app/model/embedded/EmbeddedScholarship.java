package model.embedded;

import javax.persistence.Embeddable;

/**
 * Scholarship domain class using embedded concepts
 */
@Embeddable
public class EmbeddedScholarship {

    private String description;

    private Integer amount;

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

}
