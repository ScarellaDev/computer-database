package com.excilys.computerdatabase.persistence.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerdatabase.exception.PersistenceException;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

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
   * SELECT query for computer table
   */
  public static final String  COMPUTER_SELECT_QUERY               = "SELECT c.id, c.name, c.introduced, c.discontinued, company_id, company.name as company FROM computer c LEFT JOIN company ON company.id=c.company_id";

  /*
   * INSERT query for computer table
   */
  public static final String  COMPUTER_INSERT_QUERY               = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?,?,?,?)";

  /*
   * UPDATE query for computer table
   */
  public static final String  COMPUTER_UPDATE_QUERY               = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id =? WHERE id = ?";

  /*
   * DELETE query for computer table
   */
  public static final String  COMPUTER_DELETE_QUERY               = "DELETE computer FROM computer WHERE id = ?";

  /*
   * DELETE query for computer table, delete all computers with company.id = ?
   */
  public static final String  COMPUTER_DELETE_WHERE_COMPANY_QUERY = "DELETE c FROM computer c LEFT JOIN company ON company.id=c.company_id WHERE company.id = ?";

  /*
   * COUNT query for computer table
   */
  public static final String  COMPUTER_COUNT_QUERY                = "SELECT COUNT(c.id) AS total FROM computer c LEFT JOIN company ON company.id=c.company_id";

  /*
   * MAX query for computer table
   */
  public static final String  COMPUTER_MAX_QUERY                  = "SELECT MAX(id) AS id FROM computer";

  /*
   * SELECT query for company table
   */
  public static final String  COMPANY_SELECT_QUERY                = "SELECT * FROM company";

  /*
   * DELETE query for company table
   */
  public static final String  COMPANY_DELETE_QUERY                = "DELETE company FROM company WHERE id = ?";

  /*
   * COUNT query for company table
   */
  public static final String  COMPANY_COUNT_QUERY                 = "SELECT COUNT(id) AS total FROM company";

  /*
   * LOGGER
   */
  private static final Logger LOGGER                              = LoggerFactory
                                                                      .getLogger(UtilDaoSQL.class);

  /*
   * CONNECTION_POOL
   */
  private static final BoneCP CONNECTION_POOL;

  /**
   * Static instruction block that loads the JDBC driver once
   */
  static {
    Properties properties = new Properties();
    InputStream input = null;
    try {
      input = UtilDaoSQL.class.getClassLoader().getResourceAsStream("db.properties");
      properties.load(input);

      final BoneCPConfig config = new BoneCPConfig();
      // Load the Driver class
      Class.forName(properties.getProperty("db.driver"));
      config.setJdbcUrl(properties.getProperty("db.url"));
      config.setUser(properties.getProperty("db.username"));
      config.setPassword(properties.getProperty("db.password"));
      config.setMinConnectionsPerPartition(5);
      config.setMaxConnectionsPerPartition(10);
      config.setPartitionCount(1);
      CONNECTION_POOL = new BoneCP(config);
    } catch (SQLException e) {
      LOGGER.error("SQLException: error while creating the connection pool");
      throw new PersistenceException(e.getMessage(), e);
    } catch (IOException e) {
      LOGGER.error("IOException: couldn't load the database.properties file");
      throw new PersistenceException(e.getMessage(), e);
    } catch (ClassNotFoundException e) {
      LOGGER.error("ClassNotFoundException: MySQL JDBC driver not found");
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
  }

  /**
  * Retrieve a SQL connection to the database.
  * @return The {@link Connection} instance.
  * @throws SQLException : if a database access error occurs.
  */
  public static Connection getConnection() {
    try {
      return CONNECTION_POOL.getConnection();
    } catch (SQLException e) {
      LOGGER.error("SQLException: couldn't connect to the database");
      throw new PersistenceException(e.getMessage(), e);
    }
  }

  /**
  * Retrieve a SQL connection to the database with setAutoCommit(false).
  * @return The {@link Connection} instance.
  * @throws SQLException : if a database access error occurs.
  */
  public static Connection getConnectionWithManualCommit() {
    try {
      Connection connection = CONNECTION_POOL.getConnection();
      connection.setAutoCommit(false);
      return connection;
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
        if (connection.getAutoCommit()) {
          connection.setAutoCommit(true);
        }
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
   * Execute commit on connection if not null.
   * @param connection
   */
  public static void commit(Connection connection) {
    if (connection != null) {
      try {
        connection.commit();
      } catch (SQLException e) {
        LOGGER.warn("SQLException: couldn't commit the Connection");
        throw new PersistenceException(e.getMessage(), e);
      }
    }
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