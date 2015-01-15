package com.excilys.computerdatabase.domain;

import java.time.LocalDateTime;

/**
* Computer class
*
* @author Jeremy SCARELLA
*/
public class Computer {
  private Long          id;
  private String        name;
  private LocalDateTime introduced;
  private LocalDateTime discontinued;
  private Company       company;

  public Computer() {}

  public Computer(Long id, String name, LocalDateTime introduced, LocalDateTime discontinued,
      Company company) {
    super();
    this.id = id;
    this.name = name;
    this.introduced = introduced;
    this.discontinued = discontinued;
    this.company = company;
  }

  @Override
  public String toString() {
    return "Computer [id=" + id + ", name=" + name + ", introduced=" + introduced
        + ", discontinued=" + discontinued + ", company=" + company + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((company == null) ? 0 : company.hashCode());
    result = prime * result + ((discontinued == null) ? 0 : discontinued.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((introduced == null) ? 0 : introduced.hashCode());
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

    final Computer other = (Computer) obj;
    if (id == other.getId()) {
      return true;
    }

    if (company == null && other.company != null) {
      return false;
    } else if (!company.equals(other.company)) {
      return false;
    }

    if (discontinued == null && other.discontinued != null) {
      return false;
    } else if (!discontinued.equals(other.discontinued)) {
      return false;
    }

    if (introduced == null && other.introduced != null) {
      return false;
    } else if (!introduced.equals(other.introduced)) {
      return false;
    }

    if (name == null && other.name != null) {
      return false;
    } else if (!name.equals(other.name)) {
      return false;
    }

    return true;
  }

  public static class Builder {
    Computer computer;

    private Builder() {
      computer = new Computer();
    }

    public Builder id(Long id) {
      if (id != null)
        this.computer.id = id;
      return this;
    }

    public Builder name(String name) {
      this.computer.name = name;
      return this;
    }

    public Builder introduced(LocalDateTime introduced) {
      this.computer.introduced = introduced;
      return this;
    }

    public Builder discontinued(LocalDateTime discontinued) {
      this.computer.discontinued = discontinued;
      return this;
    }

    public Builder company(Company company) {
      this.computer.company = company;
      return this;
    }

    public Computer build() {
      return this.computer;
    }
  }

  public static Builder builder() {
    return new Builder();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    if (id != null)
      this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDateTime getIntroduced() {
    return introduced;
  }

  public void setIntroduced(LocalDateTime introduced) {
    this.introduced = introduced;
  }

  public LocalDateTime getDiscontinued() {
    return discontinued;
  }

  public void setDiscontinued(LocalDateTime discontinued) {
    this.discontinued = discontinued;
  }

  public Company getCompany() {
    return company;
  }

  public void setCompany(Company company) {
    this.company = company;
  }
}
