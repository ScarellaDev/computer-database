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
import org.springframework.beans.factory.annotation.Autowired;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.dto.ComputerDto;
import com.excilys.computerdatabase.dto.ComputerDtoConverter;
import com.excilys.computerdatabase.exception.PersistenceException;
import com.excilys.computerdatabase.persistence.impl.ComputerDaoSQL;

/**
 * Test class for the ComputerDao
 * 
 * @author Jeremy SCARELLA
 */
public class ComputerDaoTest {

  @Autowired
  ConnectionManager cm;
  @Autowired
  IComputerDao      computerDao;
  List<Computer>    list;
  Company           apple    = new Company(1L, "Apple Inc.");
  Company           thinking = new Company(2L, "Thinking Machines");

  @Before
  public void init() throws SQLException {
    computerDao = new ComputerDaoSQL();
    list = new ArrayList<Computer>();
    list.add(new Computer(1L, "MacBook Pro 15.4 inch", null, null, apple));
    list.add(new Computer(2L, "MacBook Pro", LocalDateTime.parse("2006-01-10 00:00:00"), null,
        apple));
    list.add(new Computer(3L, "CM-2a", null, null, thinking));
    list.add(new Computer(4L, "CM-200", null, null, thinking));

    final Connection connection = cm.getConnection();

    final Statement stmt = connection.createStatement();
    stmt.execute("drop table if exists computer;");
    stmt.execute("drop table if exists company;");
    stmt.execute("create table company (id bigint not null auto_increment, name varchar(255), "
        + "constraint pk_company primary key (id));");
    stmt.execute("create table computer (id bigint not null auto_increment,name varchar(255), "
        + "introduced timestamp NULL, discontinued timestamp NULL,"
        + "company_id bigint default NULL," + "constraint pk_computer primary key (id));");
    stmt.execute("alter table computer add constraint fk_computer_company_1 foreign key (company_id)"
        + " references company (id) on delete restrict on update restrict;");
    stmt.execute("create index ix_computer_company_1 on computer (company_id);");

    stmt.execute("insert into company (id,name) values (  1,'Apple Inc.');");
    stmt.execute("insert into company (id,name) values (  2,'Thinking Machines');");

    stmt.execute("insert into computer (id,name,introduced,discontinued,company_id) values (  1,'MacBook Pro 15.4 inch',null,null,1);");
    stmt.execute("insert into computer (id,name,introduced,discontinued,company_id) values (  2,'MacBook Pro','2006-01-10',null,1);");
    stmt.execute("insert into computer (id,name,introduced,discontinued,company_id) values (  3,'CM-2a',null,null,2);");
    stmt.execute("insert into computer (id,name,introduced,discontinued,company_id) values (  4,'CM-200',null,null,2);");

    cm.close(stmt);
    cm.closeConnection();
  }

  /*
   * Tests of the getAll function
   */
  @Test
  public void getAll() {
    assertEquals(list, computerDao.getAll());
  }

  /*
   * Tests of the getById function
   */
  @Test
  public void getByIdValid() {
    assertEquals(list.get(0), computerDao.getById(1L));
  }

  public void getByIdInvalid() {
    assertNull(computerDao.getById(5L));
    assertNull(computerDao.getById(0L));
    assertNull(computerDao.getById(-1L));
  }

  /*
   * Tests of the getPagedList function
   */
  @Test
  public void getPagedList() {
    final Page<ComputerDto> page = new Page<ComputerDto>();
    page.setNbElementsPerPage(20);
    page.setPageIndex(1);

    final Page<ComputerDto> pageReturned = new Page<ComputerDto>();
    pageReturned.setNbElementsPerPage(20);
    pageReturned.setPageIndex(1);
    pageReturned.setTotalNbElements(list.size());
    pageReturned.setTotalNbPages(1);
    pageReturned.setList(ComputerDtoConverter.toDto(list));

    assertEquals(pageReturned, computerDao.getPagedList(page));
  }

