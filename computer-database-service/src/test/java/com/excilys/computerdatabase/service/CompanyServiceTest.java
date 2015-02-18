package com.excilys.computerdatabase.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.persistence.repository.CompanyRepository;
import com.excilys.computerdatabase.persistence.repository.ComputerRepository;
import com.excilys.computerdatabase.service.impl.CompanyService;

@RunWith(MockitoJUnitRunner.class)
public class CompanyServiceTest {

  @InjectMocks
  CompanyService     companyService;

  CompanyRepository  companyRepository;
  ComputerRepository computerRepository;

  Page<Company>      page;
  Pageable           pageable;

  List<Computer>     computerList;
  List<Company>      companyList;
  Company            c1;
  Company            c2;

  @Before
  public void init() {
    companyRepository = mock(CompanyRepository.class);
    computerRepository = mock(ComputerRepository.class);

    companyList = new ArrayList<Company>();
    c1 = new Company(1L, "company 1");
    c2 = new Company(2L, "company 2");
    companyList.add(c1);
    companyList.add(c2);

    pageable = new PageRequest(0, 5);

    page = new PageImpl<Company>(companyList, pageable, companyList.size());

    computerList = new ArrayList<Computer>();
    computerList.add(new Computer(1L, "ordi 1", null, null, c1));
    computerList.add(new Computer(2L, "ordi 2", null, null, c1));
    computerList.add(new Computer(3L, "ordi 3", null, null, c2));

    when(companyRepository.findAll()).thenReturn(companyList);
    when(companyRepository.findOne(1L)).thenReturn(c1);

    doAnswer(new Answer<List<Company>>() {
      @Override
      public List<Company> answer(final InvocationOnMock invocation) {
        final Long l = (Long) invocation.getArguments()[0];
        companyList.removeIf(c -> c.getId() == l);
        return null;
      }
    }).when(companyRepository).delete(anyLong());

    doAnswer(new Answer<List<Computer>>() {
      @Override
      public List<Computer> answer(final InvocationOnMock invocation) {
        final Long l = (Long) invocation.getArguments()[0];
        computerList.removeIf(c -> c.getCompany().getId() == l);
        return null;
      }
    }).when(computerRepository).deleteByCompanyId(anyLong());

    MockitoAnnotations.initMocks(this);
  }

  /*
   * Tests getById function
   */
  @Test
  public void getById() {
    assertEquals(c1, companyService.getById(1L));
  }

  @Test
  public void getByIdInvalid() {
    assertNull(companyService.getById(-1L));
  }

  @Test
  public void getByNullId() {
    assertNull(companyService.getById(null));
  }

  /*
   * Test getAll function
   */
  @Test
  public void getAll() {
    assertEquals(companyList, companyService.getAll());
  }

  /*
   * Tests remove function
   */
  @Test
  public void removeById() {
    final int x = companyList.size();
    final int y = computerList.size();
    companyService.removeById(1L);

    assertEquals((x - 1), companyList.size());
    assertEquals(y - 2, computerList.size());
  }

  @Test
  public void removeByInvalidId() {
    final int x = companyList.size();
    final int y = computerList.size();
    companyService.removeById(3L);

    assertEquals(x, companyList.size());
    assertEquals(y, computerList.size());
  }

  @Test
  public void removeByNullId() {
    final int x = companyList.size();
    final int y = computerList.size();
    companyService.removeById(null);

    assertEquals(x, companyList.size());
    assertEquals(y, computerList.size());
  }

  /*
   * Tests getPagedList
   */
  @Test
  public void getPagedList() {
    assertEquals(page, companyService.getPagedList(pageable));
  }

  @Test
  public void getPagedListNull() {
    assertNull(companyService.getPagedList(null));
  }
}
