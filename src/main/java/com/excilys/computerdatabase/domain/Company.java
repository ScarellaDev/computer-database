package com.excilys.computerdatabase.domain;

/**
* Company class.
*
* @author Jeremy SCARELLA
*/
public final class Company {

  /*
   * Attributes
   */
  private Long   id;
  private String name;

  /*
   * Constructors
   * 
   */
  public Company() {

  }

  /**
  * @param id
  * @param name
  */
  public Company(final Long id, final String name) {
    super();
    this.id = id;
    this.name = name;
  }

  /*
   * toString/hashCode/equals overrides
   */
  @Override
  public String toString() {
    return "Company [id=" + id + ", name=" + name + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }

    final Company other = (Company) obj;
    if (id == other.getId()) {
      return true;
    }

    if (name == null && other.name != null) {
      return false;
    } else if (!name.equals(other.name)) {
      return false;
    }

    return true;
  }

  /*
   * Builder static class
   */
  public static final class Builder {
    Company company;

    private Builder() {
      company = new Company();
    }

    public Builder id(final Long id) {
      if (id != null) {
        this.company.id = id;
      }
      return this;
    }

    public Builder name(final String name) {
      this.company.name = name;
      return this;
    }

    public Company build() {
      return this.company;
    }
  }

  public static Builder builder() {
    return new Builder();
  }

  /*
   * Getter/Setter
   */
  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }
}
