package com.excilys.computerdatabase.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.dto.ComputerDto;

/**
* Interface implemented by services to manage Computer objects.
*
* @author Jeremy SCARELLA
*/
public interface IComputerService {
  /**
   * Get the computer in the database corresponding to the id in parameter.
   * @param id : id of the computer in the database.
   * @return The computer that was found or null if there is no computer for this id.
   */
  ComputerDto getById(Long id);

  /**
   * Get the list of all computers with specified company id in the database.
   * @param id: id of the company attached to the computers in the database.
   * @return The computer list that was found or null if there is no computer for this company id.
   */
  List<ComputerDto> getByCompanyId(final Long id);

  /**
   * Get the List of all the computers in the database.
   * @return List of all the computers in the database.
   */
  List<ComputerDto> getAll();

  /**
   * Add a computer in the database using a Computer instance.
   * @param computer : instance of the computer that needs to be added to the database. Must have a name at least. 
   * @return An instance of the computer that was added to the database or null if the INSERT did not work.
   */
  ComputerDto addByComputer(Computer computer);

  /**
   * Update a computer in the database using a Computer instance.
   * @param computer : instance of the computer that needs to be added to the database. Must have an id at least. 
   * @return An instance of the computer that was updated in the database or null if the UPDATE did not work.
   */
  ComputerDto updateByComputer(Computer computer);

  /**
   * Remove a computer from the database using its id.
   * @param id : id of the computer to remove.
   * @return An instance of the computer that was removed from the database or null if the DELETE did not work.
   */
  ComputerDto removeById(Long id);

  /**
   * Remove a list of computers from the database using their ids.
   * @param idList : the list of ids of the computers to remove.
   * @return The list of deleted computers
   */
  List<ComputerDto> removeByIdList(List<Long> idList);

  /**
   * Remove a computer from the database using a Computer instance.
   * @param computer : instance of the computer that needs to be removed from the database. Must have an id at least. 
   * @return An instance of the computer that was removed from the database or null if the DELETE did not work.
   */
  ComputerDto removeByComputer(Computer computer);

  /**
   * Get a Page of computers from the database.
   * @param search: the search String entered by the user
   * @param pageable: a Spring Pageable object
   * @return A Page instance containing a sublist of computers
   */
  Page<Computer> getPagedList(String search, Pageable pageable);
}