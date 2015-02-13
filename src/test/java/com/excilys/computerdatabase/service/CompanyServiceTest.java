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

  //  Page<Company>      page;
  //  Page<Company>      pageReturned;
  //  Page<Company>      wrongPNumber;
  //  Page<Company>      wrongRPP;
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

    computerList = new ArrayList<Computer>();
    computerList.add(new Computer(1L, "ordi 1", null, null, c1));
    computerList.add(new Computer(2L, "ordi 2", null, null, c1));
    computerList.add(new Computer(3L, "ordi 3", null, null, c2));

    //    page = new Page<Company>();
    //    page.setTotalNbElements(2);
    //    page.setList(companyList);
    //
    //    pageReturned = new Page<Company>();
    //
    //    wrongPNumber = new Page<Company>();
    //    wrongPNumber.setPageIndex(-1);
    //
    //    wrongRPP = new Page<Company>();
    //    wrongRPP.setNbElementsPerPage(-1);

    when(companyRepository.findAll()).thenReturn(companyList);
    when(companyRepository.findOne(1L)).thenReturn(c1);
    //    when(companyRepository.getPagedList(page)).thenReturn(pageReturned);
    //
    //    doThrow(PersistenceException.class).when(companyRepository).getPagedList(wrongPNumber);
    //    doThrow(PersistenceException.class).when(companyRepository).getPagedList(wrongRPP);

    doAnswer(new Answer<List<Computer>>() {
      @Override
      public List<Computer> answer(final InvocationOnMock invocation) {
        final long l = (Long) invocation.getArguments()[0];
        companyList.removeIf(c -> c.getId() == l);
        return null;
      }
    }).when(companyRepository).delete(anyLong());

    doAnswer(new Answer<List<Computer>>() {
      @Override
      public List<Computer> answer(final InvocationOnMock invocation) {
        final long l = (Long) invocation.getArguments()[0];
        computerList.removeIf(c -> c.getCompany().getId() == l);
        return null;
      }
    }).when(computerRepository).delete(anyLong());

    MockitoAnnotations.initMocks(this);
  }

  /*
   * Test getAll function
   */
  @Test
  public void getAll() {
    assertEquals(companyList, companyService.getAll());
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

  /*
   * NEEDS TO BE UPDATED
   */
  //  /*
  //   * Tests getPagedList function
  //   */
  //  @Test
  //  public void getPagedList() {
  //    assertEquals(pageReturned, companyService.getPagedList(page));
  //  }
  //
  //  @Test
  //  public void getPagedListNull() {
  //    assertNull(companyService.getPagedList(null));
  //  }
  //
  //  @Test(expected = NullPointerException.class)
  //  public void invalidPageNumber() {
  //    companyService.getPagedList(wrongPNumber);
  //  }
  //
  //  @Test
  //  public void invalidResultsPerPage() {
  //    assertNull(companyService.getPagedList(wrongRPP));
  //  }

  //  /*
  //   * Tests remove function
  //   */
  //  @Test
  //  public void delete() {
  //    assertTrue(companyService.removeById(1L));
  //  }
  //
  //  @Test
  //  public void deleteInvalidId() {
  //    assertTrue(companyService.removeById(3L));
  //  }
}