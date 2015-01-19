package com.excilys.computerdatabase.test.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.persistence.CompanyDao;
import com.excilys.computerdatabase.test.persistence.mock.CompanyDaoImplSQLMock;
import com.excilys.computerdatabase.test.persistence.mock.UtilDaoSQLMock;

/**
 * Test class for the CompanyDao
 * 
 * @author Jeremy SCARELLA
 */
public class CompanyDaoTest {
  /*
   * Attributes
   */
  private CompanyDao    companyDao;
  private List<Company> list;

  /**
   * Test initialisation, creates two companies for testing and get a CompanyDaoImplSQLMock instance.
   * @throws SQLException
   */
  @Before
  public void init() throws SQLException {
    companyDao = CompanyDaoImplSQLMock.getInstance();
    list = new ArrayList<Company>();
    list.add(new Company(1L, "Apple Inc."));
    list.add(new Company(2L, "Thinking Machines"));

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
   * @result Check if the companies retrieved from database are correct and that method returns null if no company found.
   */
  @Test
  public void testGetById() {
    assertEquals(new Company(1L, "Apple Inc."), companyDao.getById(1L));
    assertEquals(new Company(2L, "Thinking Machines"), companyDao.getById(2L));
    assertNull(companyDao.getById(-1L));
    assertNull(companyDao.getById(3L));
  }

  /**
   * Test the getAll method. 
   * @result Check if the companies retrieved from database are correct.
   */
  @Test
  public void testGetAll() {
    assertEquals(list, companyDao.getAll());
  }

  /**
   * Test the getPagedList method. 
   * @result Check if the page retrieved from database is correct.
   */
  @Test
  public void getPagedList() {
    final Page<Company> page = new Page<Company>();
    page.setNbElementsPerPage(20);
    page.setPageIndex(1);
    final Page<Company> pageReturned = new Page<Company>();
    pageReturned.setNbElementsPerPage(2);
    pageReturned.setPageIndex(1);
    pageReturned.setTotalNbElements(2);
    pageReturned.setTotalNbPages(1);
    pageReturned.setList(list);
    assertEquals(pageReturned, companyDao.getPagedList(page));
  }
}