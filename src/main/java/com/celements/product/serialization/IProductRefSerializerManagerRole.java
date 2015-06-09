package com.celements.product.serialization;

import java.util.Map;
import java.util.Set;

import org.xwiki.component.annotation.ComponentRole;

import com.celements.product.IProductRef;
import com.celements.product.IUniqueProductRef;
import com.celements.product.UniqueProductRefException;

@ComponentRole
public interface IProductRefSerializerManagerRole {

  /**
   * 
   * @param productRef
   *          to be serialized, cannot be null
   * @return the serialized productRef, cannot be null/empty
   * @throws UniqueProductRefException
   *           if productRef cannot be uniquely serialized
   */
  public String serialize(IUniqueProductRef productRef) throws UniqueProductRefException;

  /**
   * 
   * @param productRef
   *          to be serialized, cannot be null
   * @return a map of the serializer name as key and the serialized productRef as value
   *         (cannot be null/empty)
   */
  public Map<String, String> serialize(IProductRef productRef);

  /**
   * 
   * @param productRef
   *          to be serialized, cannot be null
   * @param allowed
   *          list of allowed serializers denoted by name
   * @return a map of the serializer name as key and the serialized productRef as value
   */
  public Map<String, String> serialize(IProductRef productRef, Set<String> allowed);

}
