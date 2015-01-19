package com.excilys.computerdatabase.exception;

/**
* Generic wrapper for all exceptions thrown while using a database in the com.excilys.computerdatabase.persistence package.
*
* @author Jeremy SCARELLA
*/
public class PersistenceException extends RuntimeException {
  private static final long serialVersionUID = -8376160397101361602L;

  /**
  * Constructs a new persistence exception with the specified detailed message.
  *
  * @param message
  */
  public PersistenceException(String message) {
    super(message);
  }

  /**
  * Constructs a new runtime exception with the specified cause.
  *
  * @param cause
  */
  public PersistenceException(Throwable cause) {
    super(cause);
  }

  /**
  * Constructs a new runtime exception with the specified detailed message and cause.
  *
  * @param message
  * @param cause
  */
  public PersistenceException(String message, Throwable cause) {
    super(message, cause);
  }
}