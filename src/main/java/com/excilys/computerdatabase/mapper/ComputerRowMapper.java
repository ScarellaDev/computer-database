package com.excilys.computerdatabase.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.exception.PersistenceException;

/**
* IRowMapper implementation for Computer objects
* 
* @author Jeremy SCARELLA
*/
public class ComputerRowMapper implements RowMapper<Computer> {

  /*
   * LOGGER
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(ComputerRowMapper.class);

  /**
   * Returns a Computer based on a ResultSet and a row id
   * @param results : the ResulSet containing the rows to map
   * @param rowId : the id of the row to map
   * @return a Computer based on the ResultSet
   */
  @Override
  public Computer mapRow(final ResultSet results, final int rowId) throws SQLException {
    if (results == null) {
      return null;
    }
    try {
      final Computer.Builder builder = Computer.builder().id(results.getLong("id"))
          .name(results.getString("name"));
      final Timestamp introduced = results.getTimestamp("introduced");
      final Timestamp discontinued = results.getTimestamp("discontinued");
      if (introduced != null) {
        builder.introduced(introduced.toLocalDateTime());
      }
      if (discontinued != null) {
        builder.discontinued(discontinued.toLocalDateTime());
      }
      final Long companyId = results.getLong("company_Id");
      if (companyId != null) {
        builder.company(Company.builder().id(companyId).name(results.getString("company_name"))
            .build());
      }

      return builder.build();
    } catch (final SQLException e) {
      LOGGER.error("SQLException while mapping a computer");
      throw new PersistenceException(e.getMessage(), e);
    }
  }
}