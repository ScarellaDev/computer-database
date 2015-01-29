package com.excilys.computerdatabase.persistence.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.exception.PersistenceException;
import com.excilys.computerdatabase.mapper.CompanyRowMapper;
import com.excilys.computerdatabase.persistence.ICompanyDao;

/**
* Data Access Object for Company, SQL implementation.
* 
* @author Jeremy SCARELLA
*/
@Repository
public class CompanyDaoSQL implements ICompanyDao {
  /*
   * Instance of JdbcTemplate
   */
  @Autowired
  private JdbcTemplate        jdbcTemplate;

  /*
   * Instance of CompanyRowMapperImpl
   */
  private RowMapper<Company>  companyMapper = new CompanyRowMapper();

  /*
   * LOGGER
   */
  private static final Logger LOGGER        = LoggerFactory.getLogger(CompanyDaoSQL.class);

  /**
   * Get the company in the database corresponding to the id in parameter.
   * @param id : id of the company in the database.
   * @return The company that was found or null if there is no company for this id.
   */
  public Company getById(final Long id) {
    if (id == null) {
      return null;
    }

    List<Company> companies = null;
    try {
      companies = jdbcTemplate.query(UtilDaoSQL.COMPANY_SELECT_WITH_ID_QUERY, new Long[] { id },
          companyMapper);
    } catch (final DataAccessException e) {
      LOGGER.error("DataAccessException in getById() with id = " + id);
      throw new PersistenceException(e.getMessage(), e);
    }

    if (companies == null) {
      return null;
    } else {
      if (companies.size() == 1) {
        return companies.get(0);
      } else if (companies.size() == 0) {
        return null;
      } else {
        LOGGER.error("There was more than 1 company with id={} in the database", id);
        throw new PersistenceException("There was more than 1 company with id=" + id
            + " in the database");
      }
    }
  }

  /**
   * Get the List of all the companies in the database.
   * @return List of all the companies in the database.
   */
  public List<Company> getAll() {
    try {
      return jdbcTemplate.query(UtilDaoSQL.COMPANY_SELECT_QUERY, companyMapper);
    } catch (final DataAccessException e) {
      LOGGER.error("DataAccessException in getAll()");
      throw new PersistenceException(e.getMessage(), e);
    }
  }

  /**
   * Remove a company from the database using its id.
   * @param connection : the shared Connection for the CompanyServiceJDBC.removeById().
   * @param id : id of the company to remove.
   */
  public void removeById(final Long id) {
    if (id == null) {
      return;
    }

    try {
      jdbcTemplate.update(UtilDaoSQL.COMPANY_DELETE_QUERY, id);
    } catch (final DataAccessException e) {
      LOGGER.error("DataAccessException in removeById() with id = " + id);
      throw new PersistenceException(e.getMessage(), e);
    }
  }

  /**
   * Get a Page of companies in the database.
   * @param page : a page containing the pageIndex and the max number of elements the page can have
   * @return A Page instance containing a sublist of companies
   */
  @Override
  public Page<Company> getPagedList(final Page<Company> page) {
    if (page == null) {
      return null;
    }

    try {
      page.setTotalNbElements(jdbcTemplate.queryForObject(UtilDaoSQL.COMPANY_COUNT_QUERY,
          Integer.class));
      page.refreshNbPages();

      final Integer limit = page.getNbElementsPerPage();
      final Integer offset = (page.getPageIndex() - 1) * page.getNbElementsPerPage();
      page.setList(jdbcTemplate.query(UtilDaoSQL.COMPANY_LIMITED_SELECT_QUERY, new Integer[] {
          limit, offset }, companyMapper));
      return page;
    } catch (final DataAccessException e) {
      LOGGER.error("DataAccessException in getPagedList() with page = " + page);
      throw new PersistenceException(e.getMessage(), e);
    }
  }
}