package com.celements.product.catalogue;

public class ProductRetrievalException extends Exception {

  private static final long serialVersionUID = 1L;

  public ProductRetrievalException() {
    super();
  }

  public ProductRetrievalException(String msg) {
    super(msg);
  }

  public ProductRetrievalException(String msg, Throwable cause) {
    super(msg, cause);
  }

}
