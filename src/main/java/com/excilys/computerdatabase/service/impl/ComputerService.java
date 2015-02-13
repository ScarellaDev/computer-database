package com.excilys.computerdatabase.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.dto.ComputerDto;
import com.excilys.computerdatabase.dto.ComputerDtoConverter;
import com.excilys.computerdatabase.persistence.repository.ComputerRepository;
import com.excilys.computerdatabase.service.IComputerService;

/**
* Standard Service implementation to manage Computer objects.
*
* @author Jeremy SCARELLA
*/
@Service
@Transactional
public class ComputerService implements IComputerService {

  /*
   * LOGGER
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(ComputerService.class);

  /*
   * Instance of ComputerRepository
   */
  @Autowired
  private ComputerRepository  computerRepository;

  /**
   * Get the computer in the database corresponding to the id in parameter.
   * @param id: id of the computer in the database.
   * @return The computer that was found or null if there is no computer for this id.
   */
  @Transactional(readOnly = true)
  @Override
  public ComputerDto getById(final Long id) {
    LOGGER.debug("ComputerService - GET BY ID");
    return ComputerDtoConverter.toDto(computerRepository.findOne(id));
  }

  /**
   * Get the list of all computers with specified company id in the database.
   * @param id: id of the company attached to the computers in the database.
   * @return The computer list that was found or null if there is no computer for this company id.
   */
  @Transactional(readOnly = true)
  @Override
  public List<ComputerDto> getByCompanyId(final Long id) {
    LOGGER.debug("ComputerService - GET BY COMPANY ID");
    return ComputerDtoConverter.toDto(computerRepository.findByCompanyId(id));
  }

  /**
   * Get the List of all the computers in the database.
   * @return List of all the computers in the database.
   */
  @Transactional(readOnly = true)
  @Override
  public List<ComputerDto> getAll() {
    LOGGER.debug("ComputerService - GET ALL");
    return ComputerDtoConverter.toDto(computerRepository.findAll());
  }

  /**
   * Add a computer in the database using a Computer instance.
   * @param computer: instance of the computer that needs to be added to the database. Must have a name at least. 
   * @return An instance of the computer that was added to the database or null if the INSERT did not work.
   */
  @Override
  public ComputerDto addByComputer(final Computer computer) {
    LOGGER.debug("ComputerService - ADD BY COMPUTER");
    return ComputerDtoConverter.toDto(computerRepository.save(computer));
  }

  /**
   * Update a computer in the database using a Computer instance.
   * @param computer: instance of the computer that needs to be added to the database. Must have an id at least. 
   * @return An instance of the computer that was updated in the database or null if the UPDATE did not work.
   */
  @Override
  public ComputerDto updateByComputer(final Computer computer) {
    LOGGER.debug("ComputerService - UPDATE BY COMPUTER");
    if (computerRepository.exists(computer.getId())) {
      computerRepository.save(computer);
      Computer updatedComputer = computerRepository.findOne(computer.getId());
      return ComputerDtoConverter.toDto(updatedComputer);
    } else {
      return null;
    }
  }

  /**
   * Remove a computer from the database using its id.
   * @param id: id of the computer to remove.
   * @return An instance of the computer that was removed from the database or null if the DELETE did not work.
   */
  @Override
  public ComputerDto removeById(final Long id) {
    LOGGER.debug("ComputerService - REMOVE BY ID");
    if (computerRepository.exists(id)) {
      Computer computer = computerRepository.findOne(id);
      computerRepository.delete(id);
      return ComputerDtoConverter.toDto(computer);
    } else {
      return null;
    }
  }

  /**
   * Remove a list of computers from the database using their ids.
   * @param idList: the list of ids of the computers to remove.
   * * @return The list of deleted computers
   */
  @Override
  public List<ComputerDto> removeByIdList(final List<Long> idList) {
    LOGGER.debug("ComputerService - REMOVE BY ID LIST");
    List<Computer> computers = new ArrayList<Computer>();
    idList.forEach(id -> {
      if (computerRepository.exists(id)) {
        computers.add(computerRepository.findOne(id));
        computerRepository.delete(id);
      }
    });
    return ComputerDtoConverter.toDto(computers);
  }

  /**
   * Remove a computer from the database using a Computer instance.
   * @param computer: instance of the computer that needs to be removed from the database. Must have an id at least. 
   * @return An instance of the computer that was removed from the database or null if the DELETE did not work.
   */
  @Override
  public ComputerDto removeByComputer(final Computer computer) {
    LOGGER.debug("ComputerService - REMOVE BY COMPUTER");
    if (computerRepository.exists(computer.getId())) {
      Computer removedComputer = computerRepository.findOne(computer.getId());
      computerRepository.delete(computer.getId());
      return ComputerDtoConverter.toDto(removedComputer);
    } else {
      return null;
    }
  }

  /**
   * Get a Page of computers from the database.
   * @param search: the search String entered by the user
   * @param pageable: a Spring Pageable object
   * @return A Page instance containing a sublist of computers
   */
  @Override
  @Transactional(readOnly = true)
  public Page<Computer> getPagedList(final String search, final Pageable pageable) {
    return computerRepository.findByNameStartingWithOrCompanyNameStartingWith(search, search,
        pageable);
  }
}
