package com.excilys.computerdatabase.service.mock;

import java.util.List;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.exception.PersistenceException;
import com.excilys.computerdatabase.persistence.ConnectionManager;
import com.excilys.computerdatabase.persistence.ICompanyDao;
import com.excilys.computerdatabase.persistence.IComputerDao;
import com.excilys.computerdatabase.service.ICompanyDBService;

/**
* Mock standard Service implementation to manage Company objects.
*
* @author Jeremy SCARELLA
*/
public class CompanyServiceMock implements ICompanyDBService {

  /*
   * CONNECTION_MANAGER
   */
  private static final ConnectionManager CM = ConnectionManager.INSTANCE;

  /*
  * Instance of ICompanyDao
  */
  private ICompanyDao                    companyDao;

  /*
  * Instance of the ICompanyDao
  */
  private IComputerDao                   computerDao;

  /*
   * Instance of CompanyServiceMock
   */
  public CompanyServiceMock(ICompanyDao companyDao, IComputerDao computerDao) {
    this.companyDao = companyDao;
    this.computerDao = computerDao;
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

  /**
   * Remove a company from the database using its id.
   * @param id : id of the company to remove.
   * @return true if DELETE query was successful
   */
  public Boolean removeById(Long id) {
    try {
      CM.startTransactionConnection();
      computerDao.removeByCompanyId(id);
      companyDao.removeById(id);
      CM.commitTransactionConnection();
    } catch (PersistenceException e) {

    } finally {
      CM.closeTransactionConnection();
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