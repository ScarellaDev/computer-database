package com.excilys.computerdatabase.dto;

/**
* ComputerDto class.
*
* @author Jeremy SCARELLA
*/
public class ComputerDto {

  /*
   * Attributes
   */
  private long   id;
  private String name;
  private String introduced;
  private String discontinued;
  private long   companyId;
  private String companyName;

  /*
   * Constructors
   * 
   */
  public ComputerDto() {

  }

  /**
  * @param id
  * @param name
  * @param introduced
  * @param discontinued
  * @param company
  * @param companyName
  */
  public ComputerDto(final long id, final String name, final String introduced,
      final String discontinued, final long companyId, final String companyName) {
    super();
    this.id = id;
    this.name = name;
    this.introduced = introduced;
    this.discontinued = discontinued;
    this.companyId = companyId;
    this.companyName = companyName;
  }

  /*
   * toString/hashCode/equals overrides
   */
  @Override
  public String toString() {
    return "ComputerDto [id=" + id + ", name=" + name + ", introduced=" + introduced
        + ", discontinued=" + discontinued + ", companyId=" + companyId + ", companyName="
        + companyName + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (companyId ^ (companyId >>> 32));
    result = prime * result + ((companyName == null) ? 0 : companyName.hashCode());
    result = prime * result + ((discontinued == null) ? 0 : discontinued.hashCode());
    result = prime * result + (int) (id ^ (id >>> 32));
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

    final ComputerDto other = (ComputerDto) obj;
    if (companyId != other.companyId) {
      return false;
    }

    if (companyName == null && other.companyName != null) {
      return false;
    } else if (!companyName.equals(other.companyName)) {
      return false;
    }

    if (discontinued == null && other.discontinued != null) {
      return false;
    } else if (!discontinued.equals(other.discontinued)) {
      return false;
    }

    if (id != other.id) {
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
  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ComputerDto computerDto;

    private Builder() {
      computerDto = new ComputerDto();
    }

    public Builder id(final long id) {
      computerDto.id = id;
      return this;
    }

    public Builder name(final String name) {
      computerDto.name = name;
      return this;
    }

    public Builder introduced(final String introduced) {
      computerDto.introduced = introduced;
      return this;
    }

    public Builder discontinued(final String discontinued) {
      computerDto.discontinued = discontinued;
      return this;
    }

    public Builder companyId(final long companyId) {
      computerDto.companyId = companyId;
      return this;
    }

    public Builder companyName(final String companyName) {
      computerDto.companyName = companyName;
      return this;
    }

    public ComputerDto build() {
      return computerDto;
    }
  }

  /*
   * Getter/Setter
   */
  public long getId() {
    return id;
  }

  public void setId(final long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getIntroduced() {
    return introduced;
  }

  public void setIntroduced(final String introduced) {
    this.introduced = introduced;
  }

  public String getDiscontinued() {
    return discontinued;
  }

  public void setDiscontinued(final String discontinued) {
    this.discontinued = discontinued;
  }

  public long getCompanyId() {
    return companyId;
  }

  public void setCompanyId(final long companyId) {
    this.companyId = companyId;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(final String companyName) {
    this.companyName = companyName;
  }
}