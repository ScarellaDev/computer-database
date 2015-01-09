package com.excilys.computerdatabase.bean;

public final class Company {
  private Long   id;
  private String name;

  public Company() {}

  public Company(Long id, String name) {
    super();
    this.id = id;
    this.name = name;
  }

  @Override
  public String toString() {
    return "Company [id=" + id + ", name=" + name + "]";
  }

  @Override
  public boolean equals(Object obj) {
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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public final String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
