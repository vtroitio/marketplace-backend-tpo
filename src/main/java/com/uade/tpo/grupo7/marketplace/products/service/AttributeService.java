package com.uade.tpo.grupo7.marketplace.products.service;

import java.util.List;

import com.uade.tpo.grupo7.marketplace.products.entity.Attribute;

public interface AttributeService {

    List<Attribute> getAttributes();

    Attribute getAttributeById(Long attributeId);
}
