package com.celements.product.serialization;

import org.xwiki.component.annotation.ComponentRole;

import com.celements.product.IManagee;
import com.celements.product.IProductRef;

@ComponentRole
public interface IProductRefSerializerRole extends IManagee<IProductRef> {

  public String getName();

  /**
   * @param productRef
   *          to be serialized, must be of type from {@link #getSupportedClasses()} and
   *          cannot be null
   * @return the serialized productRef as String, cannot be null/empty
   */
  public String serialize(IProductRef productRef);

}
