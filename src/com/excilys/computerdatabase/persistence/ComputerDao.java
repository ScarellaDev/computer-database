package com.excilys.computerdatabase.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import com.excilys.computerdatabase.exception.InvalidArgsNumberException;
import com.excilys.computerdatabase.exception.InvalidCompanyIdException;
import com.excilys.computerdatabase.model.Company;
import com.excilys.computerdatabase.model.Computer;
import com.excilys.computerdatabase.service.CompanyService;

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
      String sqlRequest = "SELECT * FROM `computer-database-db`.computer WHERE id=" + id + ";";
      ResultSet rs1 = mySQLConnector.selectRequest(sqlRequest);
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
          if (company != null) {
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
      String sqlRequest = "SELECT * FROM `computer-database-db`.computer;";
      ResultSet rs1 = mySQLConnector.selectRequest(sqlRequest);
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
          if (company != null) {
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

  public Boolean setComputer(String[] args) throws InvalidArgsNumberException,
      InvalidCompanyIdException {
    if (args.length > 1) {
      if (args.length < 6) {

        Long id;
        if (args[0].matches("[0-9]+")) {
          id = new Long(args[0]);
          Long max = getLastId();
          if (id < 1 || id > max) {
            throw new InvalidCompanyIdException(
                "The first argument must be a positive integer between [1, " + max.toString() + "]");
          }
        } else {
          throw new InvalidCompanyIdException(
              "The first argument must contains digits only and be a positive integer");
        }

        String name;
        if (!args[1].toLowerCase().equals("null")) {
          name = args[1];
        } else {
          name = null;
        }

        LocalDateTime introduced;
        if (args.length > 2 && !args[2].toLowerCase().equals("null")) {
          StringBuffer introducedS = new StringBuffer(args[2]);
          introducedS.append(" 00:00:00");
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
          try {
            introduced = LocalDateTime.parse(introducedS, formatter);
          } catch (DateTimeParseException e) {
            return false;
          }
        } else {
          introduced = null;
        }

        LocalDateTime discontinued;
        if (args.length > 3 && !args[3].toLowerCase().equals("null")) {
          StringBuffer discontinuedS = new StringBuffer(args[3]);
          discontinuedS.append(" 00:00:00");
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
          try {
            discontinued = LocalDateTime.parse(discontinuedS, formatter);
          } catch (DateTimeParseException e) {
            return false;
          }
        } else {
          discontinued = null;
        }

        Long companyId;
        if (args.length > 4 && !args[4].toLowerCase().equals("null")) {
          if (args[4].matches("[0-9]+")) {
            companyId = new Long(args[4]);
            if (companyId < 1 || companyId > 43) {
              throw new InvalidCompanyIdException(
                  "The fourth argument must be a positive integer between [1, 43]");
            }
          } else {
            throw new InvalidCompanyIdException(
                "The fourth argument must contains digits only and be a positive integer");
          }
        } else {
          companyId = null;
        }
        
        if(!name.toLowerCase().equals("null")) {
          updateValue(id.toString(), "name", name);
        }
        if(introduced!=null) {
          updateValue(id.toString(), "introduced", introduced.toString());
        }
        if(discontinued!=null) {
          updateValue(id.toString(), "discontinued", discontinued.toString());
        }
        if(companyId!=null) {
          updateValue(id.toString(), "company_id", companyId.toString());
        }
        return true;
      } else {
        throw new InvalidArgsNumberException("Too many arguments passed (max = 5)");
      }
    } else {
      throw new InvalidArgsNumberException("Not enough arguments passed (min = 2)");
    }
  }
  
  public Boolean updateValue (String id, String col, String value) {
    if (connect()) {
      StringBuffer sqlUpdate = new StringBuffer(
          "UPDATE `computer-database-db`.`computer` SET `");
      sqlUpdate.append(col);
      sqlUpdate.append("`='");
      sqlUpdate.append(value);
      sqlUpdate.append("' WHERE `id`='");
      sqlUpdate.append(id);
      sqlUpdate.append("';");
      int rs1 = mySQLConnector.updateRequest(sqlUpdate.toString());
      if (rs1 == 0) {
        mySQLConnector.close();
        return false;
      } else {
        mySQLConnector.close();
        return true;
      }
    }
    return false;
  }

  public Boolean addComputer(String[] args) throws InvalidArgsNumberException,
      InvalidCompanyIdException {
    if (args.length > 0) {
      if (args.length < 5) {

        Long id = getLastId() + 1;

        String name;
        if(args[0].toLowerCase().equals("null")) {
          return false;
        } else {
          name = args[0];
        }

        LocalDateTime introduced;
        if (args.length > 1 && !args[1].toLowerCase().equals("null")) {
          StringBuffer introducedS = new StringBuffer(args[1]);
          introducedS.append(" 00:00:00");
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
          try {
            introduced = LocalDateTime.parse(introducedS, formatter);
          } catch (DateTimeParseException e) {
            return false;
          }
        } else {
          introduced = null;
        }

        LocalDateTime discontinued;
        if (args.length > 2 && !args[2].toLowerCase().equals("null")) {
          StringBuffer discontinuedS = new StringBuffer(args[2]);
          discontinuedS.append(" 00:00:00");
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
          try {
            discontinued = LocalDateTime.parse(discontinuedS, formatter);
          } catch (DateTimeParseException e) {
            return false;
          }
        } else {
          discontinued = null;
        }

        Long companyId;
        if (args.length > 3 && !args[3].toLowerCase().equals("null")) {
          if (args[3].matches("[0-9]+")) {
            companyId = new Long(args[3]);
            if (companyId < 1 || companyId > 43) {
              throw new InvalidCompanyIdException(
                  "The fourth argument must be a positive integer between [1, 43]");
            }
          } else {
            throw new InvalidCompanyIdException(
                "The fourth argument must contains digits only and be a positive integer");
          }
        } else {
          companyId = null;
        }

        if (connect()) {
          StringBuffer sqlUpdate = new StringBuffer(
              "INSERT INTO `computer-database-db`.computer (id,name,introduced,discontinued,company_id) VALUES ('");
          sqlUpdate.append(id).append("',");
          sqlUpdate.append("'").append(name).append("',");
          if (introduced == null) {
            sqlUpdate.append(introduced).append(",");
          } else {
            sqlUpdate.append("'").append(introduced).append("',");
          }
          if (discontinued == null) {
            sqlUpdate.append(discontinued).append(",");
          } else {
            sqlUpdate.append("'").append(discontinued).append("',");
          }
          if (companyId == null) {
            sqlUpdate.append(companyId).append(");");
          } else {
            sqlUpdate.append("'").append(companyId).append("');");
          }

          int rs1 = mySQLConnector.updateRequest(sqlUpdate.toString());
          if (rs1 == 0) {
            mySQLConnector.close();
            return false;
          } else {
            mySQLConnector.close();
            return true;
          }
        } else {
          return false;
        }
      } else {
        throw new InvalidArgsNumberException("Too many arguments passed (max = 4)");
      }
    } else {
      throw new InvalidArgsNumberException("Not enough arguments passed (min = 1)");
    }
  }
  
  public Boolean addComputer(Computer computer) throws InvalidArgsNumberException,
  InvalidCompanyIdException {
    Long id = computer.getId();
    String name = computer.getName();
    LocalDateTime introduced = computer.getIntroduced();
    LocalDateTime discontinued = computer.getDiscontinued();
    Long companyId = computer.getCompany().getId();

    if (connect()) {
      StringBuffer sqlUpdate = new StringBuffer(
          "INSERT INTO `computer-database-db`.computer (id,name,introduced,discontinued,company_id) VALUES ('");
      sqlUpdate.append(id).append("',");
      sqlUpdate.append("'").append(name).append("',");
      if (introduced == null) {
        sqlUpdate.append(introduced).append(",");
      } else {
        sqlUpdate.append("'").append(introduced).append("',");
      }
      if (discontinued == null) {
        sqlUpdate.append(discontinued).append(",");
      } else {
        sqlUpdate.append("'").append(discontinued).append("',");
      }
      if (companyId == null) {
        sqlUpdate.append(companyId).append(");");
      } else {
        sqlUpdate.append("'").append(companyId).append("');");
      }

      int rs1 = mySQLConnector.updateRequest(sqlUpdate.toString());
      if (rs1 == 0) {
        mySQLConnector.close();
        return false;
      } else {
        mySQLConnector.close();
        return true;
      }
    } else {
      return false;
    }
}

  public Boolean removeComputer(Long id) {
    if (connect()) {
      StringBuffer sqlUpdate = new StringBuffer(
          "DELETE FROM `computer-database-db`.computer WHERE id=");
      sqlUpdate.append(id).append(";");
      int rs1 = mySQLConnector.updateRequest(sqlUpdate.toString());
      if (rs1 == 0) {
        mySQLConnector.close();
        return false;
      } else {
        mySQLConnector.close();
        return true;
      }
    }
    return false;
  }
  
  public Boolean removeComputer(Computer computer) {
    if (connect()) {
      StringBuffer sqlUpdate = new StringBuffer(
          "DELETE FROM `computer-database-db`.computer WHERE id=");
      sqlUpdate.append(computer.getId().toString()).append(";");
      int rs1 = mySQLConnector.updateRequest(sqlUpdate.toString());
      if (rs1 == 0) {
        mySQLConnector.close();
        return false;
      } else {
        mySQLConnector.close();
        return true;
      }
    }
    return false;
  }

  public Long getLastId() {
    Long result = 0L;

    if (connect()) {
      String sqlRequest = "SELECT MAX(id) AS id FROM `computer-database-db`.computer;";
      ResultSet rs1 = mySQLConnector.selectRequest(sqlRequest);
      try {
        if (rs1.next()) {
          result = rs1.getLong("id");
        }
        rs1.close();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } finally {
        mySQLConnector.close();
      }
    }

    return result;
  }
}
