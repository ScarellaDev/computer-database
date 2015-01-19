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

@RunWith(MockitoJUnitRunner.class)
public class CompanyServiceTest {

  CompanyService companyService;

  @Before
  public void init() {
    CompanyDao companyDao = mock(CompanyDao.class);
    when(companyDao.getAll()).thenReturn(new ArrayList<Company>());
    when(companyDao.getById(1L)).thenReturn(Company.builder().id(1L).build());
    companyService = new CompanyServiceMock(companyDao);
  }

  @Test
  public void getById() {
    assertNull(companyService.getById(0L));
    assertEquals(Company.builder().id(1L).build(), companyService.getById(1L));
  }

  @Test
  public void getAll() {
    assertEquals(new ArrayList<Company>(), companyService.getAll());
  }
}