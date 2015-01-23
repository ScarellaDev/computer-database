package com.excilys.computerdatabase.validator;

import java.time.LocalDateTime;

/**
* Validation class to check the String inputs entered by the user in the CLI.
*
* @author Jeremy SCARELLA
*/
public class StringValidation {

  /*
   * REGEX_DELIMITER
   */
  private static final String REGEX_DELIMITER       = "(\\.|-|\\/)";

  /*
   * REGEX_DATE_EN : yyyy-MM-dd (allowed separators = '.' || '-' || '/')
   */
  private static final String REGEX_DATE_EN         = "(" + "((\\d{4})" + REGEX_DELIMITER
                                                        + "(0[13578]|10|12)" + REGEX_DELIMITER
                                                        + "(0[1-9]|[12][0-9]|3[01]))"
                                                        + "|((\\d{4})" + REGEX_DELIMITER
                                                        + "(0[469]|11)" + REGEX_DELIMITER
                                                        + "([0][1-9]|[12][0-9]|30))" + "|((\\d{4})"
                                                        + REGEX_DELIMITER + "(02)"
                                                        + REGEX_DELIMITER
                                                        + "(0[1-9]|1[0-9]|2[0-8]))"
                                                        + "|(([02468][048]00)" + REGEX_DELIMITER
                                                        + "(02)" + REGEX_DELIMITER + "(29))"
                                                        + "|(([13579][26]00)" + REGEX_DELIMITER
                                                        + "(02)" + REGEX_DELIMITER + "(29))"
                                                        + "|(([0-9][0-9][0][48])" + REGEX_DELIMITER
                                                        + "(02)" + REGEX_DELIMITER + "(29))"
                                                        + "|(([0-9][0-9][2468][048])"
                                                        + REGEX_DELIMITER + "(02)"
                                                        + REGEX_DELIMITER + "(29))"
                                                        + "|(([0-9][0-9][13579][26])"
                                                        + REGEX_DELIMITER + "(02)"
                                                        + REGEX_DELIMITER + "(29))" + ")";

  /*
   * REGEX_DATE_FR : dd-MM-yyyy (allowed separators = '.' || '-' || '/')
   */
  private static final String REGEX_DATE_FR         = "(" + "((0[1-9]|[12][0-9]|3[01])"
                                                        + REGEX_DELIMITER + "(0[13578]|10|12)"
                                                        + REGEX_DELIMITER + "(\\d{4}))"
                                                        + "|(([0][1-9]|[12][0-9]|30)"
                                                        + REGEX_DELIMITER + "(0[469]|11)"
                                                        + REGEX_DELIMITER + "(\\d{4}))"
                                                        + "|((0[1-9]|1[0-9]|2[0-8])"
                                                        + REGEX_DELIMITER + "(02)"
                                                        + REGEX_DELIMITER + "(\\d{4}))" + "|((29)"
                                                        + REGEX_DELIMITER + "(02)"
                                                        + REGEX_DELIMITER + "([02468][048]00))"
                                                        + "|((29)" + REGEX_DELIMITER + "(02)"
                                                        + REGEX_DELIMITER + "([13579][26]00))"
                                                        + "|((29)" + REGEX_DELIMITER + "(02)"
                                                        + REGEX_DELIMITER + "([0-9][0-9][0][48]))"
                                                        + "|((29)" + REGEX_DELIMITER + "(02)"
                                                        + REGEX_DELIMITER
                                                        + "([0-9][0-9][2468][048]))" + "|((29)"
                                                        + REGEX_DELIMITER + "(02)"
                                                        + REGEX_DELIMITER
                                                        + "([0-9][0-9][13579][26]))" + ")";

  /**
  * Regex expression for a positive long
  */
  private static final String POSITIVE_LONG_PATTERN = "\\d{1,19}";

  /**
  * Regex expression for a positive int
  */
  private static final String POSITIVE_INT_PATTERN  = "\\d{1,9}";

  /**
  * Check if a String as the format of a positive int
  * @param intS : string to check
  * @return true if the format if correct
  */
  public static boolean isPositiveInt(final String intS) {
    if (intS == null || intS.trim().isEmpty()) {
      return false;
    }
    if (!intS.matches(POSITIVE_INT_PATTERN)) {
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
    if (longS == null || longS.trim().isEmpty()) {
      return false;
    }
    if (!longS.matches(POSITIVE_LONG_PATTERN)) {
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
    if (str == null) {
      return true;
    }
    if (str.trim().isEmpty()) {
      return true;
    }
    if ("".equals(str.trim())) {
      return true;
    }
    if ("null".equals(str.trim().toLowerCase())) {
      return true;
    }
    return false;
  }

  /**
   * Validation of a String to see if it matches a Date.
   * @param dateS : the String input entered by the user as a Date.
   * @return true if the input matches a Date, false otherwise.
   */
  public static boolean isDate(final String dateS) {
    if (dateS == null || dateS.trim().isEmpty()) {
      return false;
    }
    if (!dateS.matches(REGEX_DATE_EN)) {
      return false;
    }

    LocalDateTime currentTime = LocalDateTime.now();
    if (new Integer(dateS.substring(0, 4)) > currentTime.getYear()) {
      return false;
    }
    if (new Integer(dateS.substring(5, 7)) > currentTime.getMonthValue()) {
      return false;
    }
    if (new Integer(dateS.substring(8)) > currentTime.getDayOfMonth()) {
      return false;
    }

    return true;
  }
}
