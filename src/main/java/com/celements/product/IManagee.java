package com.celements.product;

import java.util.Set;

public interface IManagee<K> {

  public String getName();

  /**
   * @return a set of classes supported by this managee
   */
  public Set<Class<? extends K>> getSupportedClasses();

}
