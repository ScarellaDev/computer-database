package com.excilys.computerdatabase.domain;

import java.time.LocalDate;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.excilys.computerdatabase.persistence.LocalDatePersistenceConverter;

/**
* Computer class.
*
* @author Jeremy SCARELLA
*/
@Entity
@Table(name = "computer")
public class Computer {

  /*
   * Attributes
   */
  @Id
  @GeneratedValue
  private Long      id;

  private String    name;

  @Convert(converter = LocalDatePersistenceConverter.class)
  private LocalDate introduced;

  @Convert(converter = LocalDatePersistenceConverter.class)
  private LocalDate discontinued;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "company_id")
  private Company   company;

  /*
   * Constructors
   * 
   */
  public Computer() {

  }

  /**
  * @param id
  * @param name
  * @param introduced
  * @param discontinued
  * @param company
  */
  public Computer(final Long id, final String name, final LocalDate introduced,
      final LocalDate discontinued, final Company company) {
    super();
    this.id = id;
    this.name = name;
    this.introduced = introduced;
    this.discontinued = discontinued;
    this.company = company;
  }

  /*
   * toString/hashCode/equals overrides
   */
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

  /*
   * Builder static class
   */
  public static final class Builder {
    Computer computer;

    private Builder() {
      computer = new Computer();
    }

    public Builder id(final Long id) {
      if (id != null) {
        this.computer.id = id;
      }
      return this;
    }

    public Builder name(final String name) {
      this.computer.name = name;
      return this;
    }

    public Builder introduced(final LocalDate introduced) {
      this.computer.introduced = introduced;
      return this;
    }

    public Builder discontinued(final LocalDate discontinued) {
      this.computer.discontinued = discontinued;
      return this;
    }

    public Builder company(final Company company) {
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

  /*
   * Getter/Setter
   */
  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    if (id != null) {
      this.id = id;
    }
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public LocalDate getIntroduced() {
    return introduced;
  }

  public void setIntroduced(final LocalDate introduced) {
    this.introduced = introduced;
  }

  public LocalDate getDiscontinued() {
    return discontinued;
  }

  public void setDiscontinued(final LocalDate discontinued) {
    this.discontinued = discontinued;
  }

  public Company getCompany() {
    return company;
  }

  public void setCompany(final Company company) {
    this.company = company;
  }
}
