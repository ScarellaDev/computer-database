package com.excilys.computerdatabase.persistence;

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
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.persistence.IComputerDao;
import com.excilys.computerdatabase.persistence.mock.ComputerDaoImplSQLMock;
import com.excilys.computerdatabase.persistence.mock.UtilDaoSQLMock;

/**
 * Test class for the ComputerDao
 * 
 * @author Jeremy SCARELLA
 */
public class ComputerDaoTest {
  /*
   * Attributes
   */
  private IComputerDao    computerDao;
  private List<Computer> listComputers;
  private List<Company>  listCompanies;

  /**
   * Test initialisation, creates two companies and four computers for testing, get a ComputerDaoImplSQLMock instance.
   * It also clears a mock DB and inserts the newly created computers in it. 
   * @throws SQLException
   */
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
    statement.execute("drop table if exists computer;");
    statement.execute("drop table if exists company;");
    statement
        .execute("create table company (id bigint not null auto_increment, name varchar(255), "
            + "constraint pk_company primary key (id));");
    statement
        .execute("create table computer (id bigint not null auto_increment,name varchar(255), "
            + "introduced timestamp NULL, discontinued timestamp NULL,"
            + "company_id bigint default NULL," + "constraint pk_computer primary key (id));");
    statement
        .execute("alter table computer add constraint fk_computer_company_1 foreign key (company_id)"
            + " references company (id) on delete restrict on update restrict;");
    statement.execute("create index ix_computer_company_1 on computer (company_id);");
    statement.execute("insert into company (id,name) values ( 1,'Apple Inc.');");
    statement.execute("insert into company (id,name) values ( 2,'Thinking Machines');");
    statement
        .execute("insert into computer (id,name,introduced,discontinued,company_id) values ( 1,'MacBook Pro 15.4 inch',null,null,1);");
    statement
        .execute("insert into computer (id,name,introduced,discontinued,company_id) values ( 2,'MacBook Pro','2006-01-10',null,1);");
    statement
        .execute("insert into computer (id,name,introduced,discontinued,company_id) values ( 3,'CM-2a',null,null,2);");
    statement
        .execute("insert into computer (id,name,introduced,discontinued,company_id) values ( 4,'CM-5','1991-01-01',null,2);");
    UtilDaoSQLMock.close(connection, statement);
  }

  /**
   * Test the getById method. 
   * @result Check if the computers retrieved from database are correct and that method returns null if no computer found.
   */
  @Test
  public void testGetById() {
    assertEquals(listComputers.get(0), computerDao.getById(1L));
    assertEquals(listComputers.get(1), computerDao.getById(2L));
    assertNull(computerDao.getById(0L));
    assertNull(computerDao.getById(-1L));
    assertNull(computerDao.getById(5L));
  }

  /**
   * Test the getAll method. 
   * @result Check if the companies retrieved from database are correct.
   */
  @Test
  public void testGetAll() {
    assertEquals(listComputers, computerDao.getAll());
  }

  /**
   * Test the addByString method.
   * @result Check if the INSERT SQL statement is executed properly using a String table as parameter
   */
  @Test
  public void testAddByString() {
    Computer computer = Computer.builder().name("CM-6")
        .introduced(LocalDateTime.parse("1992-01-01T00:00:00")).company(listCompanies.get(1))
        .build();

    String[] params = "CM-6 1992-01-01 null 2".split("\\s+");
    computerDao.addByString(params);
    computer.setId(5L);
    assertEquals(computer, computerDao.getById(5L));
  }

  /**
   * Test the addByComputer method.
   * @result Check if the INSERT SQL statements are executed properly using a Computer instance as parameter
   */
  @Test
  public void testAddByComputer() {
    Computer computer = Computer.builder().name("CM-6")
        .introduced(LocalDateTime.parse("1992-01-01T00:00:00")).company(listCompanies.get(1))
        .build();
    computerDao.addByComputer(computer);
    computer.setId(5L);
    assertEquals(computer, computerDao.getById(5L));
  }

  /**
   * Test the updateByString method.
   * @result Check if the UPDATE SQL statement is executed properly using a String table as parameter
   */
  @Test
  public void updateByString() {
    Computer computer = Computer.builder().id(4L).name("CM-6")
        .introduced(LocalDateTime.parse("1993-01-01T00:00:00")).build();

    String[] params = ("4 CM-6 1993-01-01 null null").split("\\s+");
    computerDao.updateByString(params);
    assertEquals(computer, computerDao.getById(4L));
  }

  /**
   * Test the updateByComputer method.
   * @result Check if the UPDATE SQL statements are executed properly using a Computer instance as parameter
   */
  @Test
  public void updateByComputer() {
    Computer computer = Computer.builder().id(4L).name("CM-6")
        .introduced(LocalDateTime.parse("1993-01-01T00:00:00")).build();
    computerDao.updateByComputer(computer);
    assertEquals(computer, computerDao.getById(4L));
  }

  /**
   * Test the removeById method.
   * @result Check if the DELETE SQL statement is executed properly using a Long id as parameter
   */
  @Test
  public void removeById() {
    assertNotNull(computerDao.getById(4L));
    computerDao.removeById(4L);
    assertNull(computerDao.getById(4L));
  }

  /**
   * Test the removeByComputer method.
   * @result Check if the DELETE SQL statements are executed properly using a Computer instance as parameter
   */
  @Test
  public void removeByComputer() {
    Computer computer = Computer.builder().id(4L).build();
    assertNotNull(computerDao.getById(4L));
    computerDao.removeByComputer(computer);
    assertNull(computerDao.getById(4L));
  }

  /**
   * Test the getPagedList method. 
   * @result Check if the page retrieved from database is correct.
   */
  @Test
  public void getPagedList() {
    final Page<Computer> page = new Page<Computer>();
    page.setNbElementsPerPage(20);
    page.setPageIndex(1);
    final Page<Computer> pageReturned = new Page<Computer>();
    pageReturned.setNbElementsPerPage(4);
    pageReturned.setPageIndex(1);
    pageReturned.setTotalNbElements(4);
    pageReturned.setTotalNbPages(1);
    pageReturned.setList(listComputers);
    assertEquals(pageReturned, computerDao.getPagedList(page));
  }
}