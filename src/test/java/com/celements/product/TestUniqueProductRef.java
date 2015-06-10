package com.celements.product;

import com.celements.product.IProductRef;

public class TestUniqueProductRef implements IUniqueProductRef {

  private static final long serialVersionUID = 1L;

  @Override
  public int compareTo(IProductRef o) {
    throw new UnsupportedOperationException();
  }

}
