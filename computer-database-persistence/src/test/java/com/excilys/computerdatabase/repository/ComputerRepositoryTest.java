package com.excilys.computerdatabase.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.persistence.repository.ComputerRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/persistence-test-context.xml" })
@Transactional
public class ComputerRepositoryTest {

  private static final String FILE     = "src/test/resources/scripts/setDBComputerTest.sql";

  @Autowired
  ComputerRepository          computerRepository;

  @Autowired
  DataSource                  dataSource;

  List<Computer>              list;
  Company                     apple    = new Company(1L, "Apple Inc.");
  Company                     thinking = new Company(2L, "Thinking Machines");

  Connection                  connection;

  @BeforeTransaction
  public void initTransaction() throws SQLException, FileNotFoundException {
    list = new ArrayList<Computer>();
    list.add(new Computer(1L, "MacBook Pro 15.4 inch", null, null, apple));
    list.add(new Computer(2L, "MacBook Pro", LocalDate.parse("2006-01-10"), null, apple));
    list.add(new Computer(3L, "CM-2a", null, null, thinking));
    list.add(new Computer(4L, "CM-200", null, null, thinking));

    FileReader reader = new FileReader(FILE);

    connection = DataSourceUtils.getConnection(dataSource);
    ScriptRunner scriptRunner = new ScriptRunner(connection);
    scriptRunner.runScript(reader);
  }

  @AfterTransaction
  public void afterTransaction() throws SQLException {
    connection.close();
  }

  /*
   * Tests of the findOne function
   */
  @Test
  public void findOneValid() {
    assertEquals(list.get(0), computerRepository.findOne(1L));
  }

  @Test
  public void findOneInvalid() {
    assertNull(computerRepository.findOne(5L));
    assertNull(computerRepository.findOne(0L));
    assertNull(computerRepository.findOne(-1L));
  }

  /*
   * Tests of the findAll function
   */
  @Test
  public void findAll() {
    assertEquals(list, computerRepository.findAll());
  }

  /*
   * Tests of the add function
   */
  @Test
  public void add() {
    final Computer computer = Computer.builder().name("test")
        .introduced(LocalDate.parse("1993-01-10")).company(apple).build();

    computerRepository.save(computer);
    computer.setId(5L);
    assertEquals(computer, computerRepository.findOne(5L));
  }

  @Test(expected = InvalidDataAccessApiUsageException.class)
  public void addNull() {
    Computer computer = null;
    computerRepository.save(computer);
  }

  @Test
  public void addEmptyComputer() {
    computerRepository.save(new Computer());
    list.add(Computer.builder().id(5L).build());
    assertEquals(list, computerRepository.findAll());
  }

  /*
   * Tests of the update function
   */
  @Test
  public void update() {
    final Computer computer = Computer.builder().id(2L).name("test")
        .introduced(LocalDate.parse("1993-01-12")).build();
    computerRepository.save(computer);
    assertEquals(computer, computerRepository.findOne(2L));
  }

  @Test(expected = InvalidDataAccessApiUsageException.class)
  public void updateNull() {
    Computer computer = null;
    computerRepository.save(computer);
  }

  @Test
  public void updateInvalidId() {
    final Computer computer = new Computer();
    computer.setId(-1L);
    computerRepository.save(computer);
    computer.setId(5L);
    list.add(computer);
    assertEquals(list, computerRepository.findAll());
  }

  @Test(expected = JpaObjectRetrievalFailureException.class)
  public void updateInvalidCompanyId() {
    final Computer computer = new Computer();
    computer.setId(1L);
    computer.setCompany(new Company(-1L, ""));
    computerRepository.save(computer);
  }

  /*
   * Tests of the remove function
   */
  @Test
  public void remove() {
    assertNotNull(computerRepository.findOne(2L));
    computerRepository.delete(2L);
    assertNull(computerRepository.findOne(2L));
  }

  @Test(expected = EmptyResultDataAccessException.class)
  public void removeInvalidId() {
    computerRepository.delete(-1L);
  }

  /*
   * Tests of the deleteByCompanyId function
   */
  @Test
  public void removeByCompanyId() {
    computerRepository.deleteByCompanyId(2L);
    assertNull(computerRepository.findOne(3L));
    assertNull(computerRepository.findOne(4L));
  }

  @Test
  public void DeleteCompanyInvalid() {
    computerRepository.deleteByCompanyId(-2L);
    assertEquals(list, computerRepository.findAll());
  }

  /*
   * Tests of the getPagedList function
   */
  @Test
  public void pagedResult() {
    Pageable pageable = new PageRequest(0, 20);
    Page<Computer> page = new PageImpl<Computer>(list, pageable, list.size());

    assertEquals(page,
        computerRepository.findByNameStartingWithOrCompanyNameStartingWith("", "", pageable));
  }

  @Test
  public void pagedResultNull() {
    Page<Computer> page = new PageImpl<Computer>(list);
    assertEquals(page,
        computerRepository.findByNameStartingWithOrCompanyNameStartingWith("", "", null));
  }

  @Test(expected = PropertyReferenceException.class)
  public void invalidSort() {
    Pageable pageable = new PageRequest(0, 20, new Sort(Direction.ASC, "x"));
    computerRepository.findByNameStartingWithOrCompanyNameStartingWith("", "", pageable);
  }
}
