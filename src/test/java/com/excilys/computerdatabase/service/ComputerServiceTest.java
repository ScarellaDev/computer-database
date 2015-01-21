package com.excilys.computerdatabase.test.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.persistence.IComputerDao;
import com.excilys.computerdatabase.service.IComputerDBService;
import com.excilys.computerdatabase.test.service.mock.ComputerServiceMock;

/**
 * Test class for the ComputerService
 * 
 * @author Jeremy SCARELLA
 */
@RunWith(MockitoJUnitRunner.class)
public class ComputerServiceTest {
  /*
   * Attributes
   */
  private IComputerDBService computerService;
  private Long            computerId;
  private Long            computerId2;
  private Computer        computer;
  private Computer        computer2;
  private IComputerDao     computerDao;
  Page<Computer>          page;
  Page<Computer>          pageReturned;

  /**
   * Test initialisation using Mockito, creates a mock ComputerDao and two instances of Computer.
   */
  @Before
  public void init() {
    computerDao = mock(IComputerDao.class);
    computer = Computer.builder().id(1L).name("CM-5").build();
    computerId = computer.getId();
    computer2 = Computer.builder().id(2L).name("CM-6").build();
    computerId2 = computer2.getId();

    page = new Page<Computer>();
    page.setNbElementsPerPage(5);
    page.setPageIndex(1);
    pageReturned = new Page<Computer>();
    page.setNbElementsPerPage(5);
    page.setPageIndex(1);
    page.setTotalNbElements(20);
    page.setList(new ArrayList<Computer>());

    when(computerDao.getAll()).thenReturn(new ArrayList<Computer>());
    when(computerDao.getById(anyLong())).thenReturn(Computer.builder().id(1L).build());
    when(computerDao.getPagedList(page)).thenReturn(pageReturned);
    computerService = new ComputerServiceMock(computerDao);
  }

  /**
   * Test the getById method. 
   * @result Check if the computers retrieved from database are correct and that method returns null if no computer found.
   */
  @Test
  public void testGetById() {
    assertEquals(Computer.builder().id(1L).build(), computerService.getById(1L));
  }

  /**
   * Test the getAll method. 
   * @result Check if the companies retrieved from database are correct.
   */
  @Test
  public void testGetAll() {
    assertEquals(new ArrayList<Computer>(), computerService.getAll());
  }

  /**
   * Test the addByString method.
   * @result Check if the INSERT SQL statement is executed properly using a String table as parameter
   */
  @Test
  public void testAddByString() {
    String[] params = "CM-4 1992-01-01 null null".split("\\s+");

    computerService.addByString(params);
    verify(computerDao).addByString(params);
  }

  /**
   * Test the addByComputer method.
   * @result Check if the INSERT SQL statements are executed properly using a Computer instance as parameter
   */
  @Test
  public void testAddByComputer() {
    computerService.addByComputer(computer);
    verify(computerDao).addByComputer(computer);
  }

  /**
   * Test the updateByString method.
   * @result Check if the UPDATE SQL statement is executed properly using a String table as parameter
   */
  @Test
  public void updateByString() {
    String[] params = (computerId.toString() + " CM-7 1994-01-01 null null").split("\\s+");

    computerService.updateByString(params);
    verify(computerDao).updateByString(params);
  }

  /**
   * Test the updateByComputer method.
   * @result Check if the UPDATE SQL statements are executed properly using a Computer instance as parameter
   */
  @Test
  public void updateByComputer() {
    computerService.updateByComputer(computer);
    verify(computerDao).updateByComputer(computer);
  }

  /**
   * Test the removeById method.
   * @result Check if the DELETE SQL statement is executed properly using a Long id as parameter
   */
  @Test
  public void removeById() {
    computerService.removeById(computerId);
    verify(computerDao).removeById(computerId);
  }

  /**
   * Test the removeByComputer method.
   * @result Check if the DELETE SQL statements are executed properly using a Computer instance as parameter
   */
  @Test
  public void removeByComputer() {
    computerService.removeByComputer(Computer.builder().id(computerId2).build());
    verify(computerDao).removeByComputer(Computer.builder().id(computerId2).build());
  }

  /**
   * Test the getPagedList method. 
   * @result Check if the page retrieved from database is correct.
   */
  @Test
  public void getPagedList() {
    assertEquals(pageReturned, computerService.getPagedList(page));
  }
}