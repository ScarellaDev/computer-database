package com.excilys.computerdatabase.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.dto.ComputerDto;
import com.excilys.computerdatabase.dto.ComputerDtoConverter;
import com.excilys.computerdatabase.exception.PersistenceException;
import com.excilys.computerdatabase.persistence.IComputerDao;
import com.excilys.computerdatabase.service.impl.ComputerServiceJDBC;

/**
 * Test class for the ComputerService
 * 
 * @author Jeremy SCARELLA
 */
@RunWith(MockitoJUnitRunner.class)
public class ComputerServiceTest {

  @InjectMocks
  ComputerServiceJDBC computerService;
  Page<ComputerDto>   page;
  Page<ComputerDto>   pageReturned;
  Page<ComputerDto>   wrongPNumber;
  Page<ComputerDto>   wrongRPP;

  IComputerDao        computerDao;
  List<Computer>      list;
  Company             c1;
  Company             c2;
  Computer            computer;

  @Before
  public void init() {
    computerDao = mock(IComputerDao.class);

    c1 = new Company(1L, "company 1");
    c2 = new Company(2L, "company 2");

    list = new ArrayList<Computer>();
    list.add(new Computer(1L, "ordi 1", null, null, c1));
    list.add(new Computer(2L, "ordi 2", null, null, c1));
    list.add(new Computer(3L, "ordi 3", null, null, c2));

    page = new Page<ComputerDto>();

    pageReturned = new Page<ComputerDto>();
    page.setTotalNbElements(list.size());
    page.setList(ComputerDtoConverter.toDto(list));

    wrongPNumber = new Page<ComputerDto>();
    wrongPNumber.setPageIndex(-1);

    wrongRPP = new Page<ComputerDto>();
    wrongRPP.setNbElementsPerPage(-1);

    computer = new Computer(4L, "ordi 4", null, null, c1);

    when(computerDao.getAll()).thenReturn(list);
    when(computerDao.getPagedList(page)).thenReturn(pageReturned);

    doAnswer(new Answer<Computer>() {
      @Override
      public Computer answer(final InvocationOnMock invocation) {
        final long l = (Long) invocation.getArguments()[0];
        if (l > 0 && l < list.size()) {
          return list.get((int) l - 1);
        }
        return null;
      }
    }).when(computerDao).getById(anyLong());

    doAnswer(new Answer<Computer>() {

      @Override
      public Computer answer(final InvocationOnMock invocation) {
        final Computer computer = (Computer) invocation.getArguments()[0];
        if (computer != null) {
          list.add(computer);
        }
        return null;
      }
    }).when(computerDao).addByComputer(any(Computer.class));

    doAnswer(new Answer<Computer>() {

      @Override
      public Computer answer(final InvocationOnMock invocation) {
        final Computer computer = (Computer) invocation.getArguments()[0];
        if (computer != null && computer.getId() > 0 && computer.getId() < list.size()) {
          if (computer.getCompany() != null
              && (computer.getCompany().getId() < 0 || computer.getCompany().getId() > 2)) {
            throw new PersistenceException("ComputerServiceTest Exception");
          }
          list.set(computer.getId().intValue() - 1, computer);
        }
        return null;
      }

    }).when(computerDao).updateByComputer(any(Computer.class));

    doAnswer(new Answer<Computer>() {

      @Override
      public Computer answer(final InvocationOnMock invocation) {
        final long l = (Long) invocation.getArguments()[0];
        list.removeIf(c -> c.getId() == l);
        return null;
      }

    }).when(computerDao).removeById(anyLong());

    doAnswer(new Answer<Computer>() {

      @Override
      public Computer answer(final InvocationOnMock invocation) {
        @SuppressWarnings("unchecked")
        final List<Long> l = (List<Long>) invocation.getArguments()[0];
        list.removeIf(c -> l.contains(c.getId()));
        return null;
      }

    }).when(computerDao).removeByIdList(Matchers.anyListOf(Long.class));

    when(computerDao.getPagedList(page)).thenReturn(pageReturned);
    doThrow(PersistenceException.class).when(computerDao).getPagedList(wrongPNumber);
    doThrow(PersistenceException.class).when(computerDao).getPagedList(wrongRPP);

    MockitoAnnotations.initMocks(this);
  }

  /*
   * Test getAll function
   */
  @Test
  public void getAll() {
    assertEquals(list, computerService.getAll());
  }

  /*
   * Tests getById function
   */
  @Test
  public void getById() {
    assertEquals(list.get(0), computerService.getById(1L));
  }

  @Test
  public void getByIdInvalid() {
    assertNull(computerService.getById(-1L));
    assertNull(computerService.getById(5L));
  }

  /*
   * Tests getPagedList
   */
  @Test
  public void getPagedList() {
    assertEquals(pageReturned, computerService.getPagedList(page));
  }

  @Test
  public void getPagedListNull() {
    assertNull(computerService.getPagedList(null));
  }

  @Test(expected = PersistenceException.class)
  public void invalidPageNumber() {
    computerService.getPagedList(wrongPNumber);
  }

  @Test(expected = PersistenceException.class)
  public void invalidResultsPerPage() {
    computerService.getPagedList(wrongRPP);
  }

  /*
   * Tests of create function
   */
  @Test
  public void create() {
    computerService.addByComputer(computer);
    assertEquals(computer, list.get(3));
  }

  @Test
  public void createNull() {
    computerDao.addByComputer(null);
    assertEquals(list, computerService.getAll());
  }

  @Test
  public void createEmptyComputer() {
    computerDao.addByComputer(new Computer());
    assertEquals(list, computerService.getAll());
  }

  /*
   * Tests of update function
   */
  @Test
  public void update() {
    computerService.updateByComputer(computer);
    assertEquals(list, computerService.getAll());
  }

  @Test
  public void updateNull() {
    computerDao.updateByComputer(null);
    assertEquals(list, computerService.getAll());
  }

  @Test
  public void updateInvalidId() {
    final Computer computer = new Computer();
    computer.setId(-1L);
    computerDao.updateByComputer(computer);
    assertEquals(list, computerService.getAll());
  }

  @Test(expected = PersistenceException.class)
  public void updateInvalidCompanyId() {
    final Computer computer = new Computer();
    computer.setId(1L);
    computer.setCompany(new Company(-1L, ""));
    computerDao.updateByComputer(computer);
  }

  /*
   * Tests of the delete() function
   */
  @Test
  public void delete() {
    final int x = computerService.getAll().size();
    computerService.removeById(3L);
    assertEquals(x - 1, list.size());
  }

  @Test
  public void deleteInvalidId() {
    final int x = computerService.getAll().size();
    computerDao.removeById(-1L);
    computerDao.removeById(4L);
    assertEquals(x, list.size());
  }

  /*
   * Test of the delete(List) function
   */
  @Test
  public void multipleDelete() {
    final List<Long> l = new ArrayList<Long>();
    l.add(1L);
    l.add(2L);
    l.forEach(id -> assertNotNull(computerDao.getById(id)));
    computerService.removeByIdList(l);
    l.forEach(id -> assertNull(computerDao.getById(id)));
  }
}
