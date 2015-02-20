package com.excilys.computerdatabase.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
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
import com.excilys.computerdatabase.persistence.repository.ComputerRepository;
import com.excilys.computerdatabase.service.impl.ComputerService;

@RunWith(MockitoJUnitRunner.class)
public class ComputerServiceTest {

  @InjectMocks
  ComputerService    computerService;
  Page<Computer>     page;
  Pageable           pageable;

  ComputerRepository computerRepository;
  List<Computer>     computerList;
  Company            c1;
  Company            c2;
  Computer           computer1;
  Computer           computer;

  @Before
  public void init() {
    computerRepository = mock(ComputerRepository.class);

    c1 = new Company(1L, "company 1");
    c2 = new Company(2L, "company 2");

    computerList = new ArrayList<Computer>();
    computer1 = new Computer(1L, "ordi 1", null, null, c1);
    computerList.add(computer1);
    computerList.add(new Computer(2L, "ordi 2", null, null, c1));
    computerList.add(new Computer(3L, "ordi 3", null, null, c2));

    pageable = new PageRequest(0, 5);

    computer = new Computer(0L, "ordi 4", null, null, c1);

    page = new PageImpl<Computer>(computerList, pageable, computerList.size());
    when(computerRepository.findAll()).thenReturn(computerList);
    when(
        computerRepository.findByNameStartingWithOrCompanyNameStartingWith(anyString(),
            anyString(), any(Pageable.class))).thenReturn(page);

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

    doAnswer(new Answer<Computer>() {
      @Override
      public Computer answer(final InvocationOnMock invocation) {
        final Long l = (Long) invocation.getArguments()[0];
        List<Computer> comp = computerList.stream().filter(c -> c.getId().equals(l))
            .collect(Collectors.toList());
        if (comp.isEmpty()) {
          return null;
        } else {
          return comp.get(0);
        }
      }
    }).when(computerRepository).findOne(anyLong());

    doAnswer(new Answer<Computer>() {
      @Override
      public Computer answer(final InvocationOnMock invocation) {
        final Computer computer = (Computer) invocation.getArguments()[0];
        if (computer != null) {
          if (computer.getId() < 1 || computer.getId() >= computerList.size()) {
            computer.setId(new Long(computerList.size() + 1));
            computerList.add(computer);
          } else {
            computerList.set((int) (computer.getId() - 1), computer);
          }
        }
        return computer;
      }
    }).when(computerRepository).save(any(Computer.class));

    doAnswer(new Answer<Computer>() {
      @Override
      public Computer answer(final InvocationOnMock invocation) {
        final Long l = (Long) invocation.getArguments()[0];
        computerList.removeIf(c -> c.getId().equals(l));
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
    assertEquals(computerList, computerService.getAll());
  }

  /*
   * Tests getById function
   */
  @Test
  public void getById() {
    assertEquals(computerRepository.findOne(1L), computerService.getById(1L));
  }

  @Test
  public void getByIdInvalid() {
    assertNull(computerService.getById(-1L));
    assertNull(computerService.getById(5L));
  }

  @Test
  public void getByIdNull() {
    assertNull(computerService.getById(null));
  }

  /*
   * Tests of add function
   */
  @Test
  public void addByComputer() {
    Computer addedComputer = computerService.addByComputer(computer);
    assertEquals(addedComputer, computerList.get(computerList.size() - 1));
  }

  @Test
  public void addByComputerNull() {
    List<Computer> l = new ArrayList<Computer>(computerList);
    computerService.addByComputer(null);
    assertEquals(l, computerList);
  }

  @Test
  public void addByComputerEmpty() {
    List<Computer> l = new ArrayList<Computer>(computerList);
    computerService.addByComputer(new Computer());
    assertEquals(l, computerList);
  }

  /*
   * Tests of update function
   */
  @Test
  public void updateByComputer() {
    computer1.setName("Updated");
    computerService.updateByComputer(computer1);
    assertEquals(computer1, computerRepository.findOne(computer1.getId()));
  }

  @Test
  public void updateByComputerNull() {
    List<Computer> l = new ArrayList<Computer>(computerList);
    computerService.updateByComputer(null);
    assertEquals(l, computerList);
  }

  @Test
  public void updateByComputerInvalidId() {
    final Computer computer = new Computer();
    List<Computer> l = new ArrayList<Computer>(computerList);
    computer.setId(-1L);
    computerService.updateByComputer(computer);
    assertEquals(l, computerList);
  }

  @Test
  public void updateByComputerInvalidCompanyId() {
    final Computer computer = new Computer();
    List<Computer> l = new ArrayList<Computer>(computerList);
    computer.setId(1L);
    computer.setCompany(new Company(-1L, ""));
    computerService.updateByComputer(computer);
    assertEquals(l, computerList);
  }

  /*
   * Tests of the remove function
   */
  @Test
  public void removeById() {
    final long id = 3L;
    computerService.removeById(id);
    assertNull(computerRepository.findOne(id));
  }

  @Test
  public void removeByIdInvalid() {
    final int x = computerService.getAll().size();
    computerService.removeById(-1L);
    computerService.removeById(4L);
    assertEquals(x, computerList.size());
  }

  @Test
  public void removeByIdNull() {
    final int x = computerService.getAll().size();
    computerService.removeById(null);
    assertEquals(x, computerList.size());
  }

  /*
   * Test of the remove(List) function
   */
  @Test
  public void removeByIdList() {
    final List<Long> l = new ArrayList<Long>();
    l.add(1L);
    l.add(2L);
    l.add(3L);
    l.forEach(id -> assertNotNull(computerRepository.findOne(id)));
    computerService.removeByIdList(l);
    l.forEach(id -> assertNull(computerRepository.findOne(id)));
  }

  @Test
  public void removeByIdListInvalid() {
    final List<Long> l = new ArrayList<Long>();
    l.add(0L);
    l.add(5L);
    l.add(-1L);
    final List<Computer> list = new ArrayList<Computer>(computerList);
    l.forEach(id -> assertNull(computerRepository.findOne(id)));
    computerService.removeByIdList(l);
    assertEquals(list, computerList);
  }

  @Test
  public void removeByIdListNull() {
    final List<Long> l = new ArrayList<Long>();
    final List<Computer> list = new ArrayList<Computer>(computerList);
    l.forEach(id -> assertNull(computerRepository.findOne(id)));
    computerService.removeByIdList(l);
    assertEquals(list, computerList);
  }

  /*
   * Tests getPagedList
   */
  @Test
  public void getPagedList() {
    assertEquals(page, computerService.getPagedList("", pageable));
  }

  @Test
  public void getPagedListNull() {
    assertNull(computerService.getPagedList("", null));
  }

}
