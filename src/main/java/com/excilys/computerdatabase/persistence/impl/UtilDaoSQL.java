package com.excilys.computerdatabase.persistence.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerdatabase.exception.PersistenceException;

/**
* Util class to create SQL database connections.
*
* @author Jeremy SCARELLA
*/
public enum UtilDaoSQL {

  /*
   * Instance of UtilDaoSQL
   */
  INSTANCE;

  /*
   * DRIVER to use for mysql database.
   */
  private static final String DB_DRIVER;

  /*
  * URL to the database server.
  */
  private static final String DB_URL;

  /*
  * USERNAME for the database.
  */
  private static final String DB_USERNAME;

  /*
  * PASSWORD for the database.
  */
  private static final String DB_PASSWORD;

  /*
   * SELECT query for computer table
   */
  public static final String  COMPUTER_SELECT_QUERY = "SELECT c.id, c.name, c.introduced, c.discontinued, company_id, company.name as company FROM computer c LEFT JOIN company ON company.id=c.company_id";

  /*
   * INSERT query for computer table
   */
  public static final String  COMPUTER_INSERT_QUERY = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?,?,?,?)";

  /*
   * UPDATE query for computer table
   */
  public static final String  COMPUTER_UPDATE_QUERY = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id =? WHERE id = ?";

  /*
   * DELETE query for computer table
   */
  public static final String  COMPUTER_DELETE_QUERY = "DELETE computer FROM computer WHERE id = ?";

  /*
   * COUNT query for computer table
   */
  public static final String  COMPUTER_COUNT_QUERY  = "SELECT COUNT(id) AS total FROM computer";

  /*
   * MAX query for computer table
   */
  public static final String  COMPUTER_MAX_QUERY    = "SELECT MAX(id) AS id FROM computer";

  /*
   * SELECT query for company table
   */
  public static final String  COMPANY_SELECT_QUERY  = "SELECT * FROM company";

  /*
   * COUNT query for company table
   */
  public static final String  COMPANY_COUNT_QUERY   = "SELECT COUNT(id) AS total FROM company";

  /*
   * LOGGER
   */
  private static final Logger LOGGER                = LoggerFactory.getLogger(UtilDaoSQL.class);

  /**
   * Static instruction block that loads the JDBC driver once
   */
  static {
    // Load db.properties
    Properties properties = new Properties();
    InputStream input = UtilDaoSQL.class.getClassLoader().getResourceAsStream("db.properties");
    try {
      properties.load(input);
    } catch (IOException e) {
      LOGGER.error("IOException: couldn't load db.properties file");
      throw new PersistenceException(e.getMessage(), e);
    } finally {
      try {
        if (input != null) {
          input.close();
        }
      } catch (IOException e) {
        LOGGER.error("IOException: couldn't close InputStream during db.properties file loading");
        throw new PersistenceException(e.getMessage(), e);
      }
    }
    DB_DRIVER = properties.getProperty("db.driver");
    DB_URL = properties.getProperty("db.url");
    DB_USERNAME = properties.getProperty("db.username");
    DB_PASSWORD = properties.getProperty("db.password");

    // Load the driver for mysql database
    try {
      Class.forName(DB_DRIVER);
    } catch (ClassNotFoundException e) {
      LOGGER.error("ClassNotFoundException: MySQL JDBC driver not found");
      throw new PersistenceException(e.getMessage(), e);
    }
  }

  /**
  * Retrieve a SQL connection to the database.
  * @return The {@link Connection} instance.
  * @throws SQLException : if a database access error occurs.
  */
  public static Connection getConnection() {
    try {
      return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    } catch (SQLException e) {
      LOGGER.error("SQLException: couldn't connect to the database");
      throw new PersistenceException(e.getMessage(), e);
    }
  }

  /**
   * Close Connection if it is not null.
   * @param connection
   */
  public static void close(Connection connection) {
    if (connection != null) {
      try {
        connection.close();
      } catch (SQLException e) {
        LOGGER.warn("SQLException: couldn't close Connection");
        throw new PersistenceException(e.getMessage(), e);
      }
    }
  }

  /**
   * Close Statement if it is not null.
   * @param statement
   */
  public static void close(Statement statement) {
    if (statement != null) {
      try {
        statement.close();
      } catch (SQLException e) {
        LOGGER.warn("SQLException: couldn't close Statement");
        throw new PersistenceException(e.getMessage(), e);
      }
    }
  }

  /**
   * Close PreparedStatement if it is not null.
   * @param pStatement
   */
  public static void close(PreparedStatement pStatement) {
    if (pStatement != null) {
      try {
        pStatement.close();
      } catch (SQLException e) {
        LOGGER.warn("SQLException: couldn't close PreparedStatement");
        throw new PersistenceException(e.getMessage(), e);
      }
    }
  }

  /**
   * Close ResultSet if it is not null.
   * @param results
   */
  public static void close(ResultSet results) {
    if (results != null) {
      try {
        results.close();
      } catch (SQLException e) {
        LOGGER.warn("SQLException: couldn't close ResultSet");
        throw new PersistenceException(e.getMessage(), e);
      }
    }
  }

  /**
   * Close elements if they are not null.
   * @param connection
   * @param statement
   */
  public static void close(Connection connection, Statement statement) {
    if (statement != null) {
      try {
        statement.close();
      } catch (SQLException e) {
        LOGGER.warn("SQLException: couldn't close Statement");
        throw new PersistenceException(e.getMessage(), e);
      }
    }
    if (connection != null) {
      try {
        connection.close();
      } catch (SQLException e) {
        LOGGER.warn("SQLException: couldn't close Connection");
        throw new PersistenceException(e.getMessage(), e);
      }
    }
  }

  /**
  * Close elements if they are not null.
  * @param connection
  * @param statement
  * @param results
  */
  public static void close(Connection connection, Statement statement, ResultSet results) {
    if (results != null) {
      try {
        results.close();
      } catch (SQLException e) {
        LOGGER.warn("SQLException: couldn't close ResultSet");
        throw new PersistenceException(e.getMessage(), e);
      }
    }
    close(connection, statement);
  }

  /**
   * Execute rollback on connection if not null.
   * @param connection
   */
  public static void rollback(Connection connection) {
    if (connection != null) {
      try {
        connection.rollback();
      } catch (SQLException e) {
        LOGGER.warn("SQLException: couldn't rollback the Connection");
        throw new PersistenceException(e.getMessage(), e);
      }
    }
  }
}