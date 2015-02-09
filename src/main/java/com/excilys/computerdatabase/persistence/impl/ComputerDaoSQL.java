package com.excilys.computerdatabase.persistence.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.dto.ComputerDto;
import com.excilys.computerdatabase.dto.ComputerDtoConverter;
import com.excilys.computerdatabase.exception.PersistenceException;
import com.excilys.computerdatabase.mapper.ComputerRowMapper;
import com.excilys.computerdatabase.persistence.IComputerDao;
import com.excilys.computerdatabase.validator.StringValidator;

/**
* Data Access Object for Computer, SQL implementation.
* 
* @author Jeremy SCARELLA
*/
@Repository
public class ComputerDaoSQL implements IComputerDao {
  /*
   * LOGGER
   */
  private static final Logger LOGGER         = LoggerFactory.getLogger(ComputerDaoSQL.class);

  /*
   * Instance of JdbcTemplate
   */
  private JdbcTemplate        jdbcTemplate;

  /*
   * Instance of ComputerRowMapperImpl
   */
  private RowMapper<Computer> computerMapper = new ComputerRowMapper();

  /*
   * Link jdbcTemplate to DataSource
   */
  @Autowired
  public void setDataSource(final DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  /**
   * Get the computer in the database corresponding to the id in parameter.
   * @param id : id of the computer in the database.
   * @return The computer that was found or null if there is no computer for this id.
   */
  public Computer getById(final Long id) {
    if (id == null) {
      return null;
    }

    List<Computer> computers = null;
    try {
      computers = jdbcTemplate.query(UtilDaoSQL.COMPUTER_SELECT_WITH_ID_QUERY, new Long[] { id },
          computerMapper);
    } catch (final DataAccessException e) {
      LOGGER.error("DataAccessException in getById() with id = " + id);
      throw new PersistenceException(e.getMessage(), e);
    }

    if (computers == null) {
      return null;
    } else {
      if (computers.size() == 1) {
        return computers.get(0);
      } else if (computers.size() == 0) {
        return null;
      } else {
        LOGGER.error("There was more than 1 computer with id={} in the database", id);
        throw new PersistenceException("There was more than 1 computer with id=" + id
            + " in the database");
      }
    }
  }

  /**
   * Get the List of all the computers in the database.
   * @return List of all the computers in the database.
   */
  public List<Computer> getAll() {
    try {
      return jdbcTemplate.query(UtilDaoSQL.COMPUTER_SELECT_QUERY, computerMapper);
    } catch (final DataAccessException e) {
      LOGGER.error("DataAccessException in getAll()");
      throw new PersistenceException(e.getMessage(), e);
    }
  }

  /**
   * Add a computer in the database using a Computer instance.
   * @param computer : instance of the computer that needs to be added to the database. Must have a name at least. 
   * @return An instance of the computer that was added to the database or null if the INSERT did not work.
   */
  public Computer addByComputer(final Computer computer) {
    if (computer == null || StringValidator.isEmpty(computer.getName())) {
      return null;
    }

    Timestamp introducedT = null;
    final LocalDateTime introduced = computer.getIntroduced();
    Timestamp discontinuedT = null;
    final LocalDateTime discontinued = computer.getDiscontinued();
    Long companyId = null;
    final Company company = computer.getCompany();

    if (introduced != null) {
      introducedT = Timestamp.valueOf(introduced);
    }
    if (discontinued != null) {
      discontinuedT = Timestamp.valueOf(discontinued);
    }
    if (company != null) {
      companyId = company.getId();
    }

    final Object[] args = new Object[] { computer.getName(), introducedT, discontinuedT, companyId };

    try {
      jdbcTemplate.update(UtilDaoSQL.COMPUTER_INSERT_QUERY, args);
      return computer;
    } catch (final DataAccessException e) {
      LOGGER.error("DataAccessException in addByComputer() with computer = " + computer);
      throw new PersistenceException(e.getMessage(), e);
    }
  }

  /**
   * Update a computer in the database using a Computer instance.
   * @param computer : instance of the computer that needs to be added to the database. Must have an id at least. 
   * @return An instance of the computer that was updated in the database or null if the UPDATE did not work.
   */
  public Computer updateByComputer(final Computer computer) {
    if (computer == null || computer.getId() < 0 || StringValidator.isEmpty(computer.getName())) {
      return null;
    }

    Timestamp introducedT = null;
    final LocalDateTime introduced = computer.getIntroduced();
    Timestamp discontinuedT = null;
    final LocalDateTime discontinued = computer.getDiscontinued();
    Long companyId = null;
    final Company company = computer.getCompany();

    if (introduced != null) {
      introducedT = Timestamp.valueOf(introduced);
    }
    if (discontinued != null) {
      discontinuedT = Timestamp.valueOf(discontinued);
    }
    if (company != null) {
      companyId = company.getId();
    }

    final Object[] args = new Object[] { computer.getName(), introducedT, discontinuedT, companyId,
        computer.getId() };

    try {
      jdbcTemplate.update(UtilDaoSQL.COMPUTER_UPDATE_QUERY, args);
      return computer;
    } catch (final DataAccessException e) {
      LOGGER.error("DataAccessException in updateByComputer() with computer = " + computer);
      throw new PersistenceException(e.getMessage(), e);
    }
  }

  /**
   * Remove a computer from the database using its id.
   * @param id : id of the computer to remove.
   * @return True if the computer was removed from the database, false otherwise.
   */
  public Boolean removeById(final Long id) {
    if (id == null) {
      return false;
    }

    try {
      jdbcTemplate.update(UtilDaoSQL.COMPUTER_DELETE_QUERY, id);
      return true;
    } catch (final DataAccessException e) {
      LOGGER.error("DataAccessException in removeById() with id = " + id);
      throw new PersistenceException(e.getMessage(), e);
    }
  }

  /**
   * Remove all computers attached to the companyId given as parameter from the database.
   * @param id : id of the company that needs its computers to be removed.
   */
  public void removeByCompanyId(final Long id) {
    if (id == null) {
      return;
    }

    try {
      jdbcTemplate.update(UtilDaoSQL.COMPUTER_DELETE_WHERE_COMPANY_QUERY, id);
    } catch (final DataAccessException e) {
      LOGGER.error("DataAccessException in removeByCompanyId() with id = " + id);
      throw new PersistenceException(e.getMessage(), e);
    }
  }

  /**
   * Remove a list of computers from the database using their ids.
   * @param idList : the list of ids of the computers to remove.
   * @return true if the transaction was a success, false otherwise
   */
  public void removeByIdList(final List<Long> idList) {
    if (idList == null || idList.isEmpty()) {
      return;
    }

    final List<Object[]> batchList = idList.stream().map(l -> new Object[] { l })
        .collect(Collectors.toList());

    try {
      jdbcTemplate.batchUpdate(UtilDaoSQL.COMPUTER_DELETE_QUERY, batchList);
    } catch (final DataAccessException e) {
      LOGGER.error("DataAccessException in removeByIdList()");
      throw new PersistenceException(e.getMessage(), e);
    }
  }

  /**
   * Remove a computer from the database using a Computer instance.
   * @param computer : instance of the computer that needs to be removed from the database. Must have an id at least. 
   * @return An instance of the computer that was removed from the database or null if the DELETE did not work.
   */
  public Computer removeByComputer(final Computer computer) {
    if (computer == null || computer.getId() < 0) {
      return null;
    }

    try {
      jdbcTemplate.update(UtilDaoSQL.COMPUTER_DELETE_QUERY, computer.getId());
      return computer;
    } catch (final DataAccessException e) {
      LOGGER.error("DataAccessException in removeByComputer() with computer = " + computer);
      throw new PersistenceException(e.getMessage(), e);
    }
  }

  /**
   * Get a Page of computers in the database.
   * @param page : a page containing the pageIndex and the max number of elements the page can have
   * @return A Page instance containing a sublist of computers
   */
  @Override
  public Page<ComputerDto> getPagedList(final Page<ComputerDto> page) {
    if (page == null) {
      return null;
    }

    final String search = page.getSearch() + "%";
    try {
      //Create & execute the counting query
      if (StringValidator.isEmpty(page.getSearch())) {
        page.setTotalNbElements(jdbcTemplate.queryForObject(UtilDaoSQL.COMPUTER_COUNT_QUERY,
            Integer.class));
      } else {
        page.setTotalNbElements(jdbcTemplate.queryForObject(UtilDaoSQL.COMPUTER_COUNT_QUERY
            + " WHERE c.name LIKE ? OR company.name LIKE ?;", new String[] { search, search },
            Integer.class));
      }
    } catch (final DataAccessException e) {
      LOGGER.error("DataAccessException in getPagedList() during countQuery with page = " + page);
      throw new PersistenceException(e.getMessage(), e);
    }

    page.refreshNbPages();

    final StringBuilder selectQuery = new StringBuilder(UtilDaoSQL.COMPUTER_SELECT_QUERY);
    List<Computer> computers = null;
    try {
      //Create & execute the selecting query
      if (StringValidator.isEmpty(page.getSearch())) {
        selectQuery.append(" ORDER BY ").append(Page.getColumnNames()[page.getSort()]).append(" ")
            .append(page.getOrder()).append(" LIMIT ? OFFSET ?;");

        final Object[] args = new Object[] { page.getNbElementsPerPage(),
            (page.getPageIndex() - 1) * page.getNbElementsPerPage() };

        computers = jdbcTemplate.query(selectQuery.toString(), args, computerMapper);
      } else {
        selectQuery.append(" WHERE c.name LIKE ? OR company.name LIKE ? ORDER BY ")
            .append(Page.getColumnNames()[page.getSort()]).append(" ").append(page.getOrder())
            .append(" LIMIT ? OFFSET ?;");

        final Object[] args = new Object[] { search, search, page.getNbElementsPerPage(),
            (page.getPageIndex() - 1) * page.getNbElementsPerPage() };

        computers = jdbcTemplate.query(selectQuery.toString(), args, computerMapper);
      }

      if (computers != null) {
        page.setList(ComputerDtoConverter.toDto(computers));
      }
      return page;
    } catch (final DataAccessException e) {
      LOGGER.error("DataAccessException in getPagedList() during countQuery with page = " + page);
      throw new PersistenceException(e.getMessage(), e);
    }
  }
}
