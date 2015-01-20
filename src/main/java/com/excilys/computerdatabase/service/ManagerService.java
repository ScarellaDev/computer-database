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
   * Instance of computerService
   */
  private IComputerDBService computerService;

  /*
   * Instance of companyService
   */
  private ICompanyDBService  companyService;

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
    computerService = new ComputerServiceImpl();
    companyService = new CompanyServiceImpl();
  }

  /*
   * Getter
   */
  public IComputerDBService getComputerService() {
    return computerService;
  }

  public ICompanyDBService getCompanyService() {
    return companyService;
  }
}