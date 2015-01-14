package com.excilys.computerdatabase.domain;

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
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
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

  public static class Builder {
    Company company;

    private Builder() {
      company = new Company();
    }

    public Builder id(Long id) {
      if (id != null)
        this.company.id = id;
      return this;
    }

    public Builder name(String name) {
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
