package com.uade.tpo.grupo7.marketplace.products.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.uade.tpo.grupo7.marketplace.products.entity.Attribute;
import com.uade.tpo.grupo7.marketplace.products.repository.AttributeRepository;

@Service
public class AttributeService {

    private final AttributeRepository attributeRepository;

    public AttributeService(AttributeRepository attributeRepository) {
        this.attributeRepository = attributeRepository;
    }

    public List<Attribute> getAttributes() {
        return attributeRepository.findAllByOrderByIdAsc();
    }

    public Attribute getAttributeById(Long attributeId) {
        return attributeRepository.findWithValuesById(attributeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Attribute not found"
                ));
    }
}
