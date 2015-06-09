package com.celements.product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.xwiki.component.annotation.Requirement;
import org.xwiki.component.manager.ComponentLookupException;
import org.xwiki.component.manager.ComponentManager;
import org.xwiki.component.phase.Initializable;
import org.xwiki.component.phase.InitializationException;

public abstract class AbstractManager<K, T extends IManagee<K>> implements Initializable {

  @Requirement
  private ComponentManager componentManager;

  private Map<Class<? extends K>, List<T>> map;

  @Override
  public void initialize() throws InitializationException {
    map = new HashMap<Class<? extends K>, List<T>>();
    for (T managee : loadManagees()) {
      for (Class<? extends K> clazz : managee.getSupportedClasses()) {
        if (!uniqueCheck(clazz)) {
          getManageesFromMap(clazz).add(managee);
        } else {
          getLogger().error("failed to initialize unique key '{}' for managee '{}'", 
              clazz, managee);
        }
      }
    }
  }

  private List<T> loadManagees() throws InitializationException {
    try {
      return componentManager.lookupList(getManageeClass());
    } catch (ComponentLookupException cle) {
      throw new InitializationException("unable to look up managee class", cle);
    }
  }

  private boolean uniqueCheck(Class<? extends K> clazz) {
    return shouldBeUnique(clazz) && (getManageesFromMap(clazz).size() == 1);
  }

  private List<T> getManageesFromMap(Class<? extends K> clazz) {
    if (!map.containsKey(clazz)) {
      map.put(clazz, new ArrayList<T>());
    }
    return map.get(clazz);
  }

  protected abstract boolean shouldBeUnique(Class<? extends K> clazz);

  protected List<T> getManagees(Class<? extends K> clazz) {
    return getManagees(clazz, null);
  }

  protected List<T> getManagees(Class<? extends K> clazz, Set<String> allowed) {
    List<T> ret = new ArrayList<T>();
    if (allowed == null) {
      ret = getManageesFromMap(clazz);
    } else if (allowed.size() == 1) {
      String hint = allowed.iterator().next();
      try {
        ret.add(componentManager.lookup(getManageeClass(), hint));
      } catch (ComponentLookupException cle) {
        getLogger().warn("getManagees: failed getting class '{}' with hint '{}'", 
            getManageeClass(), hint);
      }          
    } else if (allowed.size() > 1) {
      for (T managee : getManagees(clazz)) {
        if (allowed.contains(managee.getName())) {
          ret.add(managee);
        }
      }
    }
    return Collections.unmodifiableList(ret);
  }

  protected abstract Class<T> getManageeClass();

  protected abstract Logger getLogger();

}
