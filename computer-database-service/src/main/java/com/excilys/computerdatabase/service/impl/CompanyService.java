package com.excilys.computerdatabase.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.persistence.repository.CompanyRepository;
import com.excilys.computerdatabase.persistence.repository.ComputerRepository;
import com.excilys.computerdatabase.service.ICompanyService;

/**
* Standard Service implementation to manage Company objects.
*
* @author Jeremy SCARELLA
*/
@Service
@Transactional
public class CompanyService implements ICompanyService {

  /*
   * LOGGER
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(CompanyService.class);

  /*
   * Instance of CompanyRepository
   */
  @Autowired
  private CompanyRepository   companyRepository;

  /*
   * Instance of ComputerRepository
   */
  @Autowired
  private ComputerRepository  computerRepository;

  /**
   * Get the company in the database corresponding to the id in parameter.
   * @param id : id of the company in the database.
   * @return The company that was found or null if there is no company for this id.
   */
  @Transactional(readOnly = true)
  @Override
  public Company getById(final Long id) {
    LOGGER.debug("CompanyService - GET BY ID");
    if (id != null) {
      return companyRepository.findOne(id);
    } else {
      return null;
    }

  }

  /**
   * Get the List of all the companies in the database.
   * @return List of all the companies in the database.
   */
  @Transactional(readOnly = true)
  @Override
  public List<Company> getAll() {
    LOGGER.debug("CompanyService - GET ALL");
    return companyRepository.findAll();
  }

  /**
   * Remove a company from the database using its id.
   * @param id : id of the company to remove.
   * @return An instance of the company that was removed from the database or null
   */
  @Override
  public Company removeById(final Long id) {
    LOGGER.debug("CompanyService - REMOVE BY ID");
    if (id != null && companyRepository.exists(id)) {
      Company company = companyRepository.findOne(id);
      if (!computerRepository.findByCompanyId(id).isEmpty()) {
        computerRepository.deleteByCompanyId(id);
      }
      companyRepository.delete(id);
      return company;
    } else {
      return null;
    }
  }

  /**
   * Get a Page of companies from the database.
   * @param pageable: a Spring Pageable object
   * @return A Page instance containing a sublist of companies
   */
  @Transactional(readOnly = true)
  @Override
  public Page<Company> getPagedList(final Pageable pageable) {
    if (pageable != null) {
      return companyRepository.findAll(pageable);
    }
    return null;
  }
}