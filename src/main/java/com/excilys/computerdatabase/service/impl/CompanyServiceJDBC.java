package com.excilys.computerdatabase.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.exception.PersistenceException;
import com.excilys.computerdatabase.persistence.ConnectionManager;
import com.excilys.computerdatabase.persistence.ICompanyDao;
import com.excilys.computerdatabase.persistence.IComputerDao;
import com.excilys.computerdatabase.persistence.impl.CompanyDaoSQL;
import com.excilys.computerdatabase.persistence.impl.ComputerDaoSQL;
import com.excilys.computerdatabase.service.ICompanyService;

/**
* Standard Service implementation to manage Company objects.
*
* @author Jeremy SCARELLA
*/
public enum CompanyServiceJDBC implements ICompanyService {

  /*
  * Instance of CompanyServiceImpl
  */
  INSTANCE;

  /*
   * CONNECTION_MANAGER
   */
  private static final ConnectionManager CM          = ConnectionManager.INSTANCE;

  /*
  * Instance of the ICompanyDao
  */
  private ICompanyDao                    companyDao  = CompanyDaoSQL.INSTANCE;

  /*
  * Instance of the ICompanyDao
  */
  private IComputerDao                   computerDao = ComputerDaoSQL.INSTANCE;

  /*
   * LOGGER
   */
  private static final Logger            LOGGER      = LoggerFactory
                                                         .getLogger(CompanyServiceJDBC.class);

  /**
   * Get the company in the database corresponding to the id in parameter.
   * @param id : id of the company in the database.
   * @return The company that was found or null if there is no company for this id.
   */
  @Override
  public Company getById(Long id) {
    Company company = null;
    try {
      company = companyDao.getById(id);
    } catch (PersistenceException e) {
      LOGGER.warn("PersistenceException: during getById()", e);
    } finally {
      CM.closeConnection();
    }
    return company;
  }

  /**
   * Get the List of all the companies in the database.
   * @return List of all the companies in the database.
   */
  @Override
  public List<Company> getAll() {
    List<Company> companies = null;
    try {
      companies = companyDao.getAll();
    } catch (PersistenceException e) {
      LOGGER.warn("PersistenceException: during getAll()", e);
    } finally {
      CM.closeConnection();
    }
    return companies;
  }

  /**
   * Remove a company from the database using its id.
   * @param id : id of the company to remove.
   * @return true if DELETE query was successful
   */
  public Boolean removeById(Long id) {
    CM.startTransaction();
    try {
      computerDao.removeByCompanyId(id);
      companyDao.removeById(id);
      CM.commit();
    } catch (PersistenceException e) {
      CM.rollback();
      LOGGER.warn("PersistenceException: during removeById()", e);
    } finally {
      CM.closeConnection();
    }
    return true;
  }

  /**
   * Get a Page of companies in the database.
   * @param page : a page containing the pageIndex and the max number of elements the page can have
   * @return A Page instance containing a sublist of companies
   */
  @Override
  public Page<Company> getPagedList(final Page<Company> page) {
    Page<Company> newPage = null;
    try {
      newPage = companyDao.getPagedList(page);
    } catch (PersistenceException e) {
      LOGGER.warn("PersistenceException: during getPagedList()", e);
    } finally {
      CM.closeConnection();
    }
    return newPage;
  }
}