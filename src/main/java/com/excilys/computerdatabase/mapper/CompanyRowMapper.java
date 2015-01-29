package com.excilys.computerdatabase.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.exception.PersistenceException;

/**
* IRowMapper implementation for Company objects
* 
* @author Jeremy SCARELLA
*/
public class CompanyRowMapper implements RowMapper<Company> {

  /*
   * LOGGER
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(CompanyRowMapper.class);

  /**
   * Returns a Company based on a ResultSet and a row id
   * @param results : the ResulSet containing the rows to map
   * @param rowId : the id of the row to map
   * @return a Company based on the ResultSet
   */
  @Override
  public Company mapRow(final ResultSet results, final int rowId) throws SQLException {
    if (results == null) {
      return null;
    }
    try {
      return Company.builder().id(results.getLong("id")).name(results.getString("name")).build();
    } catch (final SQLException e) {
      LOGGER.error("SQLException while mapping a company");
      throw new PersistenceException(e.getMessage(), e);
    }
  }
}
