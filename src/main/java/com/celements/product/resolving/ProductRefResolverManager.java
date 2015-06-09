package com.celements.product.resolving;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.xwiki.component.annotation.Component;
import org.xwiki.component.annotation.InstantiationStrategy;
import org.xwiki.component.annotation.Requirement;
import org.xwiki.component.descriptor.ComponentInstantiationStrategy;

import com.celements.product.UniqueProductRefException;
import com.celements.product.IProductRef;
import com.celements.product.IUniqueProductRef;

@Component
@InstantiationStrategy(ComponentInstantiationStrategy.SINGLETON)
public class ProductRefResolverManager implements IProductRefResolverManagerRole {

  @Requirement
  private List<IProductRefResolverRole> resolvers;

  @Requirement
  private List<IUniqueProductRefResolverRole> unqiueResolvers;

  @Override
  public IUniqueProductRef resolveUnique(String ref) throws UniqueProductRefException {
    IUniqueProductRef ret = null;
    for (IUniqueProductRefResolverRole resolver : unqiueResolvers) {
      if (ret == null) {
        ret = resolver.resolve(ref);
      } else {
        throw new UniqueProductRefException("too many resolver for ref '" + ref + "'");
      }
    }
    if (ret != null) {
      return ret;
    } else {
      throw new UniqueProductRefException("No resolver for ref '" + ref + "'");
    }
  }

  @Override
  public List<IProductRef> resolve(String ref) {
    return resolve(ref, null);
  }

  @Override
  public List<IProductRef> resolve(String ref, Set<String> allowed) {
    List<IProductRef> ret = new ArrayList<IProductRef>();
    for (IProductRefResolverRole resolver : resolvers) {
      if ((allowed == null) || allowed.contains(resolver.getName())) {
        IProductRef productRef = resolver.resolve(ref);
        if (productRef != null) {
          ret.add(productRef);
        }
      }
    }
    return ret;
  }

}
