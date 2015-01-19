package com.excilys.computerdatabase.test.service.mock;

import java.util.List;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.persistence.CompanyDao;
import com.excilys.computerdatabase.service.CompanyService;

public class CompanyServiceMock implements CompanyService {

  private CompanyDao companyDao;

  public CompanyServiceMock(CompanyDao companyDao) {
    this.companyDao = companyDao;
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