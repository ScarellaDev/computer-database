package com.excilys.computerdatabase.validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;

/**
* Validation class to check the String inputs entered by the user in the CLI.
*
* @author Jeremy SCARELLA
*/
public final class StringValidator {

  /*
   * Private Constructor
   */
  private StringValidator() {

  }

  /**
  * Check if a String as the format of a positive int
  * @param intS : string to check
  * @return true if the format if correct
  */
  public static boolean isPositiveInt(final String intS) {
    if (isEmpty(intS)) {
      return false;
    }
    if (!GenericValidator.isInt(intS)) {
      return false;
    }
    if (new Integer(intS) < 0) {
      return false;
    }

    return true;
  }

  /**
  * Check if a String as the format of a positive long
  * @param longS : string to check
  * @return True if the format if correct
  */
  public static boolean isPositiveLong(final String longS) {
    if (isEmpty(longS)) {
      return false;
    }
    if (!GenericValidator.isLong(longS)) {
      return false;
    }
    if (new Long(longS) < 0) {
      return false;
    }

    return true;
  }

  /**
   * Checks if a String is empty
   * @param str : string to check
   * @return true if it  is empty
   */
  public static boolean isEmpty(final String str) {
    if (!StringUtils.isNotBlank(str)) {
      return true;
    }
    if ("null".equals(str.trim().toLowerCase())) {
      return true;
    }
    return false;
  }

  /**
  * Checks if a String is a valid name
  * @param str : string to check
  * @return true if it  is valid
  */
  public static boolean isValidName(final String str) {
    if (!StringUtils.isNotBlank(str)) {
      return false;
    }
    if ("null".equals(str.trim().toLowerCase())) {
      return false;
    }
    if (str.length() >= 255) {
      return false;
    }
    return true;
  }

  /**
   * Validation of a String to see if it matches a Date.
   * @param dateS : the String input entered by the user as a Date.
   * @param dateFormat : the format of the Date pattern used
   * @return true if the input matches a Date, false otherwise.
   */
  public static boolean isDate(final String dateS, final String dateFormat) {
    if (isEmpty(dateS)) {
      return false;
    }
    if (!GenericValidator.isDate(dateS, dateFormat, false)) {
      return false;
    }

    final LocalDate date = LocalDate.parse(dateS, DateTimeFormatter.ofPattern(dateFormat));
    final int year = date.getYear();
    //TIMESTAMP has a range of '1970-01-01 00:00:01' UTC to '2038-01-19 03:14:07' UTC
    if (year < 1970 || year > 2038) {
      return false;
    } else if (year == 1970) {
      if (date.getMonthValue() == 1 && date.getDayOfMonth() == 1) {
        return false;
      }
    } else if (year == 2038) {
      if (date.getMonthValue() > 1) {
        return false;
      } else {
        if (date.getDayOfMonth() > 19) {
          return false;
        }
      }
    }

    return true;
  }
}
