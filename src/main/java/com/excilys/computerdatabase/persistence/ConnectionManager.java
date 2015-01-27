package com.excilys.computerdatabase.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerdatabase.exception.PersistenceException;
import com.excilys.computerdatabase.util.AppSettings;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

/**
* Util class to create SQL database connections.
*
* @author Jeremy SCARELLA
*/
public enum ConnectionManager {

  /*
   * Instance of ConnectionManager
   */
  INSTANCE;

  /*
   * ConnectionPool
   */
  private BoneCP                  connectionPool        = null;

  /*
   * ThreadLocal<Connection>
   */
  private ThreadLocal<Connection> threadLocalConnection = null;

  /*
   * LOGGER
   */
  private static final Logger     LOGGER                = LoggerFactory
                                                            .getLogger(ConnectionManager.class);

  /**
  * Constructor. Load the MySQL JDBC Driver, sets the Connection Pool
  */
  private ConnectionManager() {

    try {
      // Load the Driver class
      Class.forName(AppSettings.DB_DRIVER);
    } catch (ClassNotFoundException e) {
      throw new PersistenceException("ClassNotFoundException: MySQL JDBC driver not found", e);
    }

    final BoneCPConfig config = new BoneCPConfig();
    config.setJdbcUrl(AppSettings.DB_URL);
    config.setUser(AppSettings.DB_USERNAME);
    config.setPassword(AppSettings.DB_PASSWORD);
    config.setMinConnectionsPerPartition(3);
    config.setMaxConnectionsPerPartition(10);
    config.setPartitionCount(2);

    try {
      connectionPool = new BoneCP(config);
    } catch (SQLException e) {
      throw new PersistenceException("SQLException: error while creating the connection pool", e);
    }

    threadLocalConnection = new ThreadLocal<Connection>();
  }

  /**
  * Retrieve a SQL connection to the database.
  * @throws SQLException : if a database access error occurs.
  * @return threadLocalConnection
  */
  public Connection getConnection() {
    if (threadLocalConnection.get() == null) {
      // No connection available for current Thread
      try {
        threadLocalConnection.set(connectionPool.getConnection());
      } catch (SQLException e) {
        LOGGER.warn("SQLException: couldn't get database connection", e);
        throw new PersistenceException(e.getMessage(), e);
      }
    }
    return threadLocalConnection.get();
  }

  /**
  * Retrieve a SQL connection to the database with setAutoCommit(false).
  * @throws SQLException : if a database access error occurs.
  */
  public void startTransaction() {
    try {
      Connection connection = getConnection();
      connection.setAutoCommit(false);
      connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
    } catch (SQLException e) {
      LOGGER.error("SQLException: couldn't start transaction");
      throw new PersistenceException(e.getMessage(), e);
    }
  }

  /**
   * Close Connection if it is not null.
   */
  public void closeConnection() {
    if (getConnection() != null) {
      try {
        getConnection().setAutoCommit(true);
        getConnection().close();
        threadLocalConnection.remove();
      } catch (SQLException e) {
        LOGGER.warn("SQLException: couldn't close Connection", e);
        throw new PersistenceException(e.getMessage(), e);
      }
    }
  }

  /**
   * Close Statement if it is not null.
   * @param statement
   */
  public void close(Statement statement) {
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
  public void close(PreparedStatement pStatement) {
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
  public void close(ResultSet results) {
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
   */
  public void commit() {
    if (getConnection() != null) {
      try {
        getConnection().commit();
      } catch (SQLException e) {
        LOGGER.warn("SQLException: couldn't commit the Connection");
        rollback();
      }
    }
  }

  /**
   * Execute rollback on connection if not null.
   */
  public void rollback() {
    if (getConnection() != null) {
      try {
        getConnection().rollback();
      } catch (SQLException e) {
        LOGGER.warn("SQLException: couldn't rollback the Connection");
        throw new PersistenceException(e.getMessage(), e);
      }
    }
  }
}