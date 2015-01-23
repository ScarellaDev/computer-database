package com.excilys.computerdatabase.validator;

import com.excilys.computerdatabase.dto.ComputerDto;

/**
* Validation class to check if Dtos are valid or not.
*
* @author Jeremy SCARELLA
*/
public class DtoValidation {

  /**
   * Check if a ComputerDto is valid or not.
   * @param computerDto : computerDto to check
   * @return true if the computerDto is valid
   */
  public static boolean isValidComputerDto(final ComputerDto computerDto) {
    if (computerDto == null) {
      return false;
    }
    if (computerDto.getId() < 0) {
      return false;
    }
    if (computerDto.getName() == null) {
      return false;
    }
    if (computerDto.getName().trim().isEmpty()) {
      return false;
    }
    if (computerDto.getIntroduced() != null) {
      if (!StringValidation.isDate(computerDto.getIntroduced())) {
        return false;
      }
    }
    if (computerDto.getDiscontinued() != null) {
      if (!StringValidation.isDate(computerDto.getDiscontinued())) {
        return false;
      }
    }
    if (computerDto.getCompany() < 0) {
      return false;
    }
    return true;
  }
}
