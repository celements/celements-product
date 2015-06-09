package com.celements.product.resolving;

import java.util.List;
import java.util.Set;

import org.xwiki.component.annotation.ComponentRole;

import com.celements.product.UniqueProductRefException;
import com.celements.product.IProductRef;
import com.celements.product.IUniqueProductRef;

@ComponentRole
public interface IProductRefResolverManagerRole {

  public IUniqueProductRef resolveUnique(String ref) throws UniqueProductRefException;

  public List<IProductRef> resolve(String ref);

  public List<IProductRef> resolve(String ref, Set<String> allowed);

}
