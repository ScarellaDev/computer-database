package com.excilys.computerdatabase.test.persistence.mock;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerdatabase.test.exception.PersistenceExceptionTest;

/**
* Helper class to create database connections.
*
* @author Jeremy SCARELLA
*/
public class UtilDaoSQLMock {
  // ========== Database ==========
  /**
  * URL to the database server.
  */
  private static final String DB_URL       = "jdbc:mysql://localhost:3306/computer-database-test-db?zeroDateTimeBehavior=convertToNull";
  /**
  * User name for the database.
  */
  private static final String DB_USR       = "admincdbtest";
  /**
  * User password for the database.
  */
  private static final String DB_PW        = "qwerty1234";

  /**
  * Base Query for all the Select queries
  */
  public static final String  SELECT_QUERY = "SELECT c.id, c.name, c.introduced, c.discontinued, company_id, company.name as company FROM computer c LEFT JOIN company ON company.id=c.company_id";

  private static Logger       logger       = LoggerFactory
                                               .getLogger("com.excilys.computerdatabase.test.persistence.UtilDaoSQL");

  /**
   * Static block that loads the JDBC driver once
   */
  static {
    try {
      // Load the driver for mysql database
      Class.forName("com.mysql.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      logger.error("SQLError during jdbc.Driver loading");
      throw new PersistenceExceptionTest(e);
    }
  }

  /**
  * Retrieve a connection to the database.
  *
  * @return The {@link Connection} instance.
  * @throws SQLException
  * if a database access error occurs
  */
  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(DB_URL, DB_USR, DB_PW);
  }

  /**
  * Close elements if they are not null.
  *
  * @param results
  * @param statement
  * @param connection
  */
  public static void close(Connection connection, Statement statement, ResultSet results) {
    if (results != null) {
      try {
        results.close();
      } catch (SQLException e) {
        logger.error("SQLError for results.close() in UtilDaoSQL.java");
        throw new PersistenceExceptionTest(e);
      }
    }
    close(connection, statement);
  }

  /**
  * Close elements if they are not null.
  *
  * @param statement
  * @param connection
  */
  public static void close(Connection connection, Statement statement) {
    if (statement != null) {
      try {
        statement.close();
      } catch (SQLException e) {
        logger.error("SQLError during statement.close() in UtilDaoSQL.java");
        throw new PersistenceExceptionTest(e);
      }
    }
    if (connection != null) {
      try {
        connection.close();
      } catch (SQLException e) {
        logger.error("SQLError during connection.close() in UtilDaoSQL.java");
        throw new PersistenceExceptionTest(e);
      }
    }
  }
}
