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
import com.excilys.computerdatabase.persistence.repository.ComputerRepository;
import com.excilys.computerdatabase.service.IComputerService;
import com.excilys.computerdatabase.validator.StringValidator;

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
  public Computer getById(final Long id) {
    LOGGER.debug("ComputerService - GET BY ID");
    if (id != null) {
      return computerRepository.findOne(id);
    } else {
      return null;
    }
  }

  /**
   * Get the list of all computers with specified company id in the database.
   * @param id: id of the company attached to the computers in the database.
   * @return The computer list that was found or null if there is no computer for this company id.
   */
  @Transactional(readOnly = true)
  @Override
  public List<Computer> getByCompanyId(final Long id) {
    LOGGER.debug("ComputerService - GET BY COMPANY ID");
    if (id != null) {
      return computerRepository.findByCompanyId(id);
    } else {
      return null;
    }
  }

  /**
   * Get the List of all the computers in the database.
   * @return List of all the computers in the database.
   */
  @Transactional(readOnly = true)
  @Override
  public List<Computer> getAll() {
    LOGGER.debug("ComputerService - GET ALL");
    return computerRepository.findAll();
  }

  /**
   * Add a computer in the database using a Computer instance.
   * @param computer: instance of the computer that needs to be added to the database. Must have a name at least. 
   * @return An instance of the computer that was added to the database or null if the INSERT did not work.
   */
  @Override
  public Computer addByComputer(final Computer computer) {
    LOGGER.debug("ComputerService - ADD BY COMPUTER");
    if (computer != null && computer.getName() != null
        && StringValidator.isValidName(computer.getName())) {
      Computer addedComputer = computerRepository.save(computer);
      if (computerRepository.exists(addedComputer.getId())) {
        return computerRepository.findOne(addedComputer.getId());
      }
    }
    return null;

  }

  /**
   * Update a computer in the database using a Computer instance.
   * @param computer: instance of the computer that needs to be added to the database. Must have an id and a name at least. 
   * @return An instance of the computer that was updated in the database or null if the UPDATE did not work.
   */
  @Override
  public Computer updateByComputer(final Computer computer) {
    LOGGER.debug("ComputerService - UPDATE BY COMPUTER");
    if (computer != null && computer.getId() != null && computer.getName() != null
        && StringValidator.isValidName(computer.getName())
        && computerRepository.exists(computer.getId())) {
      computerRepository.save(computer);
      return computerRepository.findOne(computer.getId());
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
  public Computer removeById(final Long id) {
    LOGGER.debug("ComputerService - REMOVE BY ID");
    if (id != null && computerRepository.exists(id)) {
      Computer removedComputer = computerRepository.findOne(id);
      computerRepository.delete(id);
      return removedComputer;
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
  public List<Computer> removeByIdList(final List<Long> idList) {
    LOGGER.debug("ComputerService - REMOVE BY ID LIST");
    if (!idList.isEmpty()) {
      List<Computer> computers = new ArrayList<Computer>();
      idList.forEach(id -> {
        if (computerRepository.exists(id)) {
          computers.add(computerRepository.findOne(id));
          computerRepository.delete(id);
        }
      });
      return computers;
    } else {
      return null;
    }
  }

  /**
   * Remove a computer from the database using a Computer instance.
   * @param computer: instance of the computer that needs to be removed from the database. Must have an id at least. 
   * @return An instance of the computer that was removed from the database or null if the DELETE did not work.
   */
  @Override
  public Computer removeByComputer(final Computer computer) {
    LOGGER.debug("ComputerService - REMOVE BY COMPUTER");
    if (computer != null && computerRepository.exists(computer.getId())) {
      Computer removedComputer = computerRepository.findOne(computer.getId());
      computerRepository.delete(computer.getId());
      return removedComputer;
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
    if (pageable != null) {
      return computerRepository.findByNameStartingWithOrCompanyNameStartingWith(search, search,
          pageable);
    }
    return null;
  }
}
