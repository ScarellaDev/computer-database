package com.excilys.computerdatabase.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerdatabase.exception.PersistenceException;
import com.excilys.computerdatabase.persistence.ConnectionManager;

/**
* Class managing the loading of the properties files
*
* @author Jeremy SCARELLA
*/
public class AppSettings {

  /*
   * DB SETTINGS KEYS
   */
  private static final String PROPERTIES_FILE = "db.properties";
  private static final String KEY_DB_DRIVER   = "db.driver";
  private static final String KEY_DB_URL      = "db.url";
  private static final String KEY_DB_USERNAME = "db.username";
  private static final String KEY_DB_PASSWORD = "db.password";

  /*
   * DB SETTINGS
   */
  public static String        DB_DRIVER;
  public static String        DB_URL;
  public static String        DB_USERNAME;
  public static String        DB_PASSWORD;

  private static final Logger LOGGER          = LoggerFactory.getLogger(AppSettings.class);

  static {
    readProperties();
  }

  // AppSettings Should never be instanciated
  private AppSettings() {}

  /**
   * Read the application configuration file and set the application 
   * parameters according to its content.
   */
  private static void readProperties() {

    Properties properties = new Properties();
    InputStream input = null;
    try {
      input = ConnectionManager.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
      properties.load(input);

      DB_DRIVER = properties.getProperty(KEY_DB_DRIVER);
      DB_URL = properties.getProperty(KEY_DB_URL);
      DB_USERNAME = properties.getProperty(KEY_DB_USERNAME);
      DB_PASSWORD = properties.getProperty(KEY_DB_PASSWORD);

      if (input != null) {
        input.close();
      }
    } catch (IOException e) {
      LOGGER.error("IOException: couldn't close InputStream during db.properties file loading");
      throw new PersistenceException(e.getMessage(), e);
    }
  }
}
