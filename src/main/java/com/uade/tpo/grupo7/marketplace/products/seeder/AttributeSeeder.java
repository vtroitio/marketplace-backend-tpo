package com.uade.tpo.grupo7.marketplace.products.seeder;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.uade.tpo.grupo7.marketplace.products.entity.Attribute;
import com.uade.tpo.grupo7.marketplace.products.entity.AttributeValue;
import com.uade.tpo.grupo7.marketplace.products.repository.AttributeRepository;

@Component
@Order(4)
public class AttributeSeeder implements CommandLineRunner {

    private final AttributeRepository attributeRepository;

    public AttributeSeeder(AttributeRepository attributeRepository) {
        this.attributeRepository = attributeRepository;
    }

    @Override
    public void run(String... args) {
        if (attributeRepository.count() != 0) {
            return;
        }

        Attribute talle = attributeRepository.save(Attribute.builder().name("Talle").code("TALLE").build());
        Attribute color = attributeRepository.save(Attribute.builder().name("Color").code("COLOR").build());

        attributeRepository.saveAll(List.of(
                attributeWithValues(talle, List.of(
                        value(talle, "S", "TALLE_S"),
                        value(talle, "M", "TALLE_M"),
                        value(talle, "L", "TALLE_L"),
                        value(talle, "XL", "TALLE_XL")
                )),
                attributeWithValues(color, List.of(
                        colorValue(color, "Negro", "COLOR_NEGRO", "#000000"),
                        colorValue(color, "Blanco", "COLOR_BLANCO", "#FFFFFF"),
                        colorValue(color, "Azul", "COLOR_AZUL", "#2563EB"),
                        colorValue(color, "Naranja", "COLOR_NARANJA", "#F97316"),
                        colorValue(color, "Gris", "COLOR_GRIS", "#6B7280"),
                        colorValue(color, "Rosa", "COLOR_ROSA", "#EC4899")
                ))
        ));
    }

    private Attribute attributeWithValues(Attribute attribute, List<AttributeValue> values) {
        attribute.setValues(values);
        return attribute;
    }

    private AttributeValue value(Attribute attribute, String value, String code) {
        return AttributeValue.builder()
                .attribute(attribute)
                .value(value)
                .code(code)
                .build();
    }

    private AttributeValue colorValue(Attribute attribute, String value, String code, String hexColor) {
        return AttributeValue.builder()
                .attribute(attribute)
                .value(value)
                .code(code)
                .hexColor(hexColor)
                .build();
    }
}
