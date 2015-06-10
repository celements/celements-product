package com.celements.product.catalogue;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.xwiki.component.descriptor.DefaultComponentDescriptor;

import com.celements.common.test.AbstractBridgedComponentTestCase;
import com.celements.product.IProduct;
import com.celements.product.IProductRef;
import com.celements.product.IUniqueProductRef;
import com.celements.product.TestProductRef;
import com.celements.product.TestUniqueProductRef;
import com.celements.product.UniqueProductRefException;
import com.google.common.collect.ImmutableSet;
import com.xpn.xwiki.web.Utils;

public class CatalogueManagerTest extends AbstractBridgedComponentTestCase {

  private CatalogueManager catalogueManager;
  private ICatalogueRole catalogueMock;
  private String catalogueName;

  @Before
  public void setup_CatalogueManagerTest() throws Exception {
    catalogueManager = (CatalogueManager) Utils.getComponent(ICatalogueManagerRole.class);
    catalogueName = "test";
    catalogueMock = createMockAndAddToDefault(ICatalogueRole.class);
    DefaultComponentDescriptor<ICatalogueRole> descr = 
        new DefaultComponentDescriptor<ICatalogueRole>();
    descr.setRole(ICatalogueRole.class);
    descr.setRoleHint(catalogueName);
    Utils.getComponentManager().registerComponent(descr, catalogueMock);
    expect(catalogueMock.getName()).andReturn(catalogueName).anyTimes();
  }

  @Test
  public void testGetProducts_noCatalogue() throws Exception {
    IProductRef ref = new TestProductRef();
    expect(catalogueMock.getSupportedClasses()).andReturn(
        ImmutableSet.<Class<? extends IProductRef>>of()).anyTimes();
    
    replayDefault();
    catalogueManager.initialize();
    Map<String, List<IProduct>> productMap = catalogueManager.getProducts(ref);
    verifyDefault();
    
    assertEquals(0, productMap.size());
  }

  @Test
  public void testGetProducts_ProductRetrievalException() throws Exception {
    IProductRef ref = new TestProductRef();
    expect(catalogueMock.getSupportedClasses()).andReturn(
        ImmutableSet.<Class<? extends IProductRef>>of(TestProductRef.class)).anyTimes();
    expect(catalogueMock.getProducts(same(ref))).andThrow(new ProductRetrievalException()
        ).once();
    
    replayDefault();
    catalogueManager.initialize();
    try {
      catalogueManager.getProducts(ref);
      fail("expecting ProductRetrievalException");
    } catch (ProductRetrievalException exc) {
      // expected
    }
    verifyDefault();
  }

  @Test
  public void testGetProducts() throws Exception {
    IProductRef ref = new TestProductRef();
    expect(catalogueMock.getSupportedClasses()).andReturn(
        ImmutableSet.<Class<? extends IProductRef>>of(TestProductRef.class)).anyTimes();
    List<IProduct> products = new ArrayList<IProduct>();
    expect(catalogueMock.getProducts(same(ref))).andReturn(products).once();
    
    replayDefault();
    catalogueManager.initialize();
    Map<String, List<IProduct>> productMap = catalogueManager.getProducts(ref);
    verifyDefault();
    
    assertEquals(1, productMap.size());
    assertSame(products, productMap.get(catalogueName));
  }

  @Test
  public void testGetProducts_unique() throws Exception {
    IProductRef ref = new TestUniqueProductRef();
    expect(catalogueMock.getSupportedClasses()).andReturn(
        ImmutableSet.<Class<? extends IProductRef>>of(TestUniqueProductRef.class)
        ).anyTimes();
    List<IProduct> products = new ArrayList<IProduct>();
    expect(catalogueMock.getProducts(same(ref))).andReturn(products).once();
    
    
    replayDefault();
    catalogueManager.initialize();
    Map<String, List<IProduct>> productMap = catalogueManager.getProducts(ref);
    verifyDefault();
    
    assertEquals(1, productMap.size());
    assertSame(products, productMap.get(catalogueName));
  }

  @Test
  public void testGetProduct_noCatalogue() throws Exception {
    IUniqueProductRef ref = new TestUniqueProductRef();
    expect(catalogueMock.getSupportedClasses()).andReturn(
        ImmutableSet.<Class<? extends IProductRef>>of()).anyTimes();
    
    replayDefault();
    catalogueManager.initialize();
    try {
      catalogueManager.getProduct(ref);
      fail("expecting UniqueProductRefException");
    } catch (UniqueProductRefException exc) {
      // expected
    }
    verifyDefault();
  }

  @Test
  public void testGetProduct_wrongCatalogue() throws Exception {
    IUniqueProductRef ref = new TestUniqueProductRef();
    expect(catalogueMock.getSupportedClasses()).andReturn(
        ImmutableSet.<Class<? extends IProductRef>>of(TestProductRef.class)).anyTimes();
    
    replayDefault();
    catalogueManager.initialize();
    try {
      catalogueManager.getProduct(ref);
      fail("expecting UniqueProductRefException");
    } catch (UniqueProductRefException exc) {
      // expected
    }
    verifyDefault();
  }

  @Test
  public void testGetProduct_ProductRetrievalException() throws Exception {
    IUniqueProductRef ref = new TestUniqueProductRef();
    expect(catalogueMock.getSupportedClasses()).andReturn(
        ImmutableSet.<Class<? extends IProductRef>>of(TestUniqueProductRef.class)
        ).anyTimes();
    expect(catalogueMock.getProduct(same(ref))).andThrow(new ProductRetrievalException()
        ).once();
    
    replayDefault();
    catalogueManager.initialize();
    try {
      catalogueManager.getProduct(ref);
      fail("expecting ProductRetrievalException");
    } catch (ProductRetrievalException exc) {
      // expected
    }
    verifyDefault();
  }

  @Test
  public void testGetProduct() throws Exception {
    IUniqueProductRef ref = new TestUniqueProductRef();
    expect(catalogueMock.getSupportedClasses()).andReturn(
        ImmutableSet.<Class<? extends IProductRef>>of(TestUniqueProductRef.class)
        ).anyTimes();
    IProduct product = createMockAndAddToDefault(IProduct.class);
    expect(catalogueMock.getProduct(same(ref))).andReturn(product).once();
    
    replayDefault();
    catalogueManager.initialize();
    IProduct ret = catalogueManager.getProduct(ref);
    verifyDefault();
    
    assertSame(product, ret);
  }
  
}