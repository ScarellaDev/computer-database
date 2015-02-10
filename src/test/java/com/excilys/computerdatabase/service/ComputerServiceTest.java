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
import org.springframework.jdbc.BadSqlGrammarException;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.dto.ComputerDto;
import com.excilys.computerdatabase.dto.ComputerDtoConverter;
import com.excilys.computerdatabase.exception.PersistenceException;
import com.excilys.computerdatabase.persistence.IComputerDao;
import com.excilys.computerdatabase.service.impl.ComputerServiceJDBC;

@RunWith(MockitoJUnitRunner.class)
public class ComputerServiceTest {

  @InjectMocks
  ComputerServiceJDBC computerServiceJDBC;
  Page<ComputerDto>   page;
  Page<ComputerDto>   pageReturned;
  Page<ComputerDto>   wrongPNumber;
  Page<ComputerDto>   wrongRPP;

  IComputerDao        computerDao;
  List<Computer>      list;
  List<ComputerDto>   listDto;
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

    listDto = new ArrayList<ComputerDto>();
    listDto.add(ComputerDtoConverter.toDto(new Computer(1L, "ordi 1", null, null, c1)));
    listDto.add(ComputerDtoConverter.toDto(new Computer(2L, "ordi 2", null, null, c1)));
    listDto.add(ComputerDtoConverter.toDto(new Computer(3L, "ordi 3", null, null, c2)));

    page = new Page<ComputerDto>();

    pageReturned = new Page<ComputerDto>();
    page.setTotalNbElements(listDto.size());
    page.setList(listDto);

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
            throw new PersistenceException(null, null);
          }
          list.set((int) (computer.getId() - 1), computer);
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
    assertEquals(list, computerServiceJDBC.getAll());
  }

  /*
   * Tests getById function
   */
  @Test
  public void getById() {
    assertEquals(list.get(0), computerServiceJDBC.getById(1L));
  }

  @Test
  public void getByIdInvalid() {
    assertNull(computerServiceJDBC.getById(-1L));
    assertNull(computerServiceJDBC.getById(5L));
  }

  /*
   * Tests getPagedList
   */
  @Test
  public void getPagedList() {
    assertEquals(pageReturned, computerServiceJDBC.getPagedList(page));
  }

  @Test
  public void getPagedListNull() {
    assertNull(computerServiceJDBC.getPagedList(null));
  }

  @Test(expected = NullPointerException.class)
  public void invalidPageNumber() {
    computerServiceJDBC.getPagedList(wrongPNumber);
  }

  @Test(expected = NullPointerException.class)
  public void invalidResultsPerPage() {
    computerServiceJDBC.getPagedList(wrongRPP);
  }

  /*
   * Tests of add function
   */
  @Test
  public void add() {
    computerServiceJDBC.addByComputer(computer);
    assertEquals(computer, list.get(3));
  }

  @Test
  public void addNull() {
    computerServiceJDBC.addByComputer(null);
    assertEquals(list, computerServiceJDBC.getAll());
  }

  @Test
  public void createEmptyComputer() {
    computerServiceJDBC.addByComputer(new Computer());
    assertEquals(list, computerServiceJDBC.getAll());
  }

  /*
   * Tests of update function
   */
  @Test
  public void update() {
    computerServiceJDBC.updateByComputer(computer);
    assertEquals(list, computerServiceJDBC.getAll());
  }

  @Test
  public void updateNull() {
    computerServiceJDBC.updateByComputer(null);
    assertEquals(list, computerServiceJDBC.getAll());
  }

  @Test
  public void updateInvalidId() {
    final Computer computer = new Computer();
    computer.setId(-1L);
    computerServiceJDBC.updateByComputer(computer);
    assertEquals(list, computerServiceJDBC.getAll());
  }

  @Test
  public void updateInvalidCompanyId() {
    final Computer computer = new Computer();
    computer.setId(1L);
    computer.setCompany(new Company(-1L, ""));
    computerServiceJDBC.updateByComputer(computer);
    assertEquals(list, computerServiceJDBC.getAll());
  }

  /*
   * Tests of the remove function
   */
  @Test
  public void remove() {
    final int x = computerServiceJDBC.getAll().size();
    computerServiceJDBC.removeById(3L);
    assertEquals(x - 1, list.size());
  }

  @Test
  public void removeInvalidId() {
    final int x = computerServiceJDBC.getAll().size();
    computerServiceJDBC.removeById(-1L);
    computerServiceJDBC.removeById(4L);
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
    l.forEach(id -> assertNotNull(computerServiceJDBC.getById(id)));
    computerServiceJDBC.removeByIdList(l);
    l.forEach(id -> assertNull(computerServiceJDBC.getById(id)));
  }
}
