package com.excilys.computerdatabase.service.impl;

import java.sql.Connection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.exception.PersistenceException;
import com.excilys.computerdatabase.persistence.ICompanyDao;
import com.excilys.computerdatabase.persistence.IComputerDao;
import com.excilys.computerdatabase.persistence.impl.CompanyDaoImplSQL;
import com.excilys.computerdatabase.persistence.impl.ComputerDaoImplSQL;
import com.excilys.computerdatabase.persistence.impl.UtilDaoSQL;
import com.excilys.computerdatabase.service.ICompanyDBService;

/**
* Standard Service implementation to manage Company objects.
*
* @author Jeremy SCARELLA
*/
public enum CompanyDBServiceImpl implements ICompanyDBService {

  /*
  * Instance of CompanyServiceImpl
  */
  INSTANCE;

  /*
  * Instance of the ICompanyDao
  */
  private ICompanyDao         companyDao  = CompanyDaoImplSQL.INSTANCE;

  /*
  * Instance of the ICompanyDao
  */
  private IComputerDao        computerDao = ComputerDaoImplSQL.INSTANCE;

  /*
   * LOGGER
   */
  private static final Logger LOGGER      = LoggerFactory.getLogger(CompanyDBServiceImpl.class);

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

  /**
   * Remove a company from the database using its id.
   * @param id : id of the company to remove.
   * @return true if DELETE query was successful
   */
  public Boolean removeById(Long id) {
    Connection connection = UtilDaoSQL.getConnectionWithManualCommit();
    try {
      computerDao.removeByCompanyId(connection, id);
      companyDao.removeById(connection, id);
      UtilDaoSQL.commit(connection);
    } catch (PersistenceException e) {
      LOGGER.error("SQLError in removeById()");
      UtilDaoSQL.rollback(connection);
      throw new PersistenceException(e.getMessage(), e);
    } finally {
      UtilDaoSQL.close(connection);
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
    return companyDao.getPagedList(page);
  }
}