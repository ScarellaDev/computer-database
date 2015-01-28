package com.excilys.computerdatabase.persistence.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.dto.ComputerDto;
import com.excilys.computerdatabase.dto.ComputerDtoConverter;
import com.excilys.computerdatabase.exception.PersistenceException;
import com.excilys.computerdatabase.mapper.IRowMapper;
import com.excilys.computerdatabase.mapper.impl.ComputerRowMapper;
import com.excilys.computerdatabase.persistence.ConnectionManager;
import com.excilys.computerdatabase.persistence.IComputerDao;
import com.excilys.computerdatabase.validator.StringValidation;

/**
* Data Access Object for Computer, SQL implementation.
* 
* @author Jeremy SCARELLA
*/
@Repository
public class ComputerDaoSQL implements IComputerDao {
  /*
   * Instance of ConnectionManager
   */
  @Autowired
  private ConnectionManager    connectionManager;

  /*
   * Instance of ComputerRowMapperImpl
   */
  private IRowMapper<Computer> computerRowMapper = new ComputerRowMapper();

  /*
   * LOGGER
   */
  private static final Logger  LOGGER            = LoggerFactory.getLogger(ComputerDaoSQL.class);

  /**
   * Get the computer in the database corresponding to the id in parameter.
   * @param id : id of the computer in the database.
   * @return The computer that was found or null if there is no computer for this id.
   */
  public Computer getById(Long id) {
    if (id == null) {
      return null;
    }

    Computer computer = null;
    Connection connection = null;
    Statement statement = null;
    ResultSet results = null;

    try {
      connection = connectionManager.getConnection();

      //Query the database
      statement = connection.createStatement();
      results = statement.executeQuery(UtilDaoSQL.COMPUTER_SELECT_QUERY + " WHERE c.id=" + id);

      //Create a computer if there is a result
      if (results.next()) {
        computer = computerRowMapper.mapRow(results);
      }
      return computer;
    } catch (SQLException e) {
      LOGGER.error("SQLError in getById() with id = " + id);
      throw new PersistenceException(e.getMessage(), e);
    } finally {
      connectionManager.close(results);
      connectionManager.close(statement);
    }
  }

  /**
   * Get the List of all the computers in the database.
   * @return List of all the computers in the database.
   */
  public List<Computer> getAll() {
    Connection connection = null;
    Statement statement = null;
    ResultSet results = null;

    try {
      //Get a connection to the database
      connection = connectionManager.getConnection();
      //Query the database to get all the computers
      statement = connection.createStatement();
      results = statement.executeQuery(UtilDaoSQL.COMPUTER_SELECT_QUERY);
      return computerRowMapper.mapRows(results);
    } catch (SQLException e) {
      LOGGER.error("SQLError in getAll()");
      throw new PersistenceException(e.getMessage(), e);
    } finally {
      connectionManager.close(results);
      connectionManager.close(statement);
    }
  }

  /**
   * Add a computer in the database using a Computer instance.
   * @param computer : instance of the computer that needs to be added to the database. Must have a name at least. 
   * @return An instance of the computer that was added to the database or null if the INSERT did not work.
   */
  public Computer addByComputer(Computer computer) {
    if (computer == null) {
      return null;
    }

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet results = null;

    try {
      //Get a connection to the database
      connection = connectionManager.getConnection();
      //Create the query
      statement = connection.prepareStatement(UtilDaoSQL.COMPUTER_INSERT_QUERY,
          Statement.RETURN_GENERATED_KEYS);
      statement.setString(1, computer.getName());
      if (computer.getIntroduced() == null) {
        statement.setNull(2, java.sql.Types.TIMESTAMP);
      } else {
        statement.setTimestamp(2, Timestamp.valueOf(computer.getIntroduced()));
      }
      if (computer.getDiscontinued() == null) {
        statement.setNull(3, java.sql.Types.TIMESTAMP);
      } else {
        statement.setTimestamp(3, Timestamp.valueOf(computer.getDiscontinued()));
      }
      if (computer.getCompany() == null) {
        statement.setNull(4, java.sql.Types.BIGINT);
      } else {
        statement.setLong(4, computer.getCompany().getId());
      }

      //Execute the query
      statement.executeUpdate();
      results = statement.getGeneratedKeys();
      if (results != null && results.next()) {
        Long id = results.getLong(1);
        computer.setId(id);
        return computer;
      } else {
        return null;
      }
    } catch (SQLException e) {
      LOGGER.error("SQLError in addByComputer() with computer = " + computer);
      throw new PersistenceException(e.getMessage(), e);
    } finally {
      connectionManager.close(statement);
    }
  }

