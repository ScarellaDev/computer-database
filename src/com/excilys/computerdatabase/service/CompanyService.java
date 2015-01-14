package com.excilys.computerdatabase.service;

import java.util.List;

import com.excilys.computerdatabase.domain.Company;

/**
* Interface implemented by services to manage companies.
*
* @author Jeremy SCARELLA
*/
public interface CompanyService {
  /**
  * Retrieve a company from the database thanks to the given id.
  *
  * @param id
  * The id of the company to retrieve.
  * @return The {@link Company}, or null if no matching company was found.
  */
  Company getById(Long id);

  /**
  * Retrieve all the companies from the database.
  *
  * @return A list containing all the companies.
  */
  List<Company> getAll();
}