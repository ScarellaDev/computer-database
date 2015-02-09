package com.excilys.computerdatabase.persistence.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;

public enum UtilDaoSQL {

  /*
   * Instance of UtilDaoSQL
   */
  INSTANCE;

  /*
   * SELECT query for computer table
   */
  public static final String COMPUTER_SELECT_QUERY               = "SELECT c.id, c.name, c.introduced, c.discontinued, company_id, company.name as company_name FROM computer c LEFT JOIN company ON company.id=c.company_id";

  /*
   * SELECT query for company table
   */
  public static final String COMPUTER_SELECT_WITH_ID_QUERY       = "SELECT c.id, c.name, c.introduced, c.discontinued, company_id, company.name as company_name FROM computer c LEFT JOIN company ON company.id=c.company_id WHERE c.id=?;";

  /*
   * INSERT query for computer table
   */
  public static final String COMPUTER_INSERT_QUERY               = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?,?,?,?)";

  /*
   * UPDATE query for computer table
   */
  public static final String COMPUTER_UPDATE_QUERY               = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?";

  /*
   * DELETE query for computer table
   */
  public static final String COMPUTER_DELETE_QUERY               = "DELETE computer FROM computer WHERE id = ?";

  /*
   * DELETE query for computer table, delete all computers with company.id = ?
   */
  public static final String COMPUTER_DELETE_WHERE_COMPANY_QUERY = "DELETE c FROM computer c LEFT JOIN company ON company.id=c.company_id WHERE company.id = ?";

  /*
   * COUNT query for computer table
   */
  public static final String COMPUTER_COUNT_QUERY                = "SELECT COUNT(c.id) AS total FROM computer c LEFT JOIN company ON company.id=c.company_id";

  /*
   * MAX query for computer table
   */
  public static final String COMPUTER_MAX_QUERY                  = "SELECT MAX(id) AS id FROM computer";

  /*
   * SELECT query for company table
   */
  public static final String COMPANY_SELECT_QUERY                = "SELECT * FROM company";

  /*
  * SELECT query for company table
  */
  public static final String COMPANY_LIMITED_SELECT_QUERY        = "SELECT * FROM company LIMIT ? OFFSET ?;";

  /*
   * SELECT query for company table
   */
  public static final String COMPANY_SELECT_WITH_ID_QUERY        = "SELECT * FROM company WHERE company.id=?;";

  /*
   * DELETE query for company table
   */
  public static final String COMPANY_DELETE_QUERY                = "DELETE company FROM company WHERE id = ?;";

  /*
   * COUNT query for company table
   */
  public static final String COMPANY_COUNT_QUERY                 = "SELECT COUNT(id) AS total FROM company";

  /**
   * Method converting LocaleDateTime to Timestamp
   * @param date : LocaleDateTime to convert
   * @return Timestamp value of date or null
   */
  public static Timestamp toTimestamp(LocalDateTime date) {
    if (date == null) {
      return null;
    } else {
      return Timestamp.valueOf(date);
    }
  }

  /**
   * Method retrieving id of Object
   * @param obj : object which id is being retrieved
   * @return a Long id or null
   */
  public static Long getCompanyId(Object obj) {
    if (obj == null) {
      return null;
    } else if (obj instanceof Computer) {
      Company company = ((Computer) obj).getCompany();
      if (company == null) {
        return null;
      } else {
        return company.getId();
      }
    } else if (obj instanceof Company) {
      return ((Company) obj).getId();
    } else {
      return null;
    }
  }
}
