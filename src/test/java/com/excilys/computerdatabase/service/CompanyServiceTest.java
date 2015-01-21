package com.excilys.computerdatabase.service;

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
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.persistence.ICompanyDao;
import com.excilys.computerdatabase.service.ICompanyDBService;
import com.excilys.computerdatabase.service.mock.CompanyServiceMock;

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
  private ICompanyDBService companyService;
  Page<Company>          page;
  Page<Company>          pageReturned;

  /**
   * Test initialisation using Mockito, creates a mock CompanyDao.
   */
  @Before
  public void init() {
    ICompanyDao companyDao = mock(ICompanyDao.class);
    page = new Page<Company>();
    page.setNbElementsPerPage(2);
    page.setPageIndex(1);
    pageReturned = new Page<Company>();
    page.setNbElementsPerPage(2);
    page.setPageIndex(1);
    page.setTotalNbElements(20);
    page.setList(new ArrayList<Company>());

    when(companyDao.getAll()).thenReturn(new ArrayList<Company>());
    when(companyDao.getById(1L)).thenReturn(Company.builder().id(1L).build());
    when(companyDao.getPagedList(page)).thenReturn(pageReturned);
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

  /**
   * Test the getPagedList method. 
   * @result Check if the page retrieved from database is correct.
   */
  @Test
  public void getPagedList() {
    assertEquals(pageReturned, companyService.getPagedList(page));
  }
}