package com.celements.product.catalogue;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.celements.common.test.AbstractComponentTest;
import com.celements.product.IProduct;
import com.celements.product.IProductRef;
import com.celements.product.IUniqueProductRef;
import com.celements.product.TestProductRef;
import com.celements.product.TestUniqueProductRef;
import com.celements.product.UniqueProductRefException;
import com.google.common.collect.ImmutableSet;
import com.xpn.xwiki.web.Utils;

public class CatalogueManagerTest extends AbstractComponentTest {

  private CatalogueManager catalogueManager;
  private ICatalogueRole<IProduct> catalogueMock;
  private String catalogueMockName;

  @Before
  public void setup_CatalogueManagerTest() throws Exception {
    catalogueManager = (CatalogueManager) Utils.getComponent(ICatalogueManagerRole.class);
    Class<ICatalogueRole<IProduct>> clazz = CatalogueManager.getProductCatalogueClass();
    catalogueMockName = "test";
    catalogueMock = registerComponentMock(clazz, catalogueMockName);
    expect(catalogueMock.getName()).andReturn(catalogueMockName).anyTimes();
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
    expect(catalogueMock.getProducts(same(ref))).andThrow(new ProductRetrievalException()).once();

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
    List<IProduct> products = new ArrayList<>();
    expect(catalogueMock.getProducts(same(ref))).andReturn(products).once();

    replayDefault();
    catalogueManager.initialize();
    Map<String, List<IProduct>> productMap = catalogueManager.getProducts(ref);
    verifyDefault();

    assertEquals(1, productMap.size());
    assertSame(products, productMap.get(catalogueMockName));
  }

  @Test
  public void testGetProducts_unique() throws Exception {
    IProductRef ref = new TestUniqueProductRef();
    expect(catalogueMock.getSupportedClasses()).andReturn(
        ImmutableSet.<Class<? extends IProductRef>>of(TestUniqueProductRef.class)).anyTimes();
    List<IProduct> products = new ArrayList<>();
    expect(catalogueMock.getProducts(same(ref))).andReturn(products).once();

    replayDefault();
    catalogueManager.initialize();
    Map<String, List<IProduct>> productMap = catalogueManager.getProducts(ref);
    verifyDefault();

    assertEquals(1, productMap.size());
    assertSame(products, productMap.get(catalogueMockName));
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
        ImmutableSet.<Class<? extends IProductRef>>of(TestUniqueProductRef.class)).anyTimes();
    expect(catalogueMock.getProduct(same(ref))).andThrow(new ProductRetrievalException()).once();

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
        ImmutableSet.<Class<? extends IProductRef>>of(TestUniqueProductRef.class)).anyTimes();
    IProduct product = createDefaultMock(IProduct.class);
    expect(catalogueMock.getProduct(same(ref))).andReturn(product).once();

    replayDefault();
    catalogueManager.initialize();
    IProduct ret = catalogueManager.getProduct(ref);
    verifyDefault();

    assertSame(product, ret);
  }

}
