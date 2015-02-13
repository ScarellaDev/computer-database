package com.excilys.computerdatabase.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.computerdatabase.domain.Company;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class CompanyRepositoryTest {

  @Autowired
  CompanyRepository companyRepository;

  List<Company>     list;

  @Autowired
  DataSource        dataSource;

  @Before
  public void init() throws SQLException {
    list = new ArrayList<Company>();
    list.add(new Company(1L, "Apple Inc."));
    list.add(new Company(2L, "Thinking Machines"));

    final Connection connection = DataSourceUtils.getConnection(dataSource);

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
    connection.close();
  }

  /*
   * Tests of the findAll function
   */
  @Test
  public void findAll() {
    assertEquals(list, companyRepository.findAll());
  }

  /*
   * Tests of the findOne function
   */
  @Test
  public void findOne() {
    assertEquals(new Company(1L, "Apple Inc."), companyRepository.findOne(1L));
  }

  @Test
  public void findOneInvalid() {
    assertNull(companyRepository.findOne(3L));
    assertNull(companyRepository.findOne(-1L));
  }

  /*
   * NEEDS TO BE UPDATED
   */
  //  /*
  //   * Tests of the getPagedList function
  //   */
  //  @Test
  //  public void getPagedList() {
  //    final Page<Company> page = new Page<Company>();
  //    page.setNbElementsPerPage(20);
  //    page.setPageIndex(1);
  //
  //    final Page<Company> pageReturned = new Page<Company>();
  //    pageReturned.setNbElementsPerPage(20);
  //    pageReturned.setPageIndex(1);
  //    pageReturned.setTotalNbElements(2);
  //    pageReturned.setTotalNbPages(1);
  //    pageReturned.setList(list);
  //    assertEquals(pageReturned, companyRepository.getPagedList(page));
  //  }
  //
  //  @Test
  //  public void getPagedListNull() {
  //    assertNull(companyRepository.getPagedList(null));
  //  }
  //
  //  @Test(expected = PersistenceException.class)
  //  public void invalidPageNumber() {
  //    final Page<Company> page = new Page<Company>();
  //    page.setPageIndex(-1);
  //    companyRepository.getPagedList(page);
  //  }
  //
  //  @Test(expected = PersistenceException.class)
  //  public void invalidResultsPerPage() {
  //    final Page<Company> page = new Page<Company>();
  //    page.setNbElementsPerPage(-1);
  //    companyRepository.getPagedList(page);
  //  }

  /*
   * Tests of the delete function
   */
  @Test
  public void remove() {
    companyRepository.delete(2L);
    assertNull(companyRepository.findOne(2L));
  }

  @Test
  public void removeInvalidId() {
    companyRepository.delete(-1L);
    assertEquals(list, companyRepository.findAll());
  }
}
