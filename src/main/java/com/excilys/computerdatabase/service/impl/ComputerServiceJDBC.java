package com.excilys.computerdatabase.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.dto.ComputerDto;
import com.excilys.computerdatabase.dto.ComputerDtoConverter;
import com.excilys.computerdatabase.exception.PersistenceException;
import com.excilys.computerdatabase.persistence.ConnectionManager;
import com.excilys.computerdatabase.persistence.IComputerDao;
import com.excilys.computerdatabase.persistence.impl.ComputerDaoSQL;
import com.excilys.computerdatabase.service.IComputerService;

/**
* Standard Service implementation to manage Computer objects.
*
* @author Jeremy SCARELLA
*/
public enum ComputerServiceJDBC implements IComputerService {

  /*
  * Instance of ComputerServiceImpl
  */
  INSTANCE;

  /*
   * CONNECTION_MANAGER
   */
  private static final ConnectionManager CM          = ConnectionManager.INSTANCE;

  /*
  * Instance of the IComputerDao
  */
  private IComputerDao                   computerDao = ComputerDaoSQL.INSTANCE;

  /*
   * LOGGER
   */
  private static final Logger            LOGGER      = LoggerFactory
                                                         .getLogger(ComputerServiceJDBC.class);

  /**
   * Get the computer in the database corresponding to the id in parameter.
   * @param id : id of the computer in the database.
   * @return The computer that was found or null if there is no computer for this id.
   */
  public ComputerDto getById(Long id) {
    Computer computer = null;
    try {
      computer = computerDao.getById(id);
    } catch (PersistenceException e) {
      LOGGER.warn("PersistenceException: during getById()", e);
    } finally {
      CM.closeConnection();
    }
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
    } catch (PersistenceException e) {
      LOGGER.warn("PersistenceException: during getAll()", e);
    } finally {
      CM.closeConnection();
    }
    return ComputerDtoConverter.toDto(computers);
  }

  /**
   * Add a computer in the database using a table of Strings as parameters.
   * @param params : String table composed of "name" (mandatory), "introduced" (date format: yyyy-MM-dd), discontinued (date format: yyyy-MM-dd), "companyId".
   * Use the String "null" to skip a value.
   * @return An instance of the computer that was added to the database or null if the INSERT did not work.
   */
  public ComputerDto addByString(String[] params) {
    Computer computer = null;
    CM.startTransaction();
    try {
      computer = computerDao.addByString(params);
      CM.commit();
    } catch (PersistenceException e) {
      CM.rollback();
      LOGGER.warn("PersistenceException: during removeById()", e);
    } finally {
      CM.closeConnection();
    }
    return ComputerDtoConverter.toDto(computer);
  }

  /**
   * Add a computer in the database using a Computer instance.
   * @param computer : instance of the computer that needs to be added to the database. Must have a name at least. 
   * @return An instance of the computer that was added to the database or null if the INSERT did not work.
   */
  public ComputerDto addByComputer(Computer computer) {
    Computer newComputer = null;
    CM.startTransaction();
    try {
      newComputer = computerDao.addByComputer(computer);
      CM.commit();
    } catch (PersistenceException e) {
      CM.rollback();
      LOGGER.warn("PersistenceException: during removeById()", e);
    } finally {
      CM.closeConnection();
    }
    return ComputerDtoConverter.toDto(newComputer);
  }

  /**
   * Update a computer in the database using a table of Strings as parameters.
   * @param params : String table composed of "id" (mandatory), "name", "introduced" (date format: yyyy-MM-dd), discontinued (date format: yyyy-MM-dd), "companyId".
   * All the attributes of the updated computer gets changed.
   * @return An instance of the computer that was updated in the database or null if the UPDATE did not work.
   */
  public ComputerDto updateByString(String[] params) {
    Computer computer = null;
    CM.startTransaction();
    try {
      computer = computerDao.updateByString(params);
      CM.commit();
    } catch (PersistenceException e) {
      CM.rollback();
      LOGGER.warn("PersistenceException: during removeById()", e);
    } finally {
      CM.closeConnection();
    }
    return ComputerDtoConverter.toDto(computer);
  }

  /**
   * Update a computer in the database using a Computer instance.
   * @param computer : instance of the computer that needs to be added to the database. Must have an id at least. 
   * @return An instance of the computer that was updated in the database or null if the UPDATE did not work.
   */
  public ComputerDto updateByComputer(Computer computer) {
    Computer newComputer = null;
    CM.startTransaction();
    try {
      newComputer = computerDao.updateByComputer(computer);
      CM.commit();
    } catch (PersistenceException e) {
      CM.rollback();
      LOGGER.warn("PersistenceException: during removeById()", e);
    } finally {
      CM.closeConnection();
    }
    return ComputerDtoConverter.toDto(newComputer);
  }

  /**
   * Remove a computer from the database using its id.
   * @param id : id of the computer to remove.
   * @return An instance of the computer that was removed from the database or null if the DELETE did not work.
   */
  public ComputerDto removeById(Long id) {
    Computer computer = null;
    CM.startTransaction();
    try {
      computer = computerDao.removeById(id);
      CM.commit();
    } catch (PersistenceException e) {
      CM.rollback();
      LOGGER.warn("PersistenceException: during removeById()", e);
    } finally {
      CM.closeConnection();
    }
    return ComputerDtoConverter.toDto(computer);
  }

  /**
   * Remove a list of computers from the database using their ids.
   * @param idList : the list of ids of the computers to remove.
   */
  public void removeByIdList(List<Long> idList) {
    CM.startTransaction();
    try {
      computerDao.removeByIdList(idList);
      CM.commit();
    } catch (PersistenceException e) {
      CM.rollback();
      LOGGER.warn("PersistenceException: during removeById()", e);
    } finally {
      CM.closeConnection();
    }
  }

  /**
   * Remove a computer from the database using a Computer instance.
   * @param computer : instance of the computer that needs to be removed from the database. Must have an id at least. 
   * @return An instance of the computer that was removed from the database or null if the DELETE did not work.
   */
  public ComputerDto removeByComputer(Computer computer) {
    Computer newComputer = null;
    CM.startTransaction();
    try {
      newComputer = computerDao.removeByComputer(computer);
      CM.commit();
    } catch (PersistenceException e) {
      CM.rollback();
      LOGGER.warn("PersistenceException: during removeById()", e);
    } finally {
      CM.closeConnection();
    }
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
    } catch (PersistenceException e) {
      LOGGER.warn("PersistenceException: during getPagedList()", e);
    } finally {
      CM.closeConnection();
    }
    return newPage;
  }
}
