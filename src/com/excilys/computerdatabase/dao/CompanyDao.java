package com.excilys.computerdatabase.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.excilys.computerdatabase.bean.Company;

public class CompanyDao {
  private MySQLConnector mySQLConnector;

  public CompanyDao() {
    super();
    mySQLConnector = new MySQLConnector();
  }

  public Boolean connect() {
    return mySQLConnector.connect();
  }

  public Company getCompany(Long id) {
    if(id == null) {
      return null;
    }
    
    Company company = new Company();

    if (connect()) {
      String SQLRequest = "SELECT * FROM `computer-database-db`.company WHERE id=" + id + ";";
      ResultSet rs1 = mySQLConnector.selectRequest(SQLRequest);

      try {
        while (rs1.next()) {
          company.setId(rs1.getLong("id"));
          company.setName(rs1.getString("name"));
        }
        rs1.close();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } finally {
        mySQLConnector.close();
      }
      return company;
    }

    return null;
  }

  public List<Company> getAllCompanies() {
    List<Company> companies = new ArrayList<Company>();

    if (connect()) {
      String SQLRequest = "SELECT * FROM `computer-database-db`.company;";
      ResultSet rs1 = mySQLConnector.selectRequest(SQLRequest);

      try {
        while (rs1.next()) {
          Company company = new Company();
          company.setId(rs1.getLong("id"));
          company.setName(rs1.getString("name"));

          companies.add(company);
        }
        rs1.close();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } finally {
        mySQLConnector.close();
      }
      return companies;
    }

    return null;
  }
}
