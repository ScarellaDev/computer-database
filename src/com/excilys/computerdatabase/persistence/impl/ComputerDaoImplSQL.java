package com.excilys.computerdatabase.persistence.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.exception.PersistenceException;
import com.excilys.computerdatabase.persistence.ComputerDao;

/**
* DAO implementation to manage computers in a SQL database.
*
* @author Jeremy SCARELLA
*/

public enum ComputerDaoImplSQL implements ComputerDao {
  /**
  * Instance of ComputerDAO
  */
  INSTANCE;

  public static ComputerDaoImplSQL getInstance() {
    return INSTANCE;
  }

  private static final List<Company> COMPANIES = CompanyDaoImplSQL.getInstance().getAll();
  private Logger                     logger    = LoggerFactory
                                                   .getLogger("com.excilys.computerdatabase.persistence.impl.computerDaoImplSQL");

  public Computer getById(Long id) {
    Computer computer = null;
    Connection connection = null;
    Statement statement = null;
    ResultSet results = null;

    try {
      connection = UtilDaoSQL.getConnection();

      //Query the database
      String query = UtilDaoSQL.SELECT_QUERY + " WHERE c.id=" + id;
      statement = connection.createStatement();
      results = statement.executeQuery(query);

      //Create a computer if there is a result
      if (results.next()) {
        computer = getComputerFromRS(results);
      }
      return computer;
    } catch (SQLException e) {
      logger.error("SQLError in getById() with id = " + id);
      throw new PersistenceException(e);
    } finally {
      if (connection != null) {
        UtilDaoSQL.close(connection, statement, results);
      }
    }
  }

  public List<Computer> getAll() {
    List<Computer> computers = new ArrayList<Computer>();
    Connection connection = null;
    Statement statement = null;
    ResultSet results = null;

    try {
      //Get a connection to the database
      connection = UtilDaoSQL.getConnection();
      //Query the database to get all the computers
      statement = connection.createStatement();
      results = statement.executeQuery(UtilDaoSQL.SELECT_QUERY);
      //Create computers and put them in the computers list with the result
      while (results.next()) {
        computers.add(getComputerFromRS(results));
      }
      return computers;
    } catch (SQLException e) {
      logger.error("SQLError in getAll()");
      throw new PersistenceException(e);
    } finally {
      if (connection != null) {
        UtilDaoSQL.close(connection, statement, results);
      }
    }
  }

