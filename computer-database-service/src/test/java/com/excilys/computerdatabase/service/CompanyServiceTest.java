package com.excilys.computerdatabase.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    when(companyRepository.findAll(any(Pageable.class))).thenReturn(page);

    computerList = new ArrayList<Computer>();
    computerList.add(new Computer(1L, "ordi 1", null, null, c1));
    computerList.add(new Computer(2L, "ordi 2", null, null, c1));
    computerList.add(new Computer(3L, "ordi 3", null, null, c2));

    when(companyRepository.findAll()).thenReturn(companyList);

    doAnswer(new Answer<Boolean>() {
      @Override
      public Boolean answer(final InvocationOnMock invocation) {
        final Long l = (Long) invocation.getArguments()[0];
        Boolean b = false;
        for (int i = 0; i < companyList.size(); i++) {
          if (companyList.get(i).getId().equals(l)) {
            b = true;
          }
        }
        return b;
      }
    }).when(companyRepository).exists(anyLong());

    doAnswer(new Answer<Boolean>() {
      @Override
      public Boolean answer(final InvocationOnMock invocation) {
        final Long l = (Long) invocation.getArguments()[0];
        Boolean b = false;
        for (int i = 0; i < computerList.size(); i++) {
          if (computerList.get(i).getId().equals(l)) {
            b = true;
          }
        }
        return b;
      }
    }).when(computerRepository).exists(anyLong());

    doAnswer(new Answer<Company>() {
      @Override
      public Company answer(final InvocationOnMock invocation) {
        final long l = (Long) invocation.getArguments()[0];
        List<Company> comp = companyList.stream().filter(c -> c.getId() == l)
            .collect(Collectors.toList());
        if (comp.isEmpty()) {
          return null;
        } else {
          return comp.get(0);
        }
      }
    }).when(companyRepository).findOne(anyLong());

    doAnswer(new Answer<List<Company>>() {
      @Override
      public List<Company> answer(final InvocationOnMock invocation) {
        final Long l = (Long) invocation.getArguments()[0];
        companyList.removeIf(c -> c.getId().equals(l));
        return null;
      }
    }).when(companyRepository).delete(anyLong());

    doAnswer(new Answer<List<Computer>>() {
      @Override
      public List<Computer> answer(final InvocationOnMock invocation) {
        final Long l = (Long) invocation.getArguments()[0];
        computerList.removeIf(c -> c.getCompany().getId().equals(l));
        return null;
      }
    }).when(computerRepository).deleteByCompanyId(anyLong());

    doAnswer(new Answer<List<Computer>>() {
      @Override
      public List<Computer> answer(final InvocationOnMock invocation) {
        final Long l = (Long) invocation.getArguments()[0];
        List<Computer> list = new ArrayList<Computer>();
        for (int i = 0; i < computerList.size(); i++) {
          if (computerList.get(i).getCompany().getId().equals(l)) {
            list.add(computerList.get(i));
          }
        }
        return list;
      }
    }).when(computerRepository).findByCompanyId(anyLong());

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
  public void getByIdNull() {
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

    assertEquals(x - 1, companyList.size());
    assertEquals(y - 2, computerList.size());
  }

  @Test
  public void removeByIdInvalid() {
    final long id = 3L;
    final int computerCount = computerList.size();

    companyService.removeById(id);

    assertNull(companyRepository.findOne(id));
    assertEquals(computerCount, computerList.size());
  }

  @Test
  public void removeByIdNull() {
    final Long id = null;
    final int computerCount = computerList.size();

    companyService.removeById(id);

    assertNull(companyRepository.findOne(id));
    assertEquals(computerCount, computerList.size());
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
