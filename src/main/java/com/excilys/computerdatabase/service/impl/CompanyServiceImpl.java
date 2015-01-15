package com.excilys.computerdatabase.service.impl;

import java.util.List;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.persistence.CompanyDao;
import com.excilys.computerdatabase.persistence.ManagerDao;
import com.excilys.computerdatabase.service.CompanyService;

/**
* Standard service implementation to manage companies.
*
* @author Jeremy SCARELLA
*/
public class CompanyServiceImpl implements CompanyService {
  private CompanyDao companyDao;

  public CompanyServiceImpl() {
    companyDao = ManagerDao.getInstance().getCompanyDao();
  }

  @Override
  public Company getById(Long id) {
    return companyDao.getById(id);
  }

  @Override
  public List<Company> getAll() {
    return companyDao.getAll();
  }
}