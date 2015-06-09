package com.celements.product.catalogue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xwiki.component.annotation.Component;
import org.xwiki.component.annotation.InstantiationStrategy;
import org.xwiki.component.descriptor.ComponentInstantiationStrategy;

import com.celements.product.AbstractManager;
import com.celements.product.IProduct;
import com.celements.product.IProductRef;
import com.celements.product.IUniqueProductRef;
import com.celements.product.UniqueProductRefException;

@Component
@InstantiationStrategy(ComponentInstantiationStrategy.SINGLETON)
public class CatalogueManager extends AbstractManager<IProductRef, ICatalogueRole>
    implements ICatalogueManagerRole {

  private static final Logger LOGGER = LoggerFactory.getLogger(CatalogueManager.class);

  @Override
  protected Class<ICatalogueRole> getManageeClass() {
    return ICatalogueRole.class;
  }

  @Override
  public IProduct getProduct(IUniqueProductRef productRef)
      throws UniqueProductRefException, ProductRetrievalException {
    List<ICatalogueRole> catalogues = getManagees(productRef.getClass());
    if (catalogues.size() > 0) {
      return catalogues.get(0).getProduct(productRef);
    } else {
      throw new UniqueProductRefException("No catalogue for ref '" + productRef + "'");
    }
  }

  @Override
  public Map<String, List<IProduct>> getProducts(IProductRef productRef)
      throws ProductRetrievalException {
    return getProducts(productRef, null);
  }

  @Override
  public Map<String, List<IProduct>> getProducts(IProductRef productRef,
      Set<String> allowed) throws ProductRetrievalException {
    Map<String, List<IProduct>> ret = new HashMap<String, List<IProduct>>();
    for (ICatalogueRole catalogue : getManagees(productRef.getClass(), allowed)) {
      ret.put(catalogue.getName(), catalogue.getProducts(productRef));
    }
    return ret;
  }

  @Override
  protected boolean shouldBeUnique(Class<? extends IProductRef> clazz) {
    return IUniqueProductRef.class.isAssignableFrom(clazz);
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }

}