  /**
   * Update a computer in the database using a Computer instance.
   * @param computer : instance of the computer that needs to be added to the database. Must have an id at least. 
   * @return An instance of the computer that was updated in the database or null if the UPDATE did not work.
   */
  public Computer updateByComputer(Computer computer) {
    if (computer == null) {
      return null;
    }

    Connection connection = null;
    PreparedStatement statement = null;

    try {
      //Get a connection to the database
      connection = connectionManager.getConnection();
      //Create the query
      statement = connection.prepareStatement(UtilDaoSQL.COMPUTER_UPDATE_QUERY,
          Statement.RETURN_GENERATED_KEYS);
      if (computer.getName() == null) {
        statement.setNull(1, java.sql.Types.VARCHAR);
      } else {
        statement.setString(1, computer.getName());
      }
      if (computer.getIntroduced() == null) {
        statement.setNull(2, java.sql.Types.TIMESTAMP);
      } else {
        statement.setTimestamp(2, Timestamp.valueOf(computer.getIntroduced()));
      }
      if (computer.getDiscontinued() == null) {
        statement.setNull(3, java.sql.Types.TIMESTAMP);
      } else {
        statement.setTimestamp(3, Timestamp.valueOf(computer.getDiscontinued()));
      }
      if (computer.getCompany() != null) {
        statement.setLong(4, computer.getCompany().getId());
      } else {
        statement.setNull(4, java.sql.Types.BIGINT);
      }
      statement.setLong(5, computer.getId());

      //Execute the query
      statement.executeUpdate();
      return computer;
    } catch (SQLException e) {
      LOGGER.error("SQLError in updateByComputer() with computer = " + computer);
      throw new PersistenceException(e.getMessage(), e);
    } finally {
      connectionManager.close(statement);
    }
  }

  /**
   * Remove a computer from the database using its id.
   * @param id : id of the computer to remove.
   * @return An instance of the computer that was removed from the database or null if the DELETE did not work.
   */
  public Computer removeById(Long id) {
    if (id == null) {
      return null;
    }

    Connection connection = null;
    PreparedStatement statement = null;

    Computer computer = null;
    computer = getById(id);

    try {
      //Get a connection
      connection = connectionManager.getConnection();
      //Create the query
      statement = connection.prepareStatement(UtilDaoSQL.COMPUTER_DELETE_QUERY,
          Statement.RETURN_GENERATED_KEYS);
      statement.setLong(1, id);

      //Execute the query
      statement.executeUpdate();
      return computer;
    } catch (SQLException e) {
      LOGGER.error("SQLError in removeById() with id = " + id);
      throw new PersistenceException(e.getMessage(), e);
    } finally {
      connectionManager.close(statement);
    }
  }

  /**
   * Remove all computers attached to the companyId given as parameter from the database.
   * @param id : id of the company that needs its computers to be removed.
   */
  public void removeByCompanyId(Long id) {
    if (id == null) {
      return;
    }

    Connection connection = null;
    PreparedStatement statement = null;

    try {
      connection = connectionManager.getConnection();
      //Create the query
      statement = connection.prepareStatement(UtilDaoSQL.COMPUTER_DELETE_WHERE_COMPANY_QUERY,
          Statement.RETURN_GENERATED_KEYS);
      statement.setLong(1, id);

      //Execute the query
      statement.executeUpdate();
    } catch (SQLException e) {
      LOGGER.error("SQLError in removeByCompanyId() with id = " + id);
      throw new PersistenceException(e.getMessage(), e);
    } finally {
      connectionManager.close(statement);
    }
  }

