package com.excilys.computerdatabase.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.excilys.computerdatabase.bean.Company;
import com.excilys.computerdatabase.bean.Computer;

public class ComputerDao {
  private MySQLConnector mySQLConnector;

  public ComputerDao() {
    super();
    mySQLConnector = new MySQLConnector();
  }

  public Boolean connect() {
    return mySQLConnector.connect();
  }

  public Computer getComputer(Long id) {
    Computer computer = new Computer();

    if (connect()) {
      String SQLRequest = "SELECT * FROM `computer-database-db`.computer WHERE id=" + id + ";";
      ResultSet rs1 = mySQLConnector.selectRequest(SQLRequest);
      CompanyDao companyDao = new CompanyDao();

      try {
        while (rs1.next()) {
          computer.setId(rs1.getLong("id"));
          computer.setName(rs1.getString("name"));
          Timestamp introduced = rs1.getTimestamp("introduced");
          if (introduced != null) {
            computer.setIntroduced(rs1.getTimestamp("introduced").toLocalDateTime());
          }
          Timestamp discontinued = rs1.getTimestamp("discontinued");
          if (discontinued != null) {
            computer.setDiscontinued(rs1.getTimestamp("discontinued").toLocalDateTime());
          }
          Company company = new Company();
          company = companyDao.getCompany(rs1.getLong("company_id"));
          if(company != null) {
            computer.setCompany(company);
          }
        }
        rs1.close();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } finally {
        mySQLConnector.close();
      }
      return computer;
    }

    return null;
  }

  public List<Computer> getAllComputers() {
    List<Computer> computers = new ArrayList<Computer>();

    if (connect()) {
      String SQLRequest = "SELECT * FROM `computer-database-db`.computer;";
      ResultSet rs1 = mySQLConnector.selectRequest(SQLRequest);
      CompanyDao companyDao = new CompanyDao();

      try {
        while (rs1.next()) {
          Computer computer = new Computer();
          computer.setId(rs1.getLong("id"));
          computer.setName(rs1.getString("name"));
          Timestamp introduced = rs1.getTimestamp("introduced");
          if (introduced != null) {
            computer.setIntroduced(rs1.getTimestamp("introduced").toLocalDateTime());
          }
          Timestamp discontinued = rs1.getTimestamp("discontinued");
          if (discontinued != null) {
            computer.setDiscontinued(rs1.getTimestamp("discontinued").toLocalDateTime());
          }
          Company company = new Company();
          company = companyDao.getCompany(rs1.getLong("company_id"));
          if(company != null) {
            computer.setCompany(company);
          }

          computers.add(computer);
        }
        rs1.close();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } finally {
        mySQLConnector.close();
      }
      return computers;
    }

    return null;
  }
}
