package com.excilys.computerdatabase.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.excilys.computerdatabase.domain.Company;

/**
* Interface implemented by services to manage Company objects.
*
* @author Jeremy SCARELLA
*/
public interface ICompanyService {
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
   * Remove a company from the database using its id.
   * @param id : id of the company to remove.
   * @return An instance of the company that was removed from the database or null
   */
  Company removeById(Long id);

  /**
   * Get a Page of companies from the database.
   * @param pageable: a Spring Pageable object
   * @return A Page instance containing a sublist of companies
   */
  Page<Company> getPagedList(Pageable pageable);
}