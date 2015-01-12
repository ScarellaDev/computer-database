package com.excilys.computerdatabase.exception;

public class InvalidArgsNumberException extends Exception {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public InvalidArgsNumberException() {}

  public InvalidArgsNumberException(String message) {
    super(message);
  }

  public InvalidArgsNumberException(Throwable cause) {
    super(cause);
  }

  public InvalidArgsNumberException(String message, Throwable cause) {
    super(message, cause);
  }
}
