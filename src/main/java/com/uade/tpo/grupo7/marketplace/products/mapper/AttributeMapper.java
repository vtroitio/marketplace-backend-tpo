package com.uade.tpo.grupo7.marketplace.products.mapper;

import java.util.Comparator;
import java.util.List;

import com.uade.tpo.grupo7.marketplace.products.dto.AttributeResponse;
import com.uade.tpo.grupo7.marketplace.products.dto.AttributeValueResponse;
import com.uade.tpo.grupo7.marketplace.products.entity.Attribute;
import com.uade.tpo.grupo7.marketplace.products.entity.AttributeValue;

public class AttributeMapper {

    public static AttributeResponse toResponse(Attribute attribute) {
        return new AttributeResponse(
                attribute.getId(),
                attribute.getName(),
                attribute.getCode(),
                mapValues(attribute.getValues())
        );
    }

    private static List<AttributeValueResponse> mapValues(List<AttributeValue> values) {
        if (values == null) {
            return List.of();
        }

        return values.stream()
                .sorted(Comparator.comparing(AttributeValue::getId))
                .map(value -> new AttributeValueResponse(
                        value.getId(),
                        value.getValue(),
                        value.getCode()
                ))
                .toList();
    }
}
