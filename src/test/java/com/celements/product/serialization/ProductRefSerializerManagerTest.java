package com.celements.product.serialization;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xwiki.component.descriptor.DefaultComponentDescriptor;

import com.celements.common.test.AbstractBridgedComponentTestCase;
import com.celements.product.IProductRef;
import com.celements.product.IUniqueProductRef;
import com.celements.product.TestProductRef;
import com.celements.product.TestUniqueProductRef;
import com.celements.product.UniqueProductRefException;
import com.google.common.collect.ImmutableSet;
import com.xpn.xwiki.web.Utils;

public class ProductRefSerializerManagerTest extends AbstractBridgedComponentTestCase {

  private ProductRefSerializerManager serializerManager;
  private IProductRefSerializerRole serializerMock;
  private String serializerMockName;

  @Before
  public void setup_ProductRefSerializerManagerTest() throws Exception {
    serializerManager = (ProductRefSerializerManager) Utils.getComponent(
        IProductRefSerializerManagerRole.class);
    serializerMockName = "test";
    serializerMock = createMockAndAddToDefault(IProductRefSerializerRole.class);
    DefaultComponentDescriptor<IProductRefSerializerRole> descr = 
        new DefaultComponentDescriptor<IProductRefSerializerRole>();
    descr.setRole(IProductRefSerializerRole.class);
    descr.setRoleHint(serializerMockName);
    Utils.getComponentManager().registerComponent(descr, serializerMock);
    expect(serializerMock.getName()).andReturn(serializerMockName).anyTimes();
  }

  @After
  public void tearDown_ProductRefSerializerManagerTest() throws Exception {
    Utils.getComponentManager().unregisterComponent(IProductRefSerializerRole.class, 
        serializerMockName);
    serializerManager.initialize();
  }

  @Test
  public void testSerialize_noSerializer() throws Exception {
    IProductRef ref = new TestProductRef();
    expect(serializerMock.getSupportedClasses()).andReturn(
        ImmutableSet.<Class<? extends IProductRef>>of()).anyTimes();
    
    replayDefault();
    serializerManager.initialize();
    Map<String, String> productMap = serializerManager.serialize(ref);
    verifyDefault();
    
    assertEquals(0, productMap.size());
  }

  @Test
  public void testSerialize() throws Exception {
    IProductRef ref = new TestProductRef();
    expect(serializerMock.getSupportedClasses()).andReturn(
        ImmutableSet.<Class<? extends IProductRef>>of(TestProductRef.class)).anyTimes();
    String serializedRef = "asdf";
    expect(serializerMock.serialize(same(ref))).andReturn(serializedRef).once();
    
    replayDefault();
    serializerManager.initialize();
    Map<String, String> productMap = serializerManager.serialize(ref);
    verifyDefault();
    
    assertEquals(1, productMap.size());
    assertSame(serializedRef, productMap.get(serializerMockName));
  }

  @Test
  public void testSerialize_uniqueMap() throws Exception {
    IProductRef ref = new TestUniqueProductRef();
    expect(serializerMock.getSupportedClasses()).andReturn(
        ImmutableSet.<Class<? extends IProductRef>>of(TestUniqueProductRef.class)
        ).anyTimes();
    String serializedRef = "asdf";
    expect(serializerMock.serialize(same(ref))).andReturn(serializedRef).once();
    
    
    replayDefault();
    serializerManager.initialize();
    Map<String, String> productMap = serializerManager.serialize(ref);
    verifyDefault();
    
    assertEquals(1, productMap.size());
    assertSame(serializedRef, productMap.get(serializerMockName));
  }

  @Test
  public void testSerialize_unique_noSerializer() throws Exception {
    IUniqueProductRef ref = new TestUniqueProductRef();
    expect(serializerMock.getSupportedClasses()).andReturn(
        ImmutableSet.<Class<? extends IProductRef>>of()).anyTimes();
    
    replayDefault();
    serializerManager.initialize();
    try {
      serializerManager.serialize(ref);
      fail("expecting UniqueProductRefException");
    } catch (UniqueProductRefException exc) {
      // expected
    }
    verifyDefault();
  }

  @Test
  public void testSerialize_unique_wrongSerializer() throws Exception {
    IUniqueProductRef ref = new TestUniqueProductRef();
    expect(serializerMock.getSupportedClasses()).andReturn(
        ImmutableSet.<Class<? extends IProductRef>>of(TestProductRef.class)).anyTimes();
    
    replayDefault();
    serializerManager.initialize();
    try {
      serializerManager.serialize(ref);
      fail("expecting UniqueProductRefException");
    } catch (UniqueProductRefException exc) {
      // expected
    }
    verifyDefault();
  }

  @Test
  public void testSerialize_unique() throws Exception {
    IUniqueProductRef ref = new TestUniqueProductRef();
    expect(serializerMock.getSupportedClasses()).andReturn(
        ImmutableSet.<Class<? extends IProductRef>>of(TestUniqueProductRef.class)
        ).anyTimes();
    String serializedRef = "asdf";
    expect(serializerMock.serialize(same(ref))).andReturn(serializedRef).once();
    
    replayDefault();
    serializerManager.initialize();
    String ret = serializerManager.serialize(ref);
    verifyDefault();
    
    assertSame(serializedRef, ret);
  }
  
}
