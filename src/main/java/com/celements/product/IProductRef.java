package com.celements.product;

import java.io.Serializable;

import org.xwiki.component.annotation.ComponentRole;

@ComponentRole
public interface IProductRef extends Serializable, Cloneable, Comparable<IProductRef> {

}
