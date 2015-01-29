package com.excilys.computerdatabase.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.dto.ComputerDto;
import com.excilys.computerdatabase.dto.ComputerDtoConverter;
import com.excilys.computerdatabase.exception.PersistenceException;
import com.excilys.computerdatabase.persistence.ConnectionManager;
import com.excilys.computerdatabase.persistence.IComputerDao;
import com.excilys.computerdatabase.service.IComputerService;

/**
* Standard Service implementation to manage Computer objects.
*
* @author Jeremy SCARELLA
*/
@Service
public class ComputerServiceJDBC implements IComputerService {

  /*
   * Instance of ConnectionManager
   */
  @Autowired
  private ConnectionManager   connectionManager;

  /*
  * Instance of the IComputerDao
  */
  @Autowired
  private IComputerDao        computerDao;

  /*
   * LOGGER
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(ComputerServiceJDBC.class);

  /**
   * Get the computer in the database corresponding to the id in parameter.
   * @param id : id of the computer in the database.
   * @return The computer that was found or null if there is no computer for this id.
   */
  public ComputerDto getById(final Long id) {
    Computer computer = null;
    try {
      computer = computerDao.getById(id);
    } catch (final PersistenceException e) {
      LOGGER.warn("PersistenceException: during getById()", e);
      LOGGER.debug("ComputerServiceJDBC - GET BY ID FAIL: " + ComputerDtoConverter.toDto(computer));
      return null;
    } finally {
      connectionManager.closeConnection();
    }
    LOGGER
        .debug("ComputerServiceJDBC - GET BY ID SUCCESS: " + ComputerDtoConverter.toDto(computer));
    return ComputerDtoConverter.toDto(computer);
  }

  /**
   * Get the List of all the computers in the database.
   * @return List of all the computers in the database.
   */
  public List<ComputerDto> getAll() {
    List<Computer> computers = null;
    try {
      computers = computerDao.getAll();
    } catch (final PersistenceException e) {
      LOGGER.warn("PersistenceException: during getAll()", e);
      LOGGER.debug("ComputerServiceJDBC - GET ALL FAIL");
      return null;
    } finally {
      connectionManager.closeConnection();
    }
    LOGGER.debug("ComputerServiceJDBC - GET ALL SUCCESS");
    return ComputerDtoConverter.toDto(computers);
  }

  /**
   * Add a computer in the database using a Computer instance.
   * @param computer : instance of the computer that needs to be added to the database. Must have a name at least. 
   * @return An instance of the computer that was added to the database or null if the INSERT did not work.
   */
  @Transactional
  public ComputerDto addByComputer(final Computer computer) {
    Computer newComputer = null;
    try {
      newComputer = computerDao.addByComputer(computer);
    } catch (final PersistenceException e) {
      LOGGER.warn("PersistenceException: during addByComputer()", e);
      LOGGER.debug("ComputerServiceJDBC - ADD BY COMPUTER FAIL: "
          + ComputerDtoConverter.toDto(newComputer));
      return null;
    } finally {
      connectionManager.closeConnection();
    }
    LOGGER.debug("ComputerServiceJDBC - ADD BY COMPUTER SUCCESS: "
        + ComputerDtoConverter.toDto(newComputer));
    return ComputerDtoConverter.toDto(newComputer);
  }

  /**
   * Update a computer in the database using a Computer instance.
   * @param computer : instance of the computer that needs to be added to the database. Must have an id at least. 
   * @return An instance of the computer that was updated in the database or null if the UPDATE did not work.
   */
  @Transactional
  public ComputerDto updateByComputer(final Computer computer) {
    Computer newComputer = null;
    try {
      newComputer = computerDao.updateByComputer(computer);
    } catch (final PersistenceException e) {
      LOGGER.warn("PersistenceException: during updateByComputer()", e);
      LOGGER.debug("ComputerServiceJDBC - UPDATE BY COMPUTER FAIL: "
          + ComputerDtoConverter.toDto(newComputer));
      return null;
    } finally {
      connectionManager.closeConnection();
    }
    LOGGER.debug("ComputerServiceJDBC - UPDATE BY COMPUTER SUCCESS: "
        + ComputerDtoConverter.toDto(newComputer));
    return ComputerDtoConverter.toDto(newComputer);
  }

  /**
   * Remove a computer from the database using its id.
   * @param id : id of the computer to remove.
   * @return True if the computer was removed from the database, false otherwise.
   */
  @Transactional
  public Boolean removeById(final Long id) {
    try {
      computerDao.removeById(id);
    } catch (final PersistenceException e) {
      LOGGER.warn("PersistenceException: during removeById()", e);
      LOGGER.debug("ComputerServiceJDBC - REMOVE BY ID FAIL");
      return false;
    } finally {
      connectionManager.closeConnection();
    }
    LOGGER.debug("ComputerServiceJDBC - REMOVE BY ID SUCCESS");
    return true;
  }

  /**
   * Remove a list of computers from the database using their ids.
   * @param idList : the list of ids of the computers to remove.
   */
  @Transactional
  public void removeByIdList(final List<Long> idList) {
    try {
      computerDao.removeByIdList(idList);
    } catch (final PersistenceException e) {
      LOGGER.warn("PersistenceException: during removeByIdList()", e);
      LOGGER.debug("ComputerServiceJDBC - REMOVE BY ID LIST FAIL");
      return;
    } finally {
      connectionManager.closeConnection();
    }
    LOGGER.debug("ComputerServiceJDBC - REMOVE BY ID LIST SUCCESS");
  }

  /**
   * Remove a computer from the database using a Computer instance.
   * @param computer : instance of the computer that needs to be removed from the database. Must have an id at least. 
   * @return An instance of the computer that was removed from the database or null if the DELETE did not work.
   */
  @Transactional
  public ComputerDto removeByComputer(final Computer computer) {
    Computer newComputer = null;
    try {
      newComputer = computerDao.removeByComputer(computer);
    } catch (final PersistenceException e) {
      LOGGER.warn("PersistenceException: during removeByComputer()", e);
      LOGGER.debug("ComputerServiceJDBC - REMOVE BY COMPUTER FAIL: "
          + ComputerDtoConverter.toDto(newComputer));
      return null;
    } finally {
      connectionManager.closeConnection();
    }
    LOGGER.debug("ComputerServiceJDBC - REMOVE BY COMPUTER SUCCESS: "
        + ComputerDtoConverter.toDto(newComputer));
    return ComputerDtoConverter.toDto(newComputer);
  }

  /**
   * Get a Page of computers in the database.
   * @param page : a page containing the pageIndex and the max number of elements the page can have
   * @return A Page instance containing a sublist of computers
   */
  public Page<ComputerDto> getPagedList(final Page<ComputerDto> page) {
    Page<ComputerDto> newPage = null;
    try {
      newPage = computerDao.getPagedList(page);
    } catch (final PersistenceException e) {
      LOGGER.warn("PersistenceException: during getPagedList()", e);
      LOGGER.debug("ComputerServiceJDBC - GET PAGED LIST FAIL: " + newPage);
      return null;
    } finally {
      connectionManager.closeConnection();
    }
    LOGGER.debug("ComputerServiceJDBC - GET PAGED LIST SUCCESS: " + newPage);
    return newPage;
  }
}
