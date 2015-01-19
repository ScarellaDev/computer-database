package com.excilys.computerdatabase.validator;

import com.excilys.computerdatabase.service.CompanyService;
import com.excilys.computerdatabase.service.ComputerService;
import com.excilys.computerdatabase.service.ManagerService;

/**
* Validation class to check the String inputs entered by the user in the CLI.
*
* @author Jeremy SCARELLA
*/
public class StringInputValidation {
  /*
   * Instance of computerService
   */
  private static ComputerService computerService = ManagerService.getInstance()
                                                     .getComputerService();

  /*
   * Instance of companyService
   */
  private static CompanyService  companyService  = ManagerService.getInstance().getCompanyService();

  /*
   * REGEX_DELIMITER
   */
  private static final String    REGEX_DELIMITER = "(\\.|-|\\/)";

  /*
   * REGEX_DATE_EN : yyyy-MM-dd (allowed separators = '.' || '-' || '/')
   */
  private static final String    REGEX_DATE_EN   = "(" + "((\\d{4})" + REGEX_DELIMITER
                                                     + "(0[13578]|10|12)" + REGEX_DELIMITER
                                                     + "(0[1-9]|[12][0-9]|3[01]))" + "|((\\d{4})"
                                                     + REGEX_DELIMITER + "(0[469]|11)"
                                                     + REGEX_DELIMITER + "([0][1-9]|[12][0-9]|30))"
                                                     + "|((\\d{4})" + REGEX_DELIMITER + "(02)"
                                                     + REGEX_DELIMITER + "(0[1-9]|1[0-9]|2[0-8]))"
                                                     + "|(([02468][048]00)" + REGEX_DELIMITER
                                                     + "(02)" + REGEX_DELIMITER + "(29))"
                                                     + "|(([13579][26]00)" + REGEX_DELIMITER
                                                     + "(02)" + REGEX_DELIMITER + "(29))"
                                                     + "|(([0-9][0-9][0][48])" + REGEX_DELIMITER
                                                     + "(02)" + REGEX_DELIMITER + "(29))"
                                                     + "|(([0-9][0-9][2468][048])"
                                                     + REGEX_DELIMITER + "(02)" + REGEX_DELIMITER
                                                     + "(29))" + "|(([0-9][0-9][13579][26])"
                                                     + REGEX_DELIMITER + "(02)" + REGEX_DELIMITER
                                                     + "(29))" + ")";

  /*
   * REGEX_DATE_FR : dd-MM-yyyy (allowed separators = '.' || '-' || '/')
   */
  private static final String    REGEX_DATE_FR   = "(" + "((0[1-9]|[12][0-9]|3[01])"
                                                     + REGEX_DELIMITER + "(0[13578]|10|12)"
                                                     + REGEX_DELIMITER + "(\\d{4}))"
                                                     + "|(([0][1-9]|[12][0-9]|30)"
                                                     + REGEX_DELIMITER + "(0[469]|11)"
                                                     + REGEX_DELIMITER + "(\\d{4}))"
                                                     + "|((0[1-9]|1[0-9]|2[0-8])" + REGEX_DELIMITER
                                                     + "(02)" + REGEX_DELIMITER + "(\\d{4}))"
                                                     + "|((29)" + REGEX_DELIMITER + "(02)"
                                                     + REGEX_DELIMITER + "([02468][048]00))"
                                                     + "|((29)" + REGEX_DELIMITER + "(02)"
                                                     + REGEX_DELIMITER + "([13579][26]00))"
                                                     + "|((29)" + REGEX_DELIMITER + "(02)"
                                                     + REGEX_DELIMITER + "([0-9][0-9][0][48]))"
                                                     + "|((29)" + REGEX_DELIMITER + "(02)"
                                                     + REGEX_DELIMITER + "([0-9][0-9][2468][048]))"
                                                     + "|((29)" + REGEX_DELIMITER + "(02)"
                                                     + REGEX_DELIMITER + "([0-9][0-9][13579][26]))"
                                                     + ")";

  /**
   * Validation of a String to see if it matches a Long computer id.
   * @param idS : the String input entered by the user as Long id.
   * @return true if the input matches a Long, false otherwise.
   */
  public static Boolean isComputerId(String idS) {
    Long id;
    Long max = computerService.getLastId();
    if (idS.matches("[0-9]+")) {
      id = new Long(idS);
      if (id < 1 || id > max) {
        return false;
      } else {
        return true;
      }
    } else {
      return false;
    }
  }

  /**
   * Validation of a String to see if it matches a Long company id.
   * @param idS : the String input entered by the user as Long id.
   * @return true if the input matches a Long, false otherwise.
   */
  public static Boolean isCompanyId(String idS) {
    Long id;
    if (idS.matches("[0-9]+")) {
      id = new Long(idS);
      if (id < 1 || id > 43) {
        return false;
      } else {
        return true;
      }
    } else {
      return false;
    }
  }

  /**
   * Validation of a String to see if it matches a Date.
   * @param dateS : the String input entered by the user as a Date.
   * @return true if the input matches a Date, false otherwise.
   */
  public static Boolean isDate(String dateS) {
    if (dateS.matches(REGEX_DATE_EN)) {
      return true;
    } else {
      return false;
    }
  }
}
