package model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Base JPA Entity
 */
@MappedSuperclass
public class BaseEntity {


  //Id shared by all entities of our domain model
  @Id
  @GeneratedValue
  protected Long id;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