  @Test
  public void getPagedListNull() {
    assertNull(computerDao.getPagedList(null));
  }

  @Test(expected = PersistenceException.class)
  public void invalidOrder() {
    final Page<ComputerDto> page = new Page<ComputerDto>();
    page.setOrder("x");
    computerDao.getPagedList(page);
  }

  @Test(expected = PersistenceException.class)
  public void invalidPageNumber() {
    final Page<ComputerDto> page = new Page<ComputerDto>();
    page.setPageIndex(-1);
    computerDao.getPagedList(page);
  }

  @Test(expected = PersistenceException.class)
  public void invalidResultsPerPage() {
    final Page<ComputerDto> page = new Page<ComputerDto>();
    page.setNbElementsPerPage(-1);
    computerDao.getPagedList(page);
  }

  /*
   * Tests of the create function
   */
  @Test
  public void create() {
    final Computer computer = Computer.builder().name("test")
        .introduced(LocalDateTime.parse("1993-01-10 00:00:00")).company(apple).build();

    computerDao.addByComputer(computer);
    computer.setId(5L);
    assertEquals(computer, computerDao.getById(5L));
  }

  @Test
  public void createNull() {
    computerDao.addByComputer(null);
    assertEquals(list, computerDao.getAll());
  }

  @Test
  public void createEmptyComputer() {
    computerDao.addByComputer(new Computer());
    assertEquals(list, computerDao.getAll());
  }

  /*
   * Tests of the update function
   */
  @Test
  public void update() {
    final Computer computer = Computer.builder().id(2L).name("test")
        .introduced(LocalDateTime.parse("1993-01-12 00:00:00")).build();
    computerDao.updateByComputer(computer);
    assertEquals(computer, computerDao.getById(2L));
  }

  @Test
  public void updateNull() {
    computerDao.updateByComputer(null);
    assertEquals(list, computerDao.getAll());
  }

  @Test
  public void updateInvalidId() {
    final Computer computer = new Computer();
    computer.setId(-1L);
    computerDao.updateByComputer(computer);
    assertEquals(list, computerDao.getAll());
  }

  @Test(expected = PersistenceException.class)
  public void updateInvalidCompanyId() {
    final Computer computer = new Computer();
    computer.setId(1L);
    computer.setCompany(new Company(-1L, ""));
    computerDao.updateByComputer(computer);
  }

  /*
   * Tests of the delete function
   */
  @Test
  public void delete() {
    assertNotNull(computerDao.getById(2L));
    computerDao.removeById(2L);
    assertNull(computerDao.getById(2L));
  }

  @Test
  public void deleteInvalidId() {
    computerDao.removeById(-1L);
    assertEquals(list, computerDao.getAll());
  }

  /*
   * Tests of the deleteByCompanyId function
   */
  //  @Test
  //  public void deleteByCompanyId() throws SQLException {
  //    final ConnectionManager cm = new ConnectionManager();
  //    cm.startTransaction();
  //    cm.getConnection();
  //    computerDao.removeByCompanyId(2L);
  //    cm.commit();
  //    cm.closeConnection();
  //
  //    assertTrue(computerDao.getByCompanyId(2L).isEmpty());
  //  }

  @Test
  public void DeleteCompanyInvalid() throws SQLException {
    cm.startTransaction();
    cm.getConnection();
    computerDao.removeByCompanyId(-2L);
    cm.commit();
    cm.closeConnection();

    assertEquals(list, computerDao.getAll());
  }

  /*
   * Test of the delete(List) function
   */
  @Test
  public void multipleDelete() {
    final List<Long> l = new ArrayList<Long>();
    l.add(1L);
    l.add(2L);
    l.forEach(id -> assertNotNull(computerDao.getById(id)));
    computerDao.removeByIdList(l);
    l.forEach(id -> assertNull(computerDao.getById(id)));
  }
}
