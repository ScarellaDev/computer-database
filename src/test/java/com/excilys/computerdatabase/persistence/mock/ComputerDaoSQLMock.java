package com.excilys.computerdatabase.persistence.mock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.dto.ComputerDto;
import com.excilys.computerdatabase.dto.ComputerDtoConverter;
import com.excilys.computerdatabase.exception.PersistenceException;
import com.excilys.computerdatabase.mapper.IRowMapper;
import com.excilys.computerdatabase.mapper.impl.ComputerRowMapper;
import com.excilys.computerdatabase.persistence.ConnectionManager;
import com.excilys.computerdatabase.persistence.IComputerDao;
import com.excilys.computerdatabase.persistence.impl.UtilDaoSQL;
import com.excilys.computerdatabase.validator.StringValidation;

/**
* Data Access Object for Computer, SQL implementation.
* Singleton
* 
* @author Jeremy SCARELLA
*/
public enum ComputerDaoSQLMock implements IComputerDao {
  /*
  * Instance of ComputerDaoSQLMock
  */
  INSTANCE;

  /*
   * CONNECTION_MANAGER
   */
  private static final ConnectionManager CM                  = ConnectionManager.INSTANCE;

  /*
   * Instance of ComputerRowMapperImpl
   */
  private IRowMapper<Computer>           computerRowMapper   = new ComputerRowMapper();

  /*
   * CONSTANT List of the companies that are in the database (cache)
   */
  private static final List<Company>     COMPANIES           = CompanyDaoSQLMock.INSTANCE.getAll();

  /*
   * DATE TIME FORMATTER
   */
  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
                                                                 .ofPattern("yyyy-MM-dd HH:mm:ss");

