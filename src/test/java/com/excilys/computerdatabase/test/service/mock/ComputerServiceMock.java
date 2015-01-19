package com.excilys.computerdatabase.test.service.mock;

import java.util.List;

import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.persistence.ComputerDao;
import com.excilys.computerdatabase.service.ComputerService;

public class ComputerServiceMock implements ComputerService {

  private ComputerDao computerDao;

  public ComputerServiceMock(ComputerDao computerDao) {
    this.computerDao = computerDao;
  }

  @Override
  public Computer getById(Long id) {
    return computerDao.getById(id);
  }

  @Override
  public List<Computer> getAll() {
    return computerDao.getAll();
  }

  @Override
  public Computer addByString(String[] params) {
    return computerDao.addByString(params);
  }

  @Override
  public Computer addByComputer(Computer computer) {
    return computerDao.addByComputer(computer);
  }

  @Override
  public Computer updateByString(String[] params) {
    return computerDao.updateByString(params);
  }

  @Override
  public Computer updateByComputer(Computer computer) {
    return computerDao.updateByComputer(computer);
  }

  @Override
  public Computer removeById(Long id) {
    return computerDao.removeById(id);
  }

  @Override
  public Computer removeByComputer(Computer computer) {
    return computerDao.removeByComputer(computer);
  }

  @Override
  public Long getLastId() {
    return computerDao.getLastId();
  }

}