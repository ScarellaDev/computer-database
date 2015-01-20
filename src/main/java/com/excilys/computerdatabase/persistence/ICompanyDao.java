package com.excilys.computerdatabase.persistence;

import java.util.List;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Page;

/**
* Interface implemented by Daos to manage computers.
*
* @author Jeremy SCARELLA
*/
public interface ICompanyDao {

  /**
   * Get the company in the database corresponding to the id in parameter.
   * @param id : id of the company in the database.
   * @return The company that was found or null if there is no company for this id.
   */
  Company getById(Long id);

  /**
  * Get the List of all the companies in the database.
  * @return List of all the companies in the database.
  */
  List<Company> getAll();

  /**
  * Get a Page of companies in the database.
  * @param page : a page containing the pageIndex and the max number of elements the page can have
  * @return A Page instance containing a sublist of companies
  */
  Page<Company> getPagedList(Page<Company> page);
}