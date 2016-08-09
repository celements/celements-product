package com.celements.product.resolving;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xwiki.component.annotation.Component;
import org.xwiki.component.annotation.InstantiationStrategy;
import org.xwiki.component.annotation.Requirement;
import org.xwiki.component.descriptor.ComponentInstantiationStrategy;

import com.celements.product.IProductRef;
import com.celements.product.IUniqueProductRef;
import com.celements.product.UniqueProductRefException;

@Component
@InstantiationStrategy(ComponentInstantiationStrategy.SINGLETON)
public class ProductRefResolverManager implements IProductRefResolverManagerRole {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProductRefResolverManager.class);

  @Requirement
  List<IProductRefResolverRole> resolvers;

  @Requirement
  List<IUniqueProductRefResolverRole> unqiueResolvers;

  @Override
  public IUniqueProductRef resolveUnique(String ref) throws UniqueProductRefException {
    IUniqueProductRef ret = null;
    for (IUniqueProductRefResolverRole resolver : unqiueResolvers) {
      if (ret == null) {
        try {
          ret = resolver.resolve(ref);
        } catch (ProductRefResolvingException exc) {
          LOGGER.debug("resolver '{}' can't resolve '{}'", resolver.getName(), ref, exc);
        }
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
  public Map<String, IProductRef> resolve(String ref) {
    return resolve(ref, null);
  }

  @Override
  public Map<String, IProductRef> resolve(String ref, Set<String> allowed) {
    Map<String, IProductRef> ret = new HashMap<>();
    for (IProductRefResolverRole resolver : resolvers) {
      if ((allowed == null) || allowed.contains(resolver.getName())) {
        try {
          ret.put(resolver.getName(), resolver.resolve(ref));
        } catch (ProductRefResolvingException exc) {
          LOGGER.debug("resolver '{}' can't resolve '{}'", resolver.getName(), ref, exc);
        }
      }
    }
    return ret;
  }

}
