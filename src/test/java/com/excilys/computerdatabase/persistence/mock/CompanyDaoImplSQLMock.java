package com.excilys.computerdatabase.persistence.mock;

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
import com.excilys.computerdatabase.exception.PersistenceExceptionTest;
import com.excilys.computerdatabase.persistence.ICompanyDao;

/**
* Mock Data Access Object for Company, SQL implementation.
* Singleton
* 
* @author Jeremy SCARELLA
*/
public enum CompanyDaoImplSQLMock implements ICompanyDao {
  /*
  * Instance of CompanyDaoImplSQLMock
  */
  INSTANCE;

  /*
   * Logger
   */
  private Logger logger = LoggerFactory.getLogger(CompanyDaoImplSQLMock.class);

  /**
  * Return the instance of CompanyDaoImplSQLMock.
  * @return Instance of CompanyDaoImplSQLMock.
  */
  public static CompanyDaoImplSQLMock getInstance() {
    return INSTANCE;
  }

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
      connection = UtilDaoSQLMock.getConnection();

      statement = connection.createStatement();
      //Execute the query
      results = statement.executeQuery(UtilDaoSQLMock.COMPANY_SELECT_QUERY + " WHERE company.id="
          + id + ";");
      //Create a company if there is a result
      if (results.next()) {
        company = getCompanyFromRS(results);
      }
      return company;
    } catch (SQLException e) {
      logger.error("SQLError in getById() with id = " + id);
      throw new PersistenceExceptionTest(e);
    } finally {
      if (connection != null) {
        UtilDaoSQLMock.close(connection, statement, results);
      }
    }
  }

  /**
   * Get the List of all the companies in the database.
   * @return List of all the companies in the database.
   */
  public List<Company> getAll() {
    Connection connection = null;
    Statement statement = null;
    List<Company> companies = new ArrayList<Company>();
    Company company;
    try {
      connection = UtilDaoSQLMock.getConnection();

      statement = connection.createStatement();
      //Execute the query
      ResultSet results = statement.executeQuery(UtilDaoSQLMock.COMPANY_SELECT_QUERY);
      //Create companies with the results
      while (results.next()) {
        company = new Company();
        company.setId(results.getLong("id"));
        company.setName(results.getString("name"));
        companies.add(company);
      }
      return companies;
    } catch (SQLException e) {
      logger.error("SQLError in getAll()");
      throw new PersistenceExceptionTest(e);
    } finally {
      if (connection != null) {
        UtilDaoSQLMock.close(connection, statement);
      }
    }
  }

  /**
   * Get a Page of companies in the database.
   * @param page : a page containing the pageIndex and the max number of elements the page can have
   * @return A Page instance containing a sublist of companies
   */
  @Override
  public Page<Company> getPagedList(final Page<Company> page) {
    Connection connection = null;
    Statement countStatement = null;
    PreparedStatement selectStatement = null;
    ResultSet countResults = null;
    ResultSet selectResults = null;
    final List<Company> companies = new ArrayList<Company>();

    try {
      connection = UtilDaoSQLMock.getConnection();

      //Create & execute the counting query
      countStatement = connection.createStatement();
      final ResultSet countResult = countStatement.executeQuery(UtilDaoSQLMock.COMPANY_COUNT_QUERY);

      //Set the number of results of the page with the result
      countResult.next();
      page.setTotalNbElements(countResult.getInt("total"));
      page.refreshNbPages();

      //Create the SELECT query
      selectStatement = connection.prepareStatement(UtilDaoSQLMock.COMPANY_SELECT_QUERY
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
      logger.error("SQLError in getPagedList() with page = " + page);
      throw new PersistenceExceptionTest(e);
    } finally {
      UtilDaoSQLMock.close(countResults);
      UtilDaoSQLMock.close(selectResults);
      UtilDaoSQLMock.close(countStatement);
      UtilDaoSQLMock.close(selectStatement);
      UtilDaoSQLMock.close(connection);
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
      logger.error("SQLError in getCompanyFromRS() with rs = " + rs);
      throw new PersistenceExceptionTest(e);
    }
  }
}