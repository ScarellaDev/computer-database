package com.excilys.computerdatabase.persistence;

import com.excilys.computerdatabase.persistence.impl.CompanyDaoImplSQL;
import com.excilys.computerdatabase.persistence.impl.ComputerDaoImplSQL;

/**
* Singleton managing Daos instances.
*
* @author Jeremy SCARELLA
*/
public enum ManagerDao {

  /*
   * Instance of ManagerDao
   */
  INSTANCE;

  /*
  * Instance of ComputerDao
  */
  private IComputerDao computerDao;

  /*
   * Instance of CompanyDao
   */
  private ICompanyDao  companyDao;

  /**
   * Return the instance of ManagerDao.
   * @return Instance of ManagerDao.
   */
  public static ManagerDao getInstance() {
    return INSTANCE;
  }

  /*
   * Constructor
   */
  private ManagerDao() {
    computerDao = ComputerDaoImplSQL.getInstance();
    companyDao = CompanyDaoImplSQL.getInstance();
  }

  /*
   * Getter
   */
  public IComputerDao getComputerDao() {
    return computerDao;
  }

  public ICompanyDao getCompanyDao() {
    return companyDao;
  }
}