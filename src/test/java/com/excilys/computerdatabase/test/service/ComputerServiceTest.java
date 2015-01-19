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
import com.excilys.computerdatabase.persistence.ComputerDao;
import com.excilys.computerdatabase.service.ComputerService;
import com.excilys.computerdatabase.test.service.mock.ComputerServiceMock;

@RunWith(MockitoJUnitRunner.class)
public class ComputerServiceTest {
  ComputerService computerService;

  Long            computerId;
  Long            computerId2;
  Computer        computer;
  Computer        computer2;
  ComputerDao     computerDao;

  @Before
  public void init() {
    computerDao = mock(ComputerDao.class);
    computer = Computer.builder().name("CM-5").build();
    computerId = computer.getId();
    computer2 = Computer.builder().name("CM-6").build();
    computerId2 = computer2.getId();
    when(computerDao.getAll()).thenReturn(new ArrayList<Computer>());
    when(computerDao.getById(anyLong())).thenReturn(Computer.builder().id(1L).build());
    computerService = new ComputerServiceMock(computerDao);
  }

  @Test
  public void testGetById() {
    assertEquals(Computer.builder().id(1L).build(), computerService.getById(1L));
  }

  @Test
  public void testGetAll() {
    assertEquals(new ArrayList<Computer>(), computerService.getAll());
  }

  @Test
  public void testAddByString() {
    String[] params = "CM-4 1992-01-01 null null".split("\\s+");

    computerService.addByString(params);
    verify(computerDao).addByString(params);
  }

  @Test
  public void testAddByComputer() {
    computerService.addByComputer(computer);
    verify(computerDao).addByComputer(computer);
  }

  public void updateByString() {
    String[] params = (computerId.toString() + " CM-7 1994-01-01 null null").split("\\s+");

    computerService.updateByString(params);
    verify(computerDao).updateByString(params);
  }

  public void updateByComputer() {
    computerService.updateByComputer(computer);
    verify(computerDao).updateByComputer(computer);
  }

  public void removeById() {
    computerService.removeById(computerId);
    verify(computerDao).removeById(computerId);
  }

  public void removeByComputer() {
    computerService.removeByComputer(Computer.builder().id(computerId2).build());
    verify(computerDao).removeByComputer(Computer.builder().id(computerId2).build());
  }
}