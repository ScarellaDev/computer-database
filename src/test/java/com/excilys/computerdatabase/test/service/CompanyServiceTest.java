package com.excilys.computerdatabase.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.persistence.CompanyDao;
import com.excilys.computerdatabase.service.CompanyService;
import com.excilys.computerdatabase.test.service.mock.CompanyServiceMock;

/**
 * Test class for the CompanyService
 * 
 * @author Jeremy SCARELLA
 */
@RunWith(MockitoJUnitRunner.class)
public class CompanyServiceTest {
  /*
   * Attributes
   */
  private CompanyService companyService;

  /**
   * Test initialisation using Mockito, creates a mock CompanyDao.
   */
  @Before
  public void init() {
    CompanyDao companyDao = mock(CompanyDao.class);
    when(companyDao.getAll()).thenReturn(new ArrayList<Company>());
    when(companyDao.getById(1L)).thenReturn(Company.builder().id(1L).build());
    companyService = new CompanyServiceMock(companyDao);
  }

  /**
   * Test the getById method. 
   * @result Check if the companies retrieved from database are correct and that method returns null if no company found.
   */
  @Test
  public void getById() {
    assertNull(companyService.getById(0L));
    assertEquals(Company.builder().id(1L).build(), companyService.getById(1L));
  }

  /**
   * Test the getAll method. 
   * @result Check if the companies retrieved from database are correct.
   */
  @Test
  public void getAll() {
    assertEquals(new ArrayList<Company>(), companyService.getAll());
  }
}