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
  private ComputerDao computerDao;

  /*
   * Instance of CompanyDao
   */
  private CompanyDao  companyDao;

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
  public ComputerDao getComputerDao() {
    return computerDao;
  }

  public CompanyDao getCompanyDao() {
    return companyDao;
  }
}