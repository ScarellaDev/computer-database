package com.excilys.computerdatabase.persistence;

import java.util.List;

import com.excilys.computerdatabase.domain.Computer;

/**
* Interface implemented by DAOs to manage computers.
*
* @author Jeremy SCARELLA
*/
public interface ComputerDao {

  Computer getById(Long id);

  List<Computer> getAll();

  Computer addByString(String[] params);

  Computer addByComputer(Computer computer);

  Computer updateByString(String[] params);

  Computer updateByComputer(Computer computer);

  Computer removeById(Long id);

  Computer removeByComputer(Computer computer);
  
  Long getLastId();
}