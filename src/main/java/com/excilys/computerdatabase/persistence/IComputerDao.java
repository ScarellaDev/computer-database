package com.excilys.computerdatabase.persistence;

import java.sql.Connection;
import java.util.List;

import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.dto.ComputerDto;

/**
* Interface implemented by Daos to manage computers.
*
* @author Jeremy SCARELLA
*/
public interface IComputerDao {

  /**
   * Get the computer in the database corresponding to the id in parameter.
   * @param id : id of the computer in the database.
   * @return The computer that was found or null if there is no computer for this id.
   */
  Computer getById(Long id);

  /**
   * Get the List of all the computers in the database.
   * @return List of all the computers in the database.
   */
  List<Computer> getAll();

  /**
   * Add a computer in the database using a table of Strings as parameters.
   * @param params : String table composed of "name" (mandatory), "introduced" (date format: yyyy-MM-dd), discontinued (date format: yyyy-MM-dd), "companyId".
   * Use the String "null" to skip a value.
   * @return An instance of the computer that was added to the database or null if the INSERT did not work.
   */
  Computer addByString(String[] params);

  /**
   * Add a computer in the database using a Computer instance.
   * @param computer : instance of the computer that needs to be added to the database. Must have a name at least. 
   * @return An instance of the computer that was added to the database or null if the INSERT did not work.
   */
  Computer addByComputer(Computer computer);

  /**
   * Update a computer in the database using a table of Strings as parameters.
   * @param params : String table composed of "id" (mandatory), "name", "introduced" (date format: yyyy-MM-dd), discontinued (date format: yyyy-MM-dd), "companyId".
   * All the attributes of the updated computer are changed.
   * @return An instance of the computer that was updated in the database or null if the UPDATE did not work.
   */
  Computer updateByString(String[] params);

  /**
   * Update a computer in the database using a Computer instance.
   * @param computer : instance of the computer that needs to be added to the database. Must have an id at least. 
   * @return An instance of the computer that was updated in the database or null if the UPDATE did not work.
   */
  Computer updateByComputer(Computer computer);

  /**
   * Remove a computer from the database using its id.
   * @param id : id of the computer to remove.
   * @return An instance of the computer that was removed from the database or null if the DELETE did not work.
   */
  Computer removeById(Long id);

  /**
   * Remove all computers attached to the companyId given as parameter from the database.
   * @param connection : the shared Connection for the CompanyDBService.removeById().
   * @param id : id of the company that needs its computers to be removed.
   */
  void removeByCompanyId(Connection connection, Long id);

  /**
   * Remove a list of computers from the database using their ids.
   * @param idList : the list of ids of the computers to remove.
   */
  void removeByIdList(List<Long> idList);

  /**
   * Remove a computer from the database using a Computer instance.
   * @param computer : instance of the computer that needs to be removed from the database. Must have an id at least. 
   * @return An instance of the computer that was removed from the database or null if the DELETE did not work.
   */
  Computer removeByComputer(Computer computer);

  /**
   * Get a Page of computers in the database.
   * @param page : a page containing the pageIndex and the max number of elements the page can have
   * @return A Page instance containing a sublist of computers
   */
  Page<ComputerDto> getPagedList(Page<ComputerDto> page);
}