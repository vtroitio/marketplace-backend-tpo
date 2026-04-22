package com.uade.tpo.grupo7.marketplace.products.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.grupo7.marketplace.products.dto.AttributeResponse;
import com.uade.tpo.grupo7.marketplace.products.entity.Attribute;
import com.uade.tpo.grupo7.marketplace.products.mapper.AttributeMapper;
import com.uade.tpo.grupo7.marketplace.products.service.AttributeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("attributes")
@Tag(name = "Attributes", description = "Endpoints de atributos para variantes del marketplace")
public class AttributeController {

    private final AttributeService attributeService;

    public AttributeController(AttributeService attributeService) {
        this.attributeService = attributeService;
    }

    @GetMapping
    @Operation(
        summary = "Obtener todos los atributos",
        description = "Obtiene la lista de atributos disponibles con sus valores, como talles y colores"
    )
    public List<AttributeResponse> getAttributes() {
        return attributeService.getAttributes()
                .stream()
                .map(AttributeMapper::toResponse)
                .toList();
    }

    @GetMapping("{attributeId}")
    @Operation(
        summary = "Obtener un atributo por ID",
        description = "Obtiene un atributo especifico con todos sus valores disponibles"
    )
    public AttributeResponse getAttributeById(@PathVariable Long attributeId) {
        Attribute attribute = attributeService.getAttributeById(attributeId);
        return AttributeMapper.toResponse(attribute);
    }
}
