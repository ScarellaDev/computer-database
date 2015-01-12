package com.excilys.computerdatabase.exception;

public class InvalidCompanyIdException extends Exception {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public InvalidCompanyIdException() {}

  public InvalidCompanyIdException(String message) {
    super(message);
  }

  public InvalidCompanyIdException(Throwable cause) {
    super(cause);
  }

  public InvalidCompanyIdException(String message, Throwable cause) {
    super(message, cause);
  }
}
