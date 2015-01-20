package com.excilys.computerdatabase.service;

import com.excilys.computerdatabase.service.impl.CompanyServiceImpl;
import com.excilys.computerdatabase.service.impl.ComputerServiceImpl;

/**
* Singleton managing Service instances.
*
* @author Jeremy SCARELLA
*/
public enum ManagerService {
  /*
   * Instance of ManagerService
   */
  INSTANCE;

  /*
   * Instance of computerDBService
   */
  private IComputerDBService computerDBService;

  /*
   * Instance of companyDBService
   */
  private ICompanyDBService  companyDBService;

  /**
   * Return the instance of ManagerService.
   * @return Instance of ManagerService.
   */
  public static ManagerService getInstance() {
    return INSTANCE;
  }

  /*
   * Constructor
   */
  private ManagerService() {
    computerDBService = new ComputerServiceImpl();
    companyDBService = new CompanyServiceImpl();
  }

  /*
   * Getter
   */
  public IComputerDBService getComputerDBService() {
    return computerDBService;
  }

  public ICompanyDBService getCompanyDBService() {
    return companyDBService;
  }
}