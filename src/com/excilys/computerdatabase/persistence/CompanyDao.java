package com.excilys.computerdatabase.persistence;

import java.util.List;

import com.excilys.computerdatabase.domain.Company;

/**
* Interface implemented by DAOs to manage computers.
*
* @author Jeremy SCARELLA
*/
public interface CompanyDao {

  /**
   * Get the company in the database corresponding to the id in parameter
   * @param id : id of the company in the database
   * @return the company that was found or null if there is no company for this id
   */
  Company getById(Long id);

  /**
  * Get the List of all the companies in the database
  * @return List of all the companies in the database
  */
  List<Company> getAll();
}