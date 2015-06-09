package com.celements.product;

public class UniqueProductRefException extends Exception {

  private static final long serialVersionUID = 1L;

  public UniqueProductRefException() {
    super();
  }

  public UniqueProductRefException(String msg) {
    super(msg);
  }

  public UniqueProductRefException(String msg, Throwable cause) {
    super(msg, cause);
  }

}
