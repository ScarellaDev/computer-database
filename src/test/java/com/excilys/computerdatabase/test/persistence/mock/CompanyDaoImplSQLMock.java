package com.excilys.computerdatabase.test.persistence.mock;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.persistence.CompanyDao;
import com.excilys.computerdatabase.test.exception.PersistenceExceptionTest;

/**
* Mock Data Access Object for Company, SQL implementation.
* Singleton
* 
* @author Jeremy SCARELLA
*/
public enum CompanyDaoImplSQLMock implements CompanyDao {
  /*
  * Instance of CompanyDaoImplSQLMock
  */
  INSTANCE;

  /*
   * Logger
   */
  private Logger logger = LoggerFactory
                            .getLogger("com.excilys.computerdatabase.persistence.impl.CompanyDaoImplSQLMock");

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
      //Get a connectionection to the database
      connection = UtilDaoSQLMock.getConnection();

      //Create the query
      String query = "SELECT * FROM company WHERE company.id=" + id + ";";
      statement = connection.createStatement();
      //Execute the query
      results = statement.executeQuery(query);
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

      //Create the query
      String query = "SELECT * FROM company;";
      statement = connection.createStatement();
      //Execute the query
      ResultSet results = statement.executeQuery(query);
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