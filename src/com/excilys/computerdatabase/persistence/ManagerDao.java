package com.excilys.computerdatabase.persistence;

import com.excilys.computerdatabase.persistence.impl.CompanyDaoImplSQL;
import com.excilys.computerdatabase.persistence.impl.ComputerDaoImplSQL;

/**
* Singleton managing DAOs instances.
*
* @author Jeremy SCARELLA
*/
public enum ManagerDao {

  INSTANCE;

  /**
  * Instance of the ComputerDao
  */
  private ComputerDao computerDao;

  /**
   * Instance of the CompanyDao
   */
  private CompanyDao  companyDao;

  public static ManagerDao getInstance() {
    return INSTANCE;
  }

  private ManagerDao() {
    computerDao = ComputerDaoImplSQL.getInstance();
    companyDao = CompanyDaoImplSQL.getInstance();
  }

  public ComputerDao getComputerDao() {
    return computerDao;
  }

  public CompanyDao getCompanyDao() {
    return companyDao;
  }
}