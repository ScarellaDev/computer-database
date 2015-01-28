package com.excilys.computerdatabase.persistence.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.exception.PersistenceException;
import com.excilys.computerdatabase.mapper.IRowMapper;
import com.excilys.computerdatabase.mapper.impl.CompanyRowMapper;
import com.excilys.computerdatabase.persistence.ConnectionManager;
import com.excilys.computerdatabase.persistence.ICompanyDao;

/**
* Data Access Object for Company, SQL implementation.
* 
* @author Jeremy SCARELLA
*/
@Repository
public class CompanyDaoSQL implements ICompanyDao {
  /*
   * Instance of ConnectionManager
   */
  @Autowired
  private ConnectionManager   connectionManager;

  /*
   * Instance of CompanyRowMapperImpl
   */
  private IRowMapper<Company> companyMapper = new CompanyRowMapper();

  /*
   * LOGGER
   */
  private static final Logger LOGGER        = LoggerFactory.getLogger(CompanyDaoSQL.class);

  /**
   * Get the company in the database corresponding to the id in parameter.
   * @param id : id of the company in the database.
   * @return The company that was found or null if there is no company for this id.
   */
  public Company getById(Long id) {
    if (id == null) {
      return null;
    }

    Connection connection = null;
    Statement statement = null;
    ResultSet results = null;
    Company company = null;

    try {
      connection = connectionManager.getConnection();

      //Create the query
      statement = connection.createStatement();
      //Execute the query
      results = statement.executeQuery(UtilDaoSQL.COMPANY_SELECT_QUERY + " WHERE company.id=" + id
          + ";");
      //Create a company if there is a result
      if (results.next()) {
        company = companyMapper.mapRow(results);
      }
      return company;
    } catch (SQLException e) {
      LOGGER.error("SQLError in getById() with id = " + id);
      throw new PersistenceException(e.getMessage(), e);
    } finally {
      connectionManager.close(results);
      connectionManager.close(statement);
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

    try {
      connection = connectionManager.getConnection();

      //Create the query
      statement = connection.createStatement();
      //Execute the query
      results = statement.executeQuery(UtilDaoSQL.COMPANY_SELECT_QUERY);
      return companyMapper.mapRows(results);
    } catch (SQLException e) {
      LOGGER.error("SQLError in getAll()");
      throw new PersistenceException(e.getMessage(), e);
    } finally {
      connectionManager.close(statement);
    }
  }

  /**
   * Remove a company from the database using its id.
   * @param connection : the shared Connection for the CompanyServiceJDBC.removeById().
   * @param id : id of the company to remove.
   */
  public void removeById(Long id) {
    if (id == null) {
      return;
    }

    Connection connection = null;
    PreparedStatement statement = null;

    try {
      connection = connectionManager.getConnection();
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
      connectionManager.close(statement);
    }
  }

  /**
   * Get a Page of companies in the database.
   * @param page : a page containing the pageIndex and the max number of elements the page can have
   * @return A Page instance containing a sublist of companies
   */
  @Override
  public Page<Company> getPagedList(Page<Company> page) {
    if (page == null) {
      return null;
    }

    Connection connection = null;
    Statement countStatement = null;
    PreparedStatement selectStatement = null;
    ResultSet countResults = null;
    ResultSet selectResults = null;

    try {
      connection = connectionManager.getConnection();

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

      page.setList(companyMapper.mapRows(selectResults));
      return page;
    } catch (SQLException e) {
      LOGGER.error("SQLError in getPagedList() with page = " + page);
      throw new PersistenceException(e.getMessage(), e);
    } finally {
      connectionManager.close(countResults);
      connectionManager.close(selectResults);
      connectionManager.close(countStatement);
      connectionManager.close(selectStatement);
    }
  }
}