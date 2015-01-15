package com.excilys.computerdatabase.test.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.persistence.ComputerDao;
import com.excilys.computerdatabase.test.persistence.mock.ComputerDaoImplSQLMock;
import com.excilys.computerdatabase.test.persistence.mock.UtilDaoSQLMock;

public class ComputerDaoTest {
  ComputerDao    computerDao;
  List<Computer> listComputers;
  List<Company>  listCompanies;

  @Before
  public void init() throws SQLException {
    listCompanies = new ArrayList<Company>();
    listCompanies.add(new Company(1L, "Apple Inc."));
    listCompanies.add(new Company(2L, "Thinking Machines"));

    computerDao = ComputerDaoImplSQLMock.getInstance();
    listComputers = new ArrayList<Computer>();
    listComputers.add(new Computer(1L, "MacBook Pro 15.4 inch", null, null, listCompanies.get(0)));
    listComputers.add(new Computer(2L, "MacBook Pro", LocalDateTime.parse("2006-01-10T00:00:00"),
        null, listCompanies.get(0)));
    listComputers.add(new Computer(3L, "CM-2a", null, null, listCompanies.get(1)));
    listComputers.add(new Computer(4L, "CM-5", LocalDateTime.parse("1991-01-01T00:00:00"), null,
        listCompanies.get(1)));

    Connection connection = null;
    Statement statement = null;
    connection = UtilDaoSQLMock.getConnection();
    statement = connection.createStatement();
    statement.execute("TRUNCATE computer");
    statement
        .execute("INSERT INTO computer (id,name,introduced,discontinued,company_id) VALUES ( 1,'MacBook Pro 15.4 inch',null,null,1);");
    statement
        .execute("INSERT INTO computer (id,name,introduced,discontinued,company_id) VALUES ( 2,'MacBook Pro','2006-01-10',null,1);");
    statement
        .execute("INSERT INTO computer (id,name,introduced,discontinued,company_id) VALUES ( 3,'CM-2a',null,null,2);");
    statement
        .execute("INSERT INTO computer (id,name,introduced,discontinued,company_id) VALUES ( 4,'CM-5','1991-01-01',null,2);");
    UtilDaoSQLMock.close(connection, statement);
  }

  @Test
  public void testGetById() {
    assertEquals(listComputers.get(0), computerDao.getById(1L));
    assertEquals(listComputers.get(1), computerDao.getById(2L));
    assertNull(computerDao.getById(0L));
    assertNull(computerDao.getById(-1L));
    assertNull(computerDao.getById(5L));
  }

  @Test
  public void testGetAll() {
    assertEquals(listComputers, computerDao.getAll());
  }

  @Test
  public void testAddByString() {
    Computer computer = Computer.builder().name("CM-6")
        .introduced(LocalDateTime.parse("1992-01-01T00:00:00")).company(listCompanies.get(1))
        .build();

    String[] params = "CM-6 1992-01-01 null 2".split("\\s+");
    computerDao.addByString(params);
    Long addId = computerDao.getLastId();
    computer.setId(addId);
    assertEquals(computer, computerDao.getById(addId));
    updateByString(addId);
  }

  @Test
  public void testAddByComputer() {
    Computer computer = Computer.builder().name("CM-6")
        .introduced(LocalDateTime.parse("1992-01-01T00:00:00")).company(listCompanies.get(1))
        .build();
    computerDao.addByComputer(computer);
    Long addId = computerDao.getLastId();
    computer.setId(addId);
    assertEquals(computer, computerDao.getById(addId));
    updateByComputer(addId);
  }

  public void updateByString(Long addId) {
    Computer computer = Computer.builder().id(addId).name("CM-6")
        .introduced(LocalDateTime.parse("1993-01-01T00:00:00")).build();

    String[] params = (addId.toString() + " CM-6 1993-01-01 null null").split("\\s+");
    computerDao.updateByString(params);
    assertEquals(computer, computerDao.getById(addId));
    removeByComputer(addId);
  }

  public void updateByComputer(Long addId) {
    Computer computer = Computer.builder().id(addId).name("CM-6")
        .introduced(LocalDateTime.parse("1993-01-01T00:00:00")).build();
    computerDao.updateByComputer(computer);
    assertEquals(computer, computerDao.getById(addId));
    removeByComputer(addId);
  }

  public void removeById(Long addId) {
    assertNotNull(computerDao.getById(addId));
    computerDao.removeById(addId);
    assertNull(computerDao.getById(addId));
  }

  public void removeByComputer(Long addId) {
    Computer computer = Computer.builder().id(addId).name("CM-6")
        .introduced(LocalDateTime.parse("1993-01-01T00:00:00")).build();
    assertNotNull(computerDao.getById(addId));
    computerDao.removeByComputer(computer);
    assertNull(computerDao.getById(addId));
  }
}