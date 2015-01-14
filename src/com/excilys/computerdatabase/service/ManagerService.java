package com.excilys.computerdatabase.service;

import com.excilys.computerdatabase.service.impl.CompanyServiceImpl;
import com.excilys.computerdatabase.service.impl.ComputerServiceImpl;

/**
* Singleton managing services instances.
*
* @author Jeremy SCARELLA
*/
public enum ManagerService {

  INSTANCE;

  private ComputerService computerService;
  private CompanyService  companyService;

  public static ManagerService getInstance() {
    return INSTANCE;
  }

  private ManagerService() {
    computerService = new ComputerServiceImpl();
    companyService = new CompanyServiceImpl();
  }

  public ComputerService getComputerService() {
    return computerService;
  }

  public CompanyService getCompanyService() {
    return companyService;
  }
}