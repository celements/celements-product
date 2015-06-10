package com.celements.product.resolving;

import java.util.Map;
import java.util.Set;

import org.xwiki.component.annotation.ComponentRole;

import com.celements.product.IProductRef;
import com.celements.product.IUniqueProductRef;
import com.celements.product.UniqueProductRefException;

@ComponentRole
public interface IProductRefResolverManagerRole {

  /**
   * 
   * @param ref
   *          to be resolved, cannot be null/empty
   * @return the resolved productRef, cannot be null/empty
   * @throws UniqueProductRefException
   *           if ref cannot be uniquely resolved
   */
  public IUniqueProductRef resolveUnique(String ref) throws UniqueProductRefException;

  /**
   * 
   * @param ref
   *          to be resolved, cannot be null/empty
   * @return a map of the resolver name as key and the resolved productRef as value
   *         (cannot be null/empty)
   */
  public Map<String, IProductRef> resolve(String ref);

  /**
   * 
   * @param ref
   *          to be resolved, cannot be null/empty
   * @param allowed
   *          list of allowed resolvers denoted by name
   * @return a map of the resolver name as key and the resolved productRef as value
   *         (cannot be null/empty)
   */
  public Map<String, IProductRef> resolve(String ref, Set<String> allowed);

}
