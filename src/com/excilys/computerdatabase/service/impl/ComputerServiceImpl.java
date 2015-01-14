package com.excilys.computerdatabase.service.impl;

import java.util.List;

import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.persistence.ComputerDao;
import com.excilys.computerdatabase.persistence.ManagerDao;
import com.excilys.computerdatabase.service.ComputerService;

/**
* ComputerService implementation
*
* @author Jeremy SCARELLA
*/
public class ComputerServiceImpl implements ComputerService {
  private ComputerDao computerDao;

  public ComputerServiceImpl() {
    computerDao = ManagerDao.getInstance().getComputerDao();
  }

  /**
   * Get the computer in the database corresponding to the id in parameter
   * @param id : id of the computer in the database
   * @return the computer that was found or null if there is no computer for this id
   */
  public Computer getById(Long id) {
    return computerDao.getById(id);
  }

  /**
  * Get the List of all the computers in the database
  * @return List of all the computers in the database
  */
  public List<Computer> getAll() {
    return computerDao.getAll();
  }

  /**
  * Add a new computer in the database
  * @param params : params of the computer to add in the database (name, introduced, discontinued, company)
  */
  public Computer addByString(String[] params) {
    return computerDao.addByString(params);
  }

  /**
  * Add a new computer in the database
  * @param computer : computer to add in the database
  */
  public Computer addByComputer(Computer computer) {
    return computerDao.addByComputer(computer);
  }

  /**
   * Update a computer of the database
   * @param params : params of the computer to update in the database (name, introduced, discontinued, company)
   */
  public Computer updateByString(String[] params) {
    return computerDao.updateByString(params);
  }

  /**
  * Update a computer of the database
  * @param computer : computer to update in the database
  */
  public Computer updateByComputer(Computer computer) {
    return computerDao.updateByComputer(computer);
  }

  /**
  * Remove a computer in the database
  * @param id : id of the computer to delete
  */
  public Computer removeById(Long id) {
    return computerDao.removeById(id);
  }

  /**
   * Remove a computer in the database
   * @param computer : computer to delete
   */
  public Computer removeByComputer(Computer computer) {
    return computerDao.removeByComputer(computer);
  }

  /**
   * Get the last Id among computers in the DB
   * @return The max id of computer objects in DB
   */
  public Long getLastId() {
    return computerDao.getLastId();
  }
}