  /*
   * LOGGER
   */
  private static final Logger            LOGGER              = LoggerFactory
                                                                 .getLogger(ComputerDaoSQLMock.class);

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
      connection = CM.getConnection();

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
      CM.close(results);
      CM.close(statement);
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
      connection = CM.getConnection();
      //Query the database to get all the computers
      statement = connection.createStatement();
      results = statement.executeQuery(UtilDaoSQL.COMPUTER_SELECT_QUERY);
      return computerRowMapper.mapRows(results);
    } catch (SQLException e) {
      LOGGER.error("SQLError in getAll()");
      throw new PersistenceException(e.getMessage(), e);
    } finally {
      CM.close(results);
      CM.close(statement);
    }
  }

  /**
   * Add a computer in the database using a table of Strings as parameters.
   * @param params : String table composed of "name" (mandatory), "introduced" (date format: yyyy-MM-dd), discontinued (date format: yyyy-MM-dd), "companyId".
   * Use the String "null" to skip a value.
   * @return An instance of the computer that was added to the database or null if the INSERT did not work.
   */
  public Computer addByString(String[] params) {
    if (params == null) {
      return null;
    }

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet results = null;

    if (params.length > 0) {
      if (params.length < 5) {
        String name;
        if (params[0].toLowerCase().equals("null") || StringValidation.isEmpty(params[0])) {
          return null;
        } else {
          name = params[0];
        }

        LocalDateTime introducedL;
        if (params.length > 1 && !params[1].toLowerCase().equals("null")
            && StringValidation.isDate(params[1])) {
          StringBuffer introducedS = new StringBuffer(params[1]);
          introducedS.append(" 00:00:00");
          try {
            introducedL = LocalDateTime.parse(introducedS, DATE_TIME_FORMATTER);
          } catch (DateTimeParseException e) {
            return null;
          }
        } else {
          introducedL = null;
        }

        LocalDateTime discontinuedL;
        if (params.length > 2 && !params[2].toLowerCase().equals("null")
            && StringValidation.isDate(params[2])
            && StringValidation.isLaterDate(params[1], params[2])) {
          StringBuffer discontinuedS = new StringBuffer(params[2]);
          discontinuedS.append(" 00:00:00");
          try {
            discontinuedL = LocalDateTime.parse(discontinuedS, DATE_TIME_FORMATTER);
          } catch (DateTimeParseException e) {
            return null;
          }
        } else {
          discontinuedL = null;
        }

        Long companyId;
        if (params.length > 3 && !params[3].toLowerCase().equals("null")
            && StringValidation.isPositiveLong(params[3])) {
          companyId = new Long(params[3]);
        } else {
          companyId = null;
        }

        try {
          //Get a connection to the database
          connection = CM.getConnection();
          //Create the query
          statement = connection.prepareStatement(UtilDaoSQL.COMPUTER_INSERT_QUERY,
              Statement.RETURN_GENERATED_KEYS);
          statement.setString(1, name);
          if (introducedL == null) {
            statement.setNull(2, java.sql.Types.TIMESTAMP);
          } else {
            statement.setTimestamp(2, Timestamp.valueOf(introducedL));
          }
          if (discontinuedL == null) {
            statement.setNull(3, java.sql.Types.TIMESTAMP);
          } else {
            statement.setTimestamp(3, Timestamp.valueOf(discontinuedL));
          }
          if (companyId == null) {
            statement.setNull(4, java.sql.Types.BIGINT);
          } else {
            statement.setLong(4, companyId);
          }

          //Execute the query
          statement.executeUpdate();
          results = statement.getGeneratedKeys();
          if (results != null && results.next()) {
            Long id = results.getLong(1);
            Computer.Builder builder = Computer.builder();
            builder.id(id);
            builder.name(name);
            builder.introduced(introducedL);
            builder.discontinued(discontinuedL);
            if (companyId != null) {
              builder.company(COMPANIES.get(companyId.intValue() - 1));
            }
            return builder.build();
          } else {
            return null;
          }
        } catch (SQLException e) {
          LOGGER.error("SQLError in addByString() with params = " + params);
          throw new PersistenceException(e.getMessage(), e);
        } finally {
          CM.close(statement);
        }
      } else {
        throw new PersistenceException("Too many arguments passed (max = 4)");
      }
    } else {
      throw new PersistenceException("Not enough arguments passed (min = 1)");
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
      connection = CM.getConnection();
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
      CM.close(statement);
    }
  }

  /**
   * Update a computer in the database using a table of Strings as parameters.
   * @param params : String table composed of "id" (mandatory), "name", "introduced" (date format: yyyy-MM-dd), discontinued (date format: yyyy-MM-dd), "companyId".
   * All the attributes of the updated computer are changed.
   * @return An instance of the computer that was updated in the database or null if the UPDATE did not work.
   */
  public Computer updateByString(String[] params) {
    if (params == null) {
      return null;
    }

    if (params.length > 1) {
      if (params.length < 6) {

        Computer.Builder builder = Computer.builder();

        if (StringValidation.isPositiveLong(params[0])) {
          builder.id(new Long(params[0]));
        } else {
          return null;
        }

        if (!params[1].toLowerCase().equals("null") && !StringValidation.isEmpty(params[1])) {
          builder.name(params[1]);
        } else {
          return null;
        }

        if (params.length > 2 && !params[2].toLowerCase().equals("null")
            && StringValidation.isDate(params[2])) {
          StringBuffer introducedS = new StringBuffer(params[2]);
          introducedS.append(" 00:00:00");
          try {
            builder.introduced(LocalDateTime.parse(introducedS, DATE_TIME_FORMATTER));
          } catch (DateTimeParseException e) {
            return null;
          }
        }

        if (params.length > 3 && !params[3].toLowerCase().equals("null")
            && StringValidation.isDate(params[3])
            && StringValidation.isLaterDate(params[2], params[3])) {
          StringBuffer discontinuedS = new StringBuffer(params[3]);
          discontinuedS.append(" 00:00:00");
          try {
            builder.discontinued(LocalDateTime.parse(discontinuedS, DATE_TIME_FORMATTER));
          } catch (DateTimeParseException e) {
            return null;
          }
        }

        if (params.length > 4 && !params[4].toLowerCase().equals("null")
            && StringValidation.isPositiveLong(params[4])) {
          builder.company(COMPANIES.get(new Long(params[4]).intValue() - 1));
        }

        return updateByComputer(builder.build());
      } else {
        throw new PersistenceException("Too many arguments passed (max = 5)");
      }
    } else {
      throw new PersistenceException("Not enough arguments passed (min = 2)");
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
      connection = CM.getConnection();
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
      CM.close(statement);
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
      connection = CM.getConnection();
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
      CM.close(statement);
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
      connection = CM.getConnection();
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
      CM.close(statement);
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
      connection = CM.getConnection();

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
      CM.close(statement);
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
      connection = CM.getConnection();
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
      CM.close(statement);
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
      connection = CM.getConnection();

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
      CM.close(countResults);
      CM.close(selectResults);
      CM.close(countStatement);
      CM.close(selectStatement);
    }
  }
}
