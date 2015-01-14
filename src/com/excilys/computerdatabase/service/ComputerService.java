package com.excilys.computerdatabase.service;

import java.util.List;

import com.excilys.computerdatabase.domain.Computer;

/**
* Database Service for the Computer
* Singleton
* @author Jeremy SCARELLA
*
*/
public interface ComputerService {
  /**
   * Get the computer in the database corresponding to the id in parameter
   * @param id : id of the computer in the database
   * @return the computer that was found or null if there is no computer for this id
   */
  Computer getById(Long id);

  /**
  * Get the List of all the computers in the database
  * @return List of all the computers in the database
  */
  List<Computer> getAll();

  /**
  * Add a new computer in the database
  * @param params : params of the computer to add in the database (name, introduced, discontinued, company)
  */
  Computer addByString(String[] params);

  /**
  * Add a new computer in the database
  * @param computer : computer to add in the database
  */
  Computer addByComputer(Computer computer);

  /**
   * Update a computer of the database
   * @param params : params of the computer to update in the database (name, introduced, discontinued, company)
   */
  Computer updateByString(String[] params);

  /**
  * Update a computer of the database
  * @param computer : computer to update in the database
  */
  Computer updateByComputer(Computer computer);

  /**
  * Remove a computer in the database
  * @param id : id of the computer to delete
  */
  Computer removeById(Long id);

  /**
   * Remove a computer in the database
   * @param computer : computer to delete
   */
  Computer removeByComputer(Computer computer);

  /**
   * Get the last Id among computers in the DB
   * @return The max id of computer objects in DB
   */
  Long getLastId();
}