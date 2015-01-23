package com.excilys.computerdatabase.mapper;

import java.sql.ResultSet;
import java.util.List;

/**
* Interface : RowMapper
* 
* @author Jeremy SCARELLA
*/
public interface IRowMapper<T> {

  /**
  * Maps an element
  * @param results : the element that needs to be mapped
  * @return A mapped instance of <T>
  */
  T mapRow(ResultSet results);

  /**
  * Maps a list of elements
  * @param results : the list of elements that need to be mapped
  * @return A mapped instance of List<T>
  */
  List<T> mapRows(ResultSet results);
}
