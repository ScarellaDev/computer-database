package com.excilys.computerdatabase.service;

import java.io.Serializable;
import java.util.List;

import com.excilys.computerdatabase.model.Company;
import com.excilys.computerdatabase.persistence.CompanyDao;

public class CompanyService implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private CompanyDao        companyDao       = new CompanyDao();

  public Company getCompany(Long id) {
    return companyDao.getCompany(id);
  }

  public List<Company> getAllCompanies() {
    return companyDao.getAllCompanies();
  }
}
