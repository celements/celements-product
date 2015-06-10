package com.celements.product.resolving;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.celements.common.test.AbstractBridgedComponentTestCase;
import com.celements.product.IProductRef;
import com.celements.product.IUniqueProductRef;
import com.celements.product.UniqueProductRefException;
import com.xpn.xwiki.web.Utils;

public class ProductRefResolverManagerTest extends AbstractBridgedComponentTestCase {

  private ProductRefResolverManager resolverManager;
  private IProductRefResolverRole resolverMock;
  private IUniqueProductRefResolverRole uniqueResolverMock;
  private String resolverName;
  private String uniqueResolverName;

  private List<IProductRefResolverRole> resolversBackup;
  private List<IUniqueProductRefResolverRole> uniqueResolversBackup;

  @Before
  public void setup_ProductRefResolverManagerTest() throws Exception {
    resolverName = "test";
    resolverMock = createMockAndAddToDefault(IProductRefResolverRole.class);
    expect(resolverMock.getName()).andReturn(resolverName).anyTimes();
    uniqueResolverName = "testUnique";
    uniqueResolverMock = createMockAndAddToDefault(IUniqueProductRefResolverRole.class);
    expect(uniqueResolverMock.getName()).andReturn(uniqueResolverName).anyTimes();
    resolverManager = (ProductRefResolverManager) Utils.getComponent(
        IProductRefResolverManagerRole.class);
    resolversBackup = resolverManager.resolvers;
    uniqueResolversBackup = resolverManager.unqiueResolvers;
  }

  @After
  public void tearDown_ProductRefResolverManagerTest() throws Exception {
    resolverManager.resolvers = resolversBackup;
    resolverManager.unqiueResolvers = uniqueResolversBackup;
  }

  @Test
  public void testResolve() throws Exception {
    resolverManager.resolvers = Arrays.asList(resolverMock);
    String serializedRef = "asdf";
    IProductRef ref = createMockAndAddToDefault(IProductRef.class);
    expect(resolverMock.resolve(same(serializedRef))).andReturn(ref).once();
    
    replayDefault();
    Map<String, IProductRef> ret = resolverManager.resolve(serializedRef);
    verifyDefault();
    
    assertEquals(1, ret.size());
    assertSame(ref, ret.get(resolverName));
  }

  @Test
  public void testResolve_multiple() throws Exception {
    resolverManager.resolvers = Arrays.asList(resolverMock, uniqueResolverMock);
    String serializedRef = "asdf";
    IProductRef ref = createMockAndAddToDefault(IProductRef.class);
    expect(resolverMock.resolve(same(serializedRef))).andReturn(ref).once();
    IUniqueProductRef uniqueRef = createMockAndAddToDefault(IUniqueProductRef.class);
    expect(uniqueResolverMock.resolve(same(serializedRef))).andReturn(uniqueRef).once();
    
    replayDefault();
    Map<String, IProductRef> ret = resolverManager.resolve(serializedRef);
    verifyDefault();
    
    assertEquals(2, ret.size());
    assertSame(ref, ret.get(resolverName));
    assertSame(uniqueRef, ret.get(uniqueResolverName));
  }

  @Test
  public void testResolve_notResolved() throws Exception {
    resolverManager.resolvers = Arrays.asList(resolverMock);
    String serializedRef = "asdf";
    expect(resolverMock.resolve(same(serializedRef))).andReturn(null).once();
    
    replayDefault();
    Map<String, IProductRef> ret = resolverManager.resolve(serializedRef);
    verifyDefault();
    
    assertEquals(0, ret.size());
  }

  @Test
  public void testResolve_noResolvers() throws Exception {
    resolverManager.resolvers = Collections.emptyList();
    String serializedRef = "asdf";
    
    replayDefault();
    Map<String, IProductRef> ret = resolverManager.resolve(serializedRef);
    verifyDefault();
    
    assertEquals(0, ret.size());
  }

  @Test
  public void testResolveUnique() throws Exception {
    resolverManager.unqiueResolvers = Arrays.asList(uniqueResolverMock);
    String serializedRef = "asdf";
    IUniqueProductRef ref = createMockAndAddToDefault(IUniqueProductRef.class);
    expect(uniqueResolverMock.resolve(same(serializedRef))).andReturn(ref).once();
    
    replayDefault();
    IUniqueProductRef ret = resolverManager.resolveUnique(serializedRef);
    verifyDefault();
    
    assertSame(ref, ret);
  }

  @Test
  public void testResolveUnique_multiple() throws Exception {
    resolverManager.unqiueResolvers = Arrays.asList(uniqueResolverMock, 
        uniqueResolverMock);
    String serializedRef = "asdf";
    IUniqueProductRef ref = createMockAndAddToDefault(IUniqueProductRef.class);
    expect(uniqueResolverMock.resolve(same(serializedRef))).andReturn(ref).once();
    
    replayDefault();
    try {
      resolverManager.resolveUnique(serializedRef);
      fail("expecting UniqueProductRefException");
    } catch (UniqueProductRefException exc) {
      // expected
    }
    verifyDefault();
  }

  @Test
  public void testResolveUnique_notResolved() throws Exception {
    resolverManager.unqiueResolvers = Arrays.asList(uniqueResolverMock);
    String serializedRef = "asdf";
    expect(uniqueResolverMock.resolve(same(serializedRef))).andReturn(null).once();
    
    replayDefault();
    try {
      resolverManager.resolveUnique(serializedRef);
      fail("expecting UniqueProductRefException");
    } catch (UniqueProductRefException exc) {
      // expected
    }
    verifyDefault();
  }

  @Test
  public void testResolveUnique_notResolers() throws Exception {
    resolverManager.unqiueResolvers = Collections.emptyList();
    String serializedRef = "asdf";
    
    replayDefault();
    try {
      resolverManager.resolveUnique(serializedRef);
      fail("expecting UniqueProductRefException");
    } catch (UniqueProductRefException exc) {
      // expected
    }
    verifyDefault();
  }
  
}
