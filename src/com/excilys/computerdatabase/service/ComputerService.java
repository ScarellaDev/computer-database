package com.excilys.computerdatabase.service;

import java.io.Serializable;
import java.util.List;

import com.excilys.computerdatabase.exception.InvalidArgsNumberException;
import com.excilys.computerdatabase.exception.InvalidCompanyIdException;
import com.excilys.computerdatabase.model.Computer;
import com.excilys.computerdatabase.persistence.ComputerDao;

public class ComputerService implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
  private ComputerDao       computerDao      = new ComputerDao();
	
	public Computer getComputer(Long id) {
		return computerDao.getComputer(id);
	}
	
	public List<Computer> getAllComputers() {
		return computerDao.getAllComputers();
	}
	
	public Boolean setComputer(String[] args) throws InvalidArgsNumberException, InvalidCompanyIdException {
	  return computerDao.setComputer(args);
	}
	
	public Boolean addComputer(String[] args) throws InvalidArgsNumberException, InvalidCompanyIdException {
      return computerDao.addComputer(args);
    }
	
	public Boolean addComputer(Computer computer) throws InvalidArgsNumberException, InvalidCompanyIdException {
      return computerDao.addComputer(computer);
    }
	
	public Boolean removeComputer(Long id) {
      return computerDao.removeComputer(id);
    }
	
	public Boolean removeComputer(Computer computer) {
      return computerDao.removeComputer(computer);
    }
	
	public Boolean removeLastComputer() {
      return computerDao.removeComputer(computerDao.getLastId());
    }
	
	public Long getLastId() {
	  return computerDao.getLastId();
	}
}
