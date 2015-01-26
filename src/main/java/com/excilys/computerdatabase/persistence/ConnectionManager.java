package com.excilys.computerdatabase.persistence;

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
public enum ConnectionManager {

  /*
   * Instance of ConnectionManager
   */
  INSTANCE;

  /*
   * ConnectionPool
   */
  private BoneCP                  connectionPool = null;

  /*
   * ThreadLocal<Connection>
   */
  private ThreadLocal<Connection> connection     = null;

  /*
   * logger
   */
  private Logger                  logger         = LoggerFactory.getLogger(ConnectionManager.class);

  /**
  * Constructor. Load the MySQL JDBC Driver
  */
  private ConnectionManager() {
    Properties properties = new Properties();
    InputStream input = null;
    try {
      input = ConnectionManager.class.getClassLoader().getResourceAsStream("db.properties");
      properties.load(input);

      // Load the Driver class
      Class.forName(properties.getProperty("db.driver"));

      final BoneCPConfig config = new BoneCPConfig();
      config.setJdbcUrl(properties.getProperty("db.url"));
      config.setUser(properties.getProperty("db.username"));
      config.setPassword(properties.getProperty("db.password"));
      config.setMinConnectionsPerPartition(5);
      config.setMaxConnectionsPerPartition(10);
      config.setPartitionCount(1);
      connectionPool = new BoneCP(config);
    } catch (SQLException e) {
      logger.error("SQLException: error while creating the connection pool");
      throw new PersistenceException(e.getMessage(), e);
    } catch (IOException e) {
      logger.error("IOException: couldn't load the database.properties file");
      throw new PersistenceException(e.getMessage(), e);
    } catch (ClassNotFoundException e) {
      logger.error("ClassNotFoundException: MySQL JDBC driver not found");
      throw new PersistenceException(e.getMessage(), e);
    } finally {
      try {
        if (input != null) {
          input.close();
        }
      } catch (IOException e) {
        logger.error("IOException: couldn't close InputStream during db.properties file loading");
        throw new PersistenceException(e.getMessage(), e);
      }
    }
  }

  /**
  * Retrieve a SQL connection to the database.
  * @return The {@link Connection} instance.
  * @throws SQLException : if a database access error occurs.
  */
  public Connection getConnection() {
    try {
      return connectionPool.getConnection();
    } catch (SQLException e) {
      logger.error("SQLException: couldn't connect to the database");
      throw new PersistenceException(e.getMessage(), e);
    }
  }

  /**
  * Retrieve a SQL connection to the database with setAutoCommit(false).
  * @throws SQLException : if a database access error occurs.
  */
  public void startTransactionConnection() {
    try {
      final Connection connection = connectionPool.getConnection();
      connection.setAutoCommit(false);
      this.connection = new ThreadLocal<Connection>();
      this.connection.set(connection);
    } catch (SQLException e) {
      logger.error("SQLException: couldn't connect to the database");
      throw new PersistenceException(e.getMessage(), e);
    }
  }

  /**
   * Get the Transaction Connection if not null
   */
  public Connection getTransactionConnection() {
    if (connection != null) {
      return connection.get();
    }
    return null;
  }

  /**
   * Close Transaction Connection if not null
   */
  public void closeTransactionConnection() {
    if (connection != null) {
      try {
        connection.get().close();
      } catch (final SQLException e) {
        logger.warn("SQLException: couldn't close the transaction Connection");
        throw new PersistenceException(e.getMessage(), e);
      }
    }
  }

  /**
   * Execute commit on transaction connection if not null.
   */
  public void commitTransactionConnection() {
    if (connection != null) {
      try {
        connection.get().commit();
      } catch (SQLException e) {
        logger.warn("SQLException: couldn't commit the transaction Connection");
        throw new PersistenceException(e.getMessage(), e);
      }
    }
  }

  /**
   * Execute rollback on transaction connection if not null.
   */
  public void rollbackTransactionConnection() {
    if (connection != null) {
      try {
        connection.get().rollback();
      } catch (SQLException e) {
        logger.warn("SQLException: couldn't rollback the transaction Connection");
        throw new PersistenceException(e.getMessage(), e);
      }
    }
  }

  /**
   * Close Connection if it is not null.
   * @param connection
   */
  public void close(Connection connection) {
    if (connection != null) {
      try {
        if (connection.getAutoCommit()) {
          connection.setAutoCommit(true);
        }
        connection.close();
      } catch (SQLException e) {
        logger.warn("SQLException: couldn't close Connection");
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
        logger.warn("SQLException: couldn't close Statement");
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
        logger.warn("SQLException: couldn't close PreparedStatement");
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
        logger.warn("SQLException: couldn't close ResultSet");
        throw new PersistenceException(e.getMessage(), e);
      }
    }
  }

  /**
   * Execute commit on connection if not null.
   * @param connection
   */
  public void commit(Connection connection) {
    if (connection != null) {
      try {
        connection.commit();
      } catch (SQLException e) {
        logger.warn("SQLException: couldn't commit the Connection");
        throw new PersistenceException(e.getMessage(), e);
      }
    }
  }

  /**
   * Execute rollback on connection if not null.
   * @param connection
   */
  public void rollback(Connection connection) {
    if (connection != null) {
      try {
        connection.rollback();
      } catch (SQLException e) {
        logger.warn("SQLException: couldn't rollback the Connection");
        throw new PersistenceException(e.getMessage(), e);
      }
    }
  }
}