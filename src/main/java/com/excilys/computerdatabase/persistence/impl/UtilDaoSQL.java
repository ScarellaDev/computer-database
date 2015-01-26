package com.excilys.computerdatabase.persistence.impl;

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
   * INSERT query for computer table
   */
  public static final String COMPUTER_INSERT_QUERY               = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?,?,?,?)";

  /*
   * UPDATE query for computer table
   */
  public static final String COMPUTER_UPDATE_QUERY               = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id =? WHERE id = ?";

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
   * DELETE query for company table
   */
  public static final String COMPANY_DELETE_QUERY                = "DELETE company FROM company WHERE id = ?";

  /*
   * COUNT query for company table
   */
  public static final String COMPANY_COUNT_QUERY                 = "SELECT COUNT(id) AS total FROM company";
}
