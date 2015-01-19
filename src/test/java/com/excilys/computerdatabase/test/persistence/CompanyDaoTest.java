package com.excilys.computerdatabase.test.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.persistence.CompanyDao;
import com.excilys.computerdatabase.test.persistence.mock.CompanyDaoImplSQLMock;

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
}