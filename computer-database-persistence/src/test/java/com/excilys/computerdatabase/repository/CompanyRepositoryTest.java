package com.excilys.computerdatabase.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.persistence.repository.CompanyRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/persistence-test-context.xml" })
public class CompanyRepositoryTest {

  private static final String FILE = "src/test/resources/scripts/setDBCompanyTest.sql";

  @Autowired
  CompanyRepository           companyRepository;
  List<Company>               list;
  @Autowired
  DataSource                  dataSource;

  @Before
  public void init() throws SQLException, FileNotFoundException {
    list = new ArrayList<Company>();
    list.add(new Company(1L, "Apple Inc."));
    list.add(new Company(2L, "Thinking Machines"));

    FileReader reader = new FileReader(FILE);

    final Connection connection = DataSourceUtils.getConnection(dataSource);
    ScriptRunner scriptRunner = new ScriptRunner(connection);
    scriptRunner.runScript(reader);

    connection.close();
  }

  /*
   * Tests of the getById function
   */
  @Test
  public void getById() {
    assertEquals(new Company(1L, "Apple Inc."), companyRepository.findOne(1L));
  }

  @Test
  public void getByIdInvalid() {
    assertNull(companyRepository.findOne(3L));
    assertNull(companyRepository.findOne(-1L));
  }

  @Test(expected = InvalidDataAccessApiUsageException.class)
  public void getByIdNull() {
    assertNull(companyRepository.findOne(null));
  }

  /*
   * Tests of the getAll function
   */
  @Test
  public void getAll() {
    assertEquals(list, companyRepository.findAll());
  }

  /*
   * Tests of the remove function
   */
  @Test
  public void removeById() {
    companyRepository.delete(2L);
    assertNull(companyRepository.findOne(2L));
  }

  @Test(expected = EmptyResultDataAccessException.class)
  public void removeByIdInvalid() {
    companyRepository.delete(-1L);
    assertEquals(list, companyRepository.findAll());
  }

  @Test(expected = InvalidDataAccessApiUsageException.class)
  public void removeByIdNull() {
    Long id = null;
    companyRepository.delete(id);
    assertEquals(list, companyRepository.findAll());
  }

  @Test(expected = DataIntegrityViolationException.class)
  public void removeComputerLeft() {
    companyRepository.delete(1L);
  }

  /*
   * Tests of the getPagedList function
   */
  @Test
  public void pagedResult() {
    Pageable pageable = new PageRequest(0, 20);
    Page<Company> page = new PageImpl<Company>(list, pageable, list.size());

    assertEquals(page, companyRepository.findAll(pageable));
  }

  @Test
  public void pagedResultNull() {
    Page<Company> page = new PageImpl<Company>(list);
    Pageable nullPageable = null;
    assertEquals(page, companyRepository.findAll(nullPageable));
  }

  @Test(expected = PropertyReferenceException.class)
  public void invalidSort() {
    Pageable pageable = new PageRequest(0, 20, new Sort(Direction.ASC, "x"));
    companyRepository.findAll(pageable);
  }
}