  /**
   * Remove a list of computers from the database using their ids.
   * @param idList : the list of ids of the computers to remove.
   * @return true if the transaction was a success, false otherwise
   */
  public void removeByIdList(List<Long> idList) {
    if (idList.isEmpty()) {
      return;
    }

    Connection connection = null;
    PreparedStatement statement = null;

    try {
      //Get a connection
      connection = connectionManager.getConnection();

      for (Long id : idList) {
        //Create the query
        statement = connection.prepareStatement(UtilDaoSQL.COMPUTER_DELETE_QUERY,
            Statement.RETURN_GENERATED_KEYS);
        statement.setLong(1, id);
        statement.executeUpdate();
      }
    } catch (SQLException e) {
      LOGGER.error("SQLError in removeByIdList()");
      throw new PersistenceException(e.getMessage(), e);
    } finally {
      connectionManager.close(statement);
    }
  }

  /**
   * Remove a computer from the database using a Computer instance.
   * @param computer : instance of the computer that needs to be removed from the database. Must have an id at least. 
   * @return An instance of the computer that was removed from the database or null if the DELETE did not work.
   */
  public Computer removeByComputer(Computer computer) {
    if (computer == null) {
      return null;
    }

    Connection connection = null;
    PreparedStatement statement = null;

    try {
      //Get a connection
      connection = connectionManager.getConnection();
      //Create the query
      statement = connection.prepareStatement(UtilDaoSQL.COMPUTER_DELETE_QUERY,
          Statement.RETURN_GENERATED_KEYS);
      statement.setLong(1, computer.getId());

      //Execute the query
      statement.executeUpdate();
      return computer;
    } catch (SQLException e) {
      LOGGER.error("SQLError in removeByComputer() with id = " + computer.getId());
      throw new PersistenceException(e.getMessage(), e);
    } finally {
      connectionManager.close(statement);
    }
  }

  /**
   * Get a Page of computers in the database.
   * @param page : a page containing the pageIndex and the max number of elements the page can have
   * @return A Page instance containing a sublist of computers
   */
  @Override
  public Page<ComputerDto> getPagedList(final Page<ComputerDto> page) {
    if (page == null) {
      return null;
    }

    Connection connection = null;
    PreparedStatement countStatement = null;
    PreparedStatement selectStatement = null;
    ResultSet countResults = null;
    ResultSet selectResults = null;

    try {
      connection = connectionManager.getConnection();

      //Create & execute the counting query
      if (StringValidation.isEmpty(page.getSearch())) {
        countStatement = connection.prepareStatement(UtilDaoSQL.COMPUTER_COUNT_QUERY);
        countResults = countStatement.executeQuery();
      } else {
        countStatement = connection.prepareStatement(UtilDaoSQL.COMPUTER_COUNT_QUERY
            + " WHERE c.name LIKE ? OR company.name LIKE ?;");
        countStatement.setString(1, page.getSearch() + "%");
        countStatement.setString(2, page.getSearch() + "%");
        countResults = countStatement.executeQuery();
      }

      //Set the number of results of the page with the result
      countResults.next();
      page.setTotalNbElements(countResults.getInt("total"));

      page.refreshNbPages();

      //Create the SELECT query
      if (StringValidation.isEmpty(page.getSearch())) {
        selectStatement = connection.prepareStatement(UtilDaoSQL.COMPUTER_SELECT_QUERY
            + " ORDER BY " + Page.getColumnNames()[page.getSort()] + " " + page.getOrder()
            + " LIMIT ? OFFSET ?;");
        selectStatement.setInt(1, page.getNbElementsPerPage());
        selectStatement.setInt(2, (page.getPageIndex() - 1) * page.getNbElementsPerPage());
      } else {
        selectStatement = connection.prepareStatement(UtilDaoSQL.COMPUTER_SELECT_QUERY
            + " WHERE c.name LIKE ? OR company.name LIKE ? ORDER BY "
            + Page.getColumnNames()[page.getSort()] + " " + page.getOrder() + " LIMIT ? OFFSET ?;");
        selectStatement.setString(1, page.getSearch() + "%");
        selectStatement.setString(2, page.getSearch() + "%");
        selectStatement.setInt(3, page.getNbElementsPerPage());
        selectStatement.setInt(4, (page.getPageIndex() - 1) * page.getNbElementsPerPage());
      }

      //Execute the SELECT query
      selectResults = selectStatement.executeQuery();

      page.setList(ComputerDtoConverter.toDto(computerRowMapper.mapRows(selectResults)));
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
