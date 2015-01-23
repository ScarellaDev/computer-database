package com.excilys.computerdatabase.mapper.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.exception.PersistenceException;
import com.excilys.computerdatabase.mapper.IRowMapper;

/**
* IRowMapper implementation for Company objects
* 
* @author Jeremy SCARELLA
*/
public class CompanyRowMapperImpl implements IRowMapper<Company> {

  /*
   * LOGGER
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(CompanyRowMapperImpl.class);

  /**
   * Returns a Company based on a row of ResultSet
   * @param results : the row of ResulSet to map
   * @return a Company based on the ResultSet
   */
  @Override
  public Company mapRow(final ResultSet results) {
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

  /**
   * Returns a Company List based on rows of ResultSet
   * @param results : the rows of ResulSet to map
   * @return a Company List based on the ResultSet
   */
  @Override
  public List<Company> mapRows(final ResultSet results) {
    if (results == null) {
      return null;
    }
    try {
      final List<Company> companies = new ArrayList<Company>();
      while (results.next()) {
        companies.add(mapRow(results));
      }
      return companies;
    } catch (final SQLException e) {
      LOGGER.error("SQLException while mapping a list of companies");
      throw new PersistenceException(e.getMessage(), e);
    }
  }

}