  public Computer addByString(String[] params) {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet results = null;

    if (params.length > 0) {
      if (params.length < 5) {
        String name;
        if (params[0].toLowerCase().equals("null")) {
          return null;
        } else {
          name = params[0];
        }

        LocalDateTime introducedL;
        if (params.length > 1 && !params[1].toLowerCase().equals("null")) {
          StringBuffer introducedS = new StringBuffer(params[1]);
          introducedS.append(" 00:00:00");
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
          try {
            introducedL = LocalDateTime.parse(introducedS, formatter);
          } catch (DateTimeParseException e) {
            return null;
          }
        } else {
          introducedL = null;
        }

        LocalDateTime discontinuedL;
        if (params.length > 2 && !params[2].toLowerCase().equals("null")) {
          StringBuffer discontinuedS = new StringBuffer(params[2]);
          discontinuedS.append(" 00:00:00");
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
          try {
            discontinuedL = LocalDateTime.parse(discontinuedS, formatter);
          } catch (DateTimeParseException e) {
            return null;
          }
        } else {
          discontinuedL = null;
        }

        Long companyId;
        if (params.length > 3 && !params[3].toLowerCase().equals("null")) {
          if (params[3].matches("[0-9]+")) {
            companyId = new Long(params[3]);
            if (companyId < 1 || companyId > 43) {
              throw new PersistenceException(
                  "The fourth argument must be a positive integer between [1, 43]");
            }
          } else {
            throw new PersistenceException(
                "The fourth argument must contains digits only and be a positive integer");
          }
        } else {
          companyId = null;
        }

        try {
          //Get a connection to the database
          connection = UtilDaoSQL.getConnection();
          //Create the query
          String insertSQL = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?,?,?,?)";
          statement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
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
          if (statement.executeUpdate() == 0) {
            return null;
          } else {
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
          }
        } catch (SQLException e) {
          logger.error("SQLError in addByString() with params = " + params);
          throw new PersistenceException(e);
        } finally {
          if (connection != null) {
            UtilDaoSQL.close(connection, statement);
          }
        }
      } else {
        throw new PersistenceException("Too many arguments passed (max = 4)");
      }
    } else {
      throw new PersistenceException("Not enough arguments passed (min = 1)");
    }
  }

  public Computer addByComputer(Computer computer) {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet results = null;

    try {
      //Get a connection to the database
      connection = UtilDaoSQL.getConnection();
      //Create the query
      String insertSQL = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?,?,?,?)";
      statement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
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
      if (statement.executeUpdate() == 0) {
        return null;
      } else {
        results = statement.getGeneratedKeys();
        if (results != null && results.next()) {
          Long id = results.getLong(1);
          computer.setId(id);
          return computer;
        } else {
          return null;
        }
      }
    } catch (SQLException e) {
      logger.error("SQLError in addByComputer() with computer = " + computer);
      throw new PersistenceException(e);
    } finally {
      if (connection != null) {
        UtilDaoSQL.close(connection, statement);
      }
    }
  }

  public Computer updateByString(String[] params) {
    if (params.length > 1) {
      if (params.length < 6) {

        Long id;
        Long max = getLastId();
        if (params[0].matches("[0-9]+")) {
          id = new Long(params[0]);
          if (id < 1 || id > max) {
            throw new PersistenceException("The first argument must be a positive integer.");
          } else {
            id = new Long(params[0]);
          }
        } else {
          throw new PersistenceException(
              "The first argument must contains digits only and be a positive integer.");
        }

        String name;
        if (!params[1].toLowerCase().equals("null")) {
          name = params[1];
        } else {
          name = null;
        }

        LocalDateTime introduced;
        if (params.length > 2 && !params[2].toLowerCase().equals("null")) {
          StringBuffer introducedS = new StringBuffer(params[2]);
          introducedS.append(" 00:00:00");
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
          try {
            introduced = LocalDateTime.parse(introducedS, formatter);
          } catch (DateTimeParseException e) {
            return null;
          }
        } else {
          introduced = null;
        }

        LocalDateTime discontinued;
        if (params.length > 3 && !params[3].toLowerCase().equals("null")) {
          StringBuffer discontinuedS = new StringBuffer(params[3]);
          discontinuedS.append(" 00:00:00");
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
          try {
            discontinued = LocalDateTime.parse(discontinuedS, formatter);
          } catch (DateTimeParseException e) {
            return null;
          }
        } else {
          discontinued = null;
        }

        Long companyId;
        Company company = null;
        if (params.length > 4 && !params[4].toLowerCase().equals("null")) {
          if (params[4].matches("[0-9]+")) {
            companyId = new Long(params[4]);
            if (companyId < 1 || companyId > 43) {
              throw new PersistenceException(
                  "The fourth argument must be a positive integer between [1, 43]");
            } else {
              company = COMPANIES.get(companyId.intValue() - 1);
            }
          } else {
            throw new PersistenceException(
                "The fourth argument must contains digits only and be a positive integer");
          }
        } else {
          companyId = null;
        }
        return updateByComputer(new Computer(id, name, introduced, discontinued, company));
      } else {
        throw new PersistenceException("Too many arguments passed (max = 5)");
      }
    } else {
      throw new PersistenceException("Not enough arguments passed (min = 2)");
    }
  }

  public Computer updateByComputer(Computer computer) {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet results = null;

    try {
      //Get a connection to the database
      connection = UtilDaoSQL.getConnection();
      connection.setAutoCommit(false);
      //Create the query
      statement = connection
          .prepareStatement(
              "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id =? WHERE id = ?",
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
      if (statement.executeUpdate() == 0) {
        return null;
      } else {
        connection.commit();
        return computer;
      }
    } catch (SQLException e) {
      logger.error("SQLError in updateByComputer() with computer = " + computer);
      throw new PersistenceException(e);
    } finally {
      if (connection != null) {
        UtilDaoSQL.close(connection, statement);
      }
    }
  }

  public Computer removeById(Long id) {
    Computer computer = getById(id);
    if (computer == null) {
      return null;
    } else {
      return removeByComputer(computer);
    }
  }

  public Computer removeByComputer(Computer computer) {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet results = null;

    if (computer == null) {
      return null;
    }

    try {
      //Get a connection
      connection = UtilDaoSQL.getConnection();
      connection.setAutoCommit(false);
      //Create the query
      statement = connection.prepareStatement("DELETE computer FROM computer WHERE id = ?",
          Statement.RETURN_GENERATED_KEYS);
      statement.setLong(1, computer.getId());

      //Execute the query
      if (statement.executeUpdate() == 0) {
        return null;
      } else {
        connection.commit();
        return computer;
      }
    } catch (SQLException e) {
      logger.error("SQLError in removeByComputer() with id = " + computer.getId());
      throw new PersistenceException(e);
    } finally {
      if (connection != null) {
        UtilDaoSQL.close(connection, statement);
      }
    }
  }

  private Computer getComputerFromRS(final ResultSet rs) {
    Long id = null;
    String name = null;
    Timestamp introducedT;
    LocalDateTime introduced = null;
    Timestamp discontinuedT;
    LocalDateTime discontinued = null;
    Long companyId;
    Company company = null;

    try {
      id = rs.getLong("id");
      name = rs.getString("name");
      introducedT = rs.getTimestamp("introduced");
      if (introducedT != null) {
        introduced = rs.getTimestamp("introduced").toLocalDateTime();
      } else {
        introduced = null;
      }
      discontinuedT = rs.getTimestamp("discontinued");
      if (discontinuedT != null) {
        discontinued = rs.getTimestamp("discontinued").toLocalDateTime();
      } else {
        discontinued = null;
      }
      companyId = rs.getLong("company_Id");
      if (companyId != null) {
        company = new Company(companyId, rs.getString("company"));
      } else {
        company = null;
      }
    } catch (SQLException e) {
      logger.error("SQLError in getComputerFromRS() with rs = " + rs);
      throw new PersistenceException(e);
    }
    return new Computer(id, name, introduced, discontinued, company);
  }

  public Long getLastId() {
    Connection connection = null;
    Statement statement = null;
    ResultSet results = null;
    Long lastId = null;

    try {
      //Get a connection to the database
      connection = UtilDaoSQL.getConnection();
      //Query the database to get all the computers
      statement = connection.createStatement();
      results = statement
          .executeQuery("SELECT MAX(id) AS id FROM `computer-database-db`.computer;");
      //Create computers and put them in the computers list with the result
      if (results.next()) {
        lastId = results.getLong("id");
      }
    } catch (SQLException e) {
      logger.error("SQLError in getAll()");
      throw new PersistenceException(e);
    } finally {
      if (connection != null) {
        UtilDaoSQL.close(connection, statement, results);
      }
    }
    return lastId;
  }
}
