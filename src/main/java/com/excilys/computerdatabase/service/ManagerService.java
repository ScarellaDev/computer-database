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
  private IComputerService computerService;

  /*
   * Instance of companyService
   */
  private ICompanyService  companyService;

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
  public IComputerService getComputerService() {
    return computerService;
  }

  public ICompanyService getCompanyService() {
    return companyService;
  }
}