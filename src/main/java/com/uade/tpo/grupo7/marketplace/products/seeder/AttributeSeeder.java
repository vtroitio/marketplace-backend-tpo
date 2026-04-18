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
                        value(color, "Negro", "COLOR_NEGRO"),
                        value(color, "Blanco", "COLOR_BLANCO"),
                        value(color, "Azul", "COLOR_AZUL"),
                        value(color, "Naranja", "COLOR_NARANJA"),
                        value(color, "Gris", "COLOR_GRIS"),
                        value(color, "Rosa", "COLOR_ROSA")
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
}
