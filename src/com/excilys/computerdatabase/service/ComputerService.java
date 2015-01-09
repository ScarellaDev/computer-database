package com.excilys.computerdatabase.service;

import java.io.Serializable;
import java.util.List;

import com.excilys.computerdatabase.bean.Computer;
import com.excilys.computerdatabase.dao.ComputerDao;

public class ComputerService implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
  private ComputerDao       computerDao      = new ComputerDao();
	
	public Computer getComputer(Long id){
		return computerDao.getComputer(id);
	}
	
	public List<Computer> getAllComputers(){
		return computerDao.getAllComputers();
	}
}
