package com.excilys.computerdatabase.mapper.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.exception.PersistenceException;
import com.excilys.computerdatabase.mapper.IRowMapper;

/**
* IRowMapper implementation for Computer objects
* 
* @author Jeremy SCARELLA
*/
public class ComputerRowMapper implements IRowMapper<Computer> {

  /*
   * LOGGER
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(ComputerRowMapper.class);

  /**
   * Returns a Computer based on a row of ResultSet
   * @param results : the row of ResulSet to map
   * @return a Computer based on the ResultSet
   */
  @Override
  public Computer mapRow(final ResultSet results) {
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

  /**
   * Returns a Computer List based on rows of ResultSet
   * @param results : the rows of ResulSet to map
   * @return a Computer List based on the ResultSet
   */
  @Override
  public List<Computer> mapRows(final ResultSet results) {
    if (results == null) {
      return null;
    }
    try {
      final List<Computer> computeresults = new ArrayList<Computer>();
      while (results.next()) {
        computeresults.add(mapRow(results));
      }
      return computeresults;
    } catch (final SQLException e) {
      LOGGER.error("SQLException while mapping a list of computers");
      throw new PersistenceException(e.getMessage(), e);
    }
  }
}