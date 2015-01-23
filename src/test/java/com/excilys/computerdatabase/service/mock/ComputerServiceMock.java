package com.excilys.computerdatabase.service.mock;

import java.sql.Connection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.exception.PersistenceException;
import com.excilys.computerdatabase.persistence.IComputerDao;
import com.excilys.computerdatabase.persistence.impl.UtilDaoSQL;
import com.excilys.computerdatabase.service.IComputerDBService;

/**
* Mock standard Service implementation to manage Computer objects.
*
* @author Jeremy SCARELLA
*/
public class ComputerServiceMock implements IComputerDBService {

  /*
  * Instance of IComputerDao
  */
  private IComputerDao computerDao;

  /*
   * Instance of ComputerServiceMock
   */
  public ComputerServiceMock(IComputerDao computerDao) {
    this.computerDao = computerDao;
  }

  /*
   * LOGGER
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(ComputerServiceMock.class);

  /**
   * Get the computer in the database corresponding to the id in parameter.
   * @param id : id of the computer in the database.
   * @return The computer that was found or null if there is no computer for this id.
   */
  public Computer getById(Long id) {
    return computerDao.getById(id);
  }

  /**
   * Get the List of all the computers in the database.
   * @return List of all the computers in the database.
   */
  public List<Computer> getAll() {
    return computerDao.getAll();
  }

  /**
   * Add a computer in the database using a table of Strings as parameters.
   * @param params : String table composed of "name" (mandatory), "introduced" (date format: yyyy-MM-dd), discontinued (date format: yyyy-MM-dd), "companyId".
   * Use the String "null" to skip a value.
   * @return An instance of the computer that was added to the database or null if the INSERT did not work.
   */
  public Computer addByString(String[] params) {
    return computerDao.addByString(params);
  }

  /**
   * Add a computer in the database using a Computer instance.
   * @param computer : instance of the computer that needs to be added to the database. Must have a name at least. 
   * @return An instance of the computer that was added to the database or null if the INSERT did not work.
   */
  public Computer addByComputer(Computer computer) {
    return computerDao.addByComputer(computer);
  }

  /**
   * Update a computer in the database using a table of Strings as parameters.
   * @param params : String table composed of "id" (mandatory), "name", "introduced" (date format: yyyy-MM-dd), discontinued (date format: yyyy-MM-dd), "companyId".
   * All the attributes of the updated computer gets changed.
   * @return An instance of the computer that was updated in the database or null if the UPDATE did not work.
   */
  public Computer updateByString(String[] params) {
    return computerDao.updateByString(params);
  }

  /**
   * Update a computer in the database using a Computer instance.
   * @param computer : instance of the computer that needs to be added to the database. Must have an id at least. 
   * @return An instance of the computer that was updated in the database or null if the UPDATE did not work.
   */
  public Computer updateByComputer(Computer computer) {
    return computerDao.updateByComputer(computer);
  }

  /**
   * Remove a computer from the database using its id.
   * @param id : id of the computer to remove.
   * @return An instance of the computer that was removed from the database or null if the DELETE did not work.
   */
  public Computer removeById(Long id) {
    return computerDao.removeById(id);
  }

  /**
   * Remove all computers attached to the companyId given as parameter from the database.
   * @param id : id of the company that needs its computers to be removed.
   */
  public void removeByCompanyId(Long id) {
    Connection connection = UtilDaoSQL.getConnectionWithManualCommit();
    try {
      computerDao.removeByCompanyId(connection, id);
      UtilDaoSQL.commit(connection);
    } catch (PersistenceException e) {
      LOGGER.error("SQLError in removeById()");
      UtilDaoSQL.rollback(connection);
      throw new PersistenceException(e.getMessage(), e);
    } finally {
      UtilDaoSQL.close(connection);
    }
  }

  /**
   * Remove a list of computers from the database using their ids.
   * @param idList : the list of ids of the computers to remove.
   */
  public void removeByIdList(List<Long> idList) {
    computerDao.removeByIdList(idList);
  }

  /**
   * Remove a computer from the database using a Computer instance.
   * @param computer : instance of the computer that needs to be removed from the database. Must have an id at least. 
   * @return An instance of the computer that was removed from the database or null if the DELETE did not work.
   */
  public Computer removeByComputer(Computer computer) {
    return computerDao.removeByComputer(computer);
  }

  /**
   * Get a Page of computers in the database.
   * @param page : a page containing the pageIndex and the max number of elements the page can have
   * @return A Page instance containing a sublist of computers
   */
  public Page<Computer> getPagedList(final Page<Computer> page) {
    return computerDao.getPagedList(page);
  }
}
