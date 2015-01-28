package com.excilys.computerdatabase.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.exception.PersistenceException;

/**
 * Test class for the CompanyDao
 * 
 * @author Jeremy SCARELLA
 */
public class CompanyDaoTest {

  @Autowired
  ICompanyDao       companyDao;
  @Autowired
  ConnectionManager cm;
  List<Company>     list;

  @Before
  public void init() throws SQLException {
    list = new ArrayList<Company>();
    list.add(new Company(1L, "Apple Inc."));
    list.add(new Company(2L, "Thinking Machines"));
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
    cm.close(stmt);
    cm.closeConnection();
  }

  /*
   * Tests of the getAll function
   */
  @Test
  public void getAll() {
    assertEquals(list, companyDao.getAll());
  }

  /*
   * Tests of the getById function
   */
  @Test
  public void getById() {
    assertEquals(new Company(1L, "Apple Inc."), companyDao.getById(1L));
  }

  @Test
  public void getByIdInvalid() {
    assertNull(companyDao.getById(3L));
    assertNull(companyDao.getById(-1L));
  }

  /*
   * Tests of the getPagedList function
   */
  @Test
  public void getPagedList() {
    final Page<Company> page = new Page<Company>();
    page.setNbElementsPerPage(20);
    page.setPageIndex(1);

    final Page<Company> pageReturned = new Page<Company>();
    pageReturned.setNbElementsPerPage(20);
    pageReturned.setPageIndex(1);
    pageReturned.setTotalNbElements(2);
    pageReturned.setTotalNbPages(1);
    pageReturned.setList(list);
    assertEquals(pageReturned, companyDao.getPagedList(page));
  }

  @Test
  public void getPagedListNull() {
    assertNull(companyDao.getPagedList(null));
  }

  @Test(expected = PersistenceException.class)
  public void invalidPageNumber() {
    final Page<Company> page = new Page<Company>();
    page.setPageIndex(-1);
    companyDao.getPagedList(page);
  }

  @Test(expected = PersistenceException.class)
  public void invalidResultsPerPage() {
    final Page<Company> page = new Page<Company>();
    page.setNbElementsPerPage(-1);
    companyDao.getPagedList(page);
  }

  /*
   * Tests of the delete function
   */
  @Test
  public void delete() {
    cm.getConnection();
    companyDao.removeById(2L);;
    cm.closeConnection();
    assertNull(companyDao.getById(2L));
  }

  @Test
  public void deleteInvalidId() {
    cm.getConnection();
    companyDao.removeById(-1L);
    cm.closeConnection();
    assertEquals(list, companyDao.getAll());
  }

  @Test(expected = PersistenceException.class)
  public void deleteComputerLeft() {
    cm.getConnection();
    companyDao.removeById(1L);
    cm.closeConnection();
  }
}
