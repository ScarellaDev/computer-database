package com.excilys.computerdatabase.test;

import java.util.ArrayList;
import java.util.List;

import com.excilys.computerdatabase.bean.Company;
import com.excilys.computerdatabase.bean.Computer;
import com.excilys.computerdatabase.dao.CompanyDao;
import com.excilys.computerdatabase.dao.ComputerDao;

public class Test {
  public static void main(String[] args) {
    // TODO Auto-generated method stub

    //Print computer list from DB
    ComputerDao computerDao = new ComputerDao();
    List<Computer> computers = new ArrayList<Computer>();
    computers = computerDao.getAllComputers();
    if (computers != null) {
      for (Computer computer : computers) {
        System.out.println(computer.toString());
      }
    } else {
      System.out.println("computers == null");
    }

    //Print company list from DB
    CompanyDao companyDao = new CompanyDao();
    List<Company> companies = new ArrayList<Company>();
    companies = companyDao.getAllCompanies();
    if (companies != null) {
      for (Company company : companies) {
        System.out.println(company.toString());
      }
    } else {
      System.out.println("computers == null");
    }
  }
}