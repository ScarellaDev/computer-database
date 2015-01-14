package com.excilys.computerdatabase.exception;

/**
* Generic wrapper for all exceptions thrown while using a database.
*
* @author Jeremy SCARELLA
*/
public class PersistenceException extends RuntimeException {
  private static final long serialVersionUID = -8376160397101361602L;

  /**
  * Constructs a new persistence exception with the specified detail message.
  *
  * @param message
  * the detail message.
  */
  public PersistenceException(String message) {
    super(message);
  }

  /**
  * Constructs a new runtime exception with the specified detail message and cause.
  *
  * @param message
  * the detail message.
  * @param cause
  * the cause.
  */
  public PersistenceException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
  * Constructs a new runtime exception with the specified cause.
  *
  * @param cause
  * the cause.
  */
  public PersistenceException(Throwable cause) {
    super(cause);
  }
}