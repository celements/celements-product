package com.celements.product.resolving;

import org.xwiki.component.annotation.ComponentRole;

import com.celements.product.IUniqueProductRef;

@ComponentRole
public interface IUniqueProductRefResolverRole extends IProductRefResolverRole {

  /**
   * @param ref
   *          to be resolved, cannot be null/empty
   * @return the resolved unique productRef or null if unable to resolve
   */
  public IUniqueProductRef resolve(String ref);

}
