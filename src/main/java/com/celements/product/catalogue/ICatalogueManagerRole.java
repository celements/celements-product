package com.celements.product.catalogue;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xwiki.component.annotation.ComponentRole;

import com.celements.product.IProduct;
import com.celements.product.IProductRef;
import com.celements.product.IUniqueProductRef;
import com.celements.product.UniqueProductRefException;

@ComponentRole
public interface ICatalogueManagerRole<T extends IProduct> {

  /**
   * @param productRef
   *          to get product for, cannot be null
   * @return the product, cannot be null
   * @throws UniqueProductRefException
   *           if productRef cannot be uniquely serialized
   * @throws ProductRetrievalException
   *           if retrieving product failed (e.g. database access failed)
   */
  public T getProduct(IUniqueProductRef productRef) throws UniqueProductRefException,
      ProductRetrievalException;

  /**
   * @param productRef
   *          to get products for, cannot be null
   * @return a map of the catalogue name as key and the products as value (cannot be null)
   * @throws ProductRetrievalException
   *           if retrieving products failed (e.g. database access failed)
   */
  public Map<String, List<T>> getProducts(IProductRef productRef) throws ProductRetrievalException;

  /**
   * @param productRef
   *          to get products for, cannot be null
   * @param allowed
   *          list of allowed catalogues denoted by name
   * @return a map of the catalogue name as key and the products as value (cannot be null)
   * @throws ProductRetrievalException
   *           if retrieving products failed (e.g. database access failed)
   */
  public Map<String, List<T>> getProducts(IProductRef productRef, Set<String> allowed)
      throws ProductRetrievalException;

}
