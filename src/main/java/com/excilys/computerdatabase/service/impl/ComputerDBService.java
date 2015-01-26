package com.excilys.computerdatabase.service.impl;

import java.util.List;

import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.dto.ComputerDto;
import com.excilys.computerdatabase.dto.ComputerDtoConverter;
import com.excilys.computerdatabase.persistence.IComputerDao;
import com.excilys.computerdatabase.persistence.impl.ComputerDaoSQL;
import com.excilys.computerdatabase.service.IComputerDBService;

/**
* Standard Service implementation to manage Computer objects.
*
* @author Jeremy SCARELLA
*/
public enum ComputerDBService implements IComputerDBService {

  /*
  * Instance of ComputerServiceImpl
  */
  INSTANCE;

  /*
  * Instance of the IComputerDao
  */
  private IComputerDao computerDao = ComputerDaoSQL.INSTANCE;

  /**
   * Get the computer in the database corresponding to the id in parameter.
   * @param id : id of the computer in the database.
   * @return The computer that was found or null if there is no computer for this id.
   */
  public ComputerDto getById(Long id) {
    Computer computer = computerDao.getById(id);
    return ComputerDtoConverter.toDto(computer);
  }

  /**
   * Get the List of all the computers in the database.
   * @return List of all the computers in the database.
   */
  public List<ComputerDto> getAll() {
    List<Computer> computers = computerDao.getAll();
    return ComputerDtoConverter.toDto(computers);
  }

  /**
   * Add a computer in the database using a table of Strings as parameters.
   * @param params : String table composed of "name" (mandatory), "introduced" (date format: yyyy-MM-dd), discontinued (date format: yyyy-MM-dd), "companyId".
   * Use the String "null" to skip a value.
   * @return An instance of the computer that was added to the database or null if the INSERT did not work.
   */
  public ComputerDto addByString(String[] params) {
    Computer computer = computerDao.addByString(params);
    return ComputerDtoConverter.toDto(computer);
  }

  /**
   * Add a computer in the database using a Computer instance.
   * @param computer : instance of the computer that needs to be added to the database. Must have a name at least. 
   * @return An instance of the computer that was added to the database or null if the INSERT did not work.
   */
  public ComputerDto addByComputer(Computer computer) {
    computer = computerDao.addByComputer(computer);
    return ComputerDtoConverter.toDto(computer);
  }

  /**
   * Update a computer in the database using a table of Strings as parameters.
   * @param params : String table composed of "id" (mandatory), "name", "introduced" (date format: yyyy-MM-dd), discontinued (date format: yyyy-MM-dd), "companyId".
   * All the attributes of the updated computer gets changed.
   * @return An instance of the computer that was updated in the database or null if the UPDATE did not work.
   */
  public ComputerDto updateByString(String[] params) {
    Computer computer = computerDao.updateByString(params);
    return ComputerDtoConverter.toDto(computer);
  }

  /**
   * Update a computer in the database using a Computer instance.
   * @param computer : instance of the computer that needs to be added to the database. Must have an id at least. 
   * @return An instance of the computer that was updated in the database or null if the UPDATE did not work.
   */
  public ComputerDto updateByComputer(Computer computer) {
    computer = computerDao.updateByComputer(computer);
    return ComputerDtoConverter.toDto(computer);
  }

  /**
   * Remove a computer from the database using its id.
   * @param id : id of the computer to remove.
   * @return An instance of the computer that was removed from the database or null if the DELETE did not work.
   */
  public ComputerDto removeById(Long id) {
    Computer computer = computerDao.removeById(id);
    return ComputerDtoConverter.toDto(computer);
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
  public ComputerDto removeByComputer(Computer computer) {
    computer = computerDao.removeByComputer(computer);
    return ComputerDtoConverter.toDto(computer);
  }

  /**
   * Get a Page of computers in the database.
   * @param page : a page containing the pageIndex and the max number of elements the page can have
   * @return A Page instance containing a sublist of computers
   */
  public Page<ComputerDto> getPagedList(final Page<ComputerDto> page) {
    return computerDao.getPagedList(page);
  }
}
