package com.excilys.computerdatabase.persistence.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.exception.PersistenceException;
import com.excilys.computerdatabase.persistence.ICompanyDao;

/**
* Data Access Object for Company, SQL implementation.
* Singleton
* 
* @author Jeremy SCARELLA
*/
public enum CompanyDaoImplSQL implements ICompanyDao {
  /*
  * Instance of CompanyDaoImplSQL
  */
  INSTANCE;

  /*
   * LOGGER
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(CompanyDaoImplSQL.class);

  /**
   * Get the company in the database corresponding to the id in parameter.
   * @param id : id of the company in the database.
   * @return The company that was found or null if there is no company for this id.
   */
  public Company getById(Long id) {
    Connection connection = null;
    Statement statement = null;
    ResultSet results = null;
    Company company = null;

    try {
      connection = UtilDaoSQL.getConnection();

      //Create the query
      statement = connection.createStatement();
      //Execute the query
      results = statement.executeQuery(UtilDaoSQL.COMPANY_SELECT_QUERY + " WHERE company.id=" + id
          + ";");
      //Create a company if there is a result
      if (results.next()) {
        company = getCompanyFromRS(results);
      }
      return company;
    } catch (SQLException e) {
      LOGGER.error("SQLError in getById() with id = " + id);
      throw new PersistenceException(e.getMessage(), e);
    } finally {
      UtilDaoSQL.close(results);
      UtilDaoSQL.close(statement);
      UtilDaoSQL.close(connection);
    }
  }

  /**
   * Get the List of all the companies in the database.
   * @return List of all the companies in the database.
   */
  public List<Company> getAll() {
    Connection connection = null;
    Statement statement = null;
    ResultSet results = null;
    List<Company> companies = new ArrayList<Company>();
    Company company;
    try {
      connection = UtilDaoSQL.getConnection();

      //Create the query
      statement = connection.createStatement();
      //Execute the query
      results = statement.executeQuery(UtilDaoSQL.COMPANY_SELECT_QUERY);
      //Create companies with the results
      while (results.next()) {
        company = new Company();
        company.setId(results.getLong("id"));
        company.setName(results.getString("name"));
        companies.add(company);
      }
      return companies;
    } catch (SQLException e) {
      LOGGER.error("SQLError in getAll()");
      throw new PersistenceException(e.getMessage(), e);
    } finally {
      UtilDaoSQL.close(statement);
      UtilDaoSQL.close(connection);
    }
  }

  /**
   * Remove a company from the database using its id.
   * @param id : id of the company to remove.
   */
  public void removeById(Connection connection, Long id) throws PersistenceException {
    PreparedStatement statement = null;
    if (id == null) {
      return;
    }

    try {
      //Create the query
      statement = connection.prepareStatement(UtilDaoSQL.COMPANY_DELETE_QUERY,
          Statement.RETURN_GENERATED_KEYS);
      statement.setLong(1, id);

      //Execute the query
      statement.executeUpdate();
    } catch (SQLException e) {
      LOGGER.error("SQLError in removeById() with id = " + id);
      throw new PersistenceException(e.getMessage(), e);
    } finally {
      UtilDaoSQL.close(statement);
    }
  }

  /**
   * Get a Page of companies in the database.
   * @param page : a page containing the pageIndex and the max number of elements the page can have
   * @return A Page instance containing a sublist of companies
   */
  @Override
  public Page<Company> getPagedList(Page<Company> page) {
    Connection connection = null;
    Statement countStatement = null;
    PreparedStatement selectStatement = null;
    ResultSet countResults = null;
    ResultSet selectResults = null;
    final List<Company> companies = new ArrayList<Company>();

    try {
      connection = UtilDaoSQL.getConnection();

      //Create & execute the counting query
      countStatement = connection.createStatement();
      countResults = countStatement.executeQuery(UtilDaoSQL.COMPANY_COUNT_QUERY);

      //Set the number of results of the page with the result
      countResults.next();
      page.setTotalNbElements(countResults.getInt("total"));

      page.refreshNbPages();

      //Create the SELECT query
      selectStatement = connection.prepareStatement(UtilDaoSQL.COMPANY_SELECT_QUERY
          + " LIMIT ? OFFSET ?;");
      selectStatement.setInt(1, page.getNbElementsPerPage());
      selectStatement.setInt(2, (page.getPageIndex() - 1) * page.getNbElementsPerPage());

      //Execute the SELECT query
      selectResults = selectStatement.executeQuery();

      //Create the computers with the results
      while (selectResults.next()) {
        companies.add(getCompanyFromRS(selectResults));
      }
      page.setList(companies);
      return page;
    } catch (SQLException e) {
      LOGGER.error("SQLError in getPagedList() with page = " + page);
      throw new PersistenceException(e.getMessage(), e);
    } finally {
      UtilDaoSQL.close(countResults);
      UtilDaoSQL.close(selectResults);
      UtilDaoSQL.close(countStatement);
      UtilDaoSQL.close(selectStatement);
      UtilDaoSQL.close(connection);
    }
  }

  /**
  * Get a Company instance based on the columns of a row of a ResultSet.
  * @param rs : ResultSet on a row containing a company.
  * @return The company instance extracted from the ResulSet.
  */
  private Company getCompanyFromRS(final ResultSet rs) {
    try {
      return Company.builder().id(rs.getLong("id")).name(rs.getString("name")).build();
    } catch (SQLException e) {
      LOGGER.error("SQLError in getCompanyFromRS() with rs = " + rs);
      throw new PersistenceException(e.getMessage(), e);
    }
  }
}