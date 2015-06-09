package com.celements.product.serialization;

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
import com.celements.product.IProductRef;
import com.celements.product.IUniqueProductRef;
import com.celements.product.UniqueProductRefException;

@Component
@InstantiationStrategy(ComponentInstantiationStrategy.SINGLETON)
public class ProductRefSerializerManager 
    extends AbstractManager<IProductRef, IProductRefSerializerRole> 
    implements IProductRefSerializerManagerRole {

  private static final Logger LOGGER = LoggerFactory.getLogger(
      ProductRefSerializerManager.class);

  @Override
  protected Class<IProductRefSerializerRole> getManageeClass() {
    return IProductRefSerializerRole.class;
  }

  @Override
  public String serialize(IUniqueProductRef productRef) throws UniqueProductRefException {
    List<IProductRefSerializerRole> serializers = getManagees(productRef.getClass());
    if (serializers.size() > 0) {
      return serializers.get(0).serialize(productRef);
    } else {
      throw new UniqueProductRefException("No serializer for ref '" + productRef + "'");
    }
  }

  @Override
  public Map<String, String> serialize(IProductRef productRef) {
    return serialize(productRef, null);
  }

  @Override
  public Map<String, String> serialize(IProductRef productRef, Set<String> allowed) {
    Map<String, String> ret = new HashMap<String, String>();
    for (IProductRefSerializerRole serializer : getManagees(productRef.getClass(), 
        allowed)) {
      ret.put(serializer.getName(), serializer.serialize(productRef));
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
