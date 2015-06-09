package com.celements.product.resolving;

import org.xwiki.component.annotation.ComponentRole;

import com.celements.product.IProductRef;

@ComponentRole
public interface IProductRefResolverRole {

  public String getName();

  /**
   * @param ref
   *          to be resolved, cannot be null/empty
   * @return the resolved productRef or null if unable to resolve
   */
  public IProductRef resolve(String ref);

}
