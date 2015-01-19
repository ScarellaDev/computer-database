package com.excilys.computerdatabase.test.service.mock;

import java.util.List;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.persistence.CompanyDao;
import com.excilys.computerdatabase.service.CompanyService;

/**
* Mock standard Service implementation to manage Company objects.
*
* @author Jeremy SCARELLA
*/
public class CompanyServiceMock implements CompanyService {
  /*
   * Instance of companyDao
   */
  private CompanyDao companyDao;

  /**
   * Constructor
   * @param companyDao
   */
  public CompanyServiceMock(CompanyDao companyDao) {
    this.companyDao = companyDao;
  }

  /**
   * Get the company in the database corresponding to the id in parameter.
   * @param id : id of the company in the database.
   * @return The company that was found or null if there is no company for this id.
   */
  @Override
  public Company getById(Long id) {
    return companyDao.getById(id);
  }

  /**
   * Get the List of all the companies in the database.
   * @return List of all the companies in the database.
   */
  @Override
  public List<Company> getAll() {
    return companyDao.getAll();
  }
}