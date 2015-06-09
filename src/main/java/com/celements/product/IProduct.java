package com.celements.product;

import java.util.List;

import org.xwiki.component.annotation.ComponentRole;

@ComponentRole
public interface IProduct {

  public List<IProductRef> getProductRefs();

  public List<IUniqueProductRef> getUniqueProductRefs();

  public String getTitle();

  public String getSubTitle();

  public String getProducerName();

  public String getDescription();

  public String getAdditonalDescription();

  public double getPrice();

  public int getVATCode();

  public float getVATValue();

  public String getVATDisplay();

}
