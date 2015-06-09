package com.celements.product.catalogue;

import java.util.List;

import org.xwiki.component.annotation.ComponentRole;

import com.celements.product.IManagee;
import com.celements.product.IProduct;
import com.celements.product.IProductRef;
import com.celements.product.IUniqueProductRef;

@ComponentRole
public interface ICatalogueRole extends IManagee<IProductRef> {

  public String getName();

  /**
   * @param productRef
   *          to get product for, must be of type from {@link #getSupportedClasses()} and
   *          cannot be null
   * @return the product, cannot be null
   * @throws ProductRetrievalException
   *           if retrieving product failed (e.g. database access failed)
   */
  public IProduct getProduct(IUniqueProductRef productRef)
      throws ProductRetrievalException;

  /**
   * 
   * @param productRef
   *          to get products for, must be of type from {@link #getSupportedClasses()} and
   *          cannot be null
   * @return all found products for the given productRef
   * @throws ProductRetrievalException
   *           if retrieving products failed (e.g. database access failed)
   */
  public List<IProduct> getProducts(IProductRef productRef)
      throws ProductRetrievalException;

}
