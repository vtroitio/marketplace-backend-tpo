package com.uade.tpo.grupo7.marketplace.products.seeder;

import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.uade.tpo.grupo7.marketplace.products.entity.AttributeValue;
import com.uade.tpo.grupo7.marketplace.products.entity.Category;
import com.uade.tpo.grupo7.marketplace.products.entity.Product;
import com.uade.tpo.grupo7.marketplace.products.entity.ProductVariant;
import com.uade.tpo.grupo7.marketplace.products.entity.VariantAttributeValue;
import com.uade.tpo.grupo7.marketplace.products.repository.AttributeValueRepository;
import com.uade.tpo.grupo7.marketplace.products.repository.CategoryRepository;
import com.uade.tpo.grupo7.marketplace.products.repository.ProductRepository;

@Component
@Order(5)
public class ProductSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final AttributeValueRepository attributeValueRepository;

    public ProductSeeder(
        ProductRepository productRepository,
        CategoryRepository categoryRepository,
        AttributeValueRepository attributeValueRepository
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.attributeValueRepository = attributeValueRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() != 0) {
            return;
        }

        Category hombre = findCategory("HOMBRE");
        Category mujer = findCategory("MUJER");
        Category hombreRemera = findCategory("HOMBRE_REMERA");
        Category hombreShort = findCategory("HOMBRE_SHORT");
        Category mujerRemera = findCategory("MUJER_REMERA");
        Category mujerPantalon = findCategory("MUJER_PANTALON");
        Category mujerTop = findCategory("MUJER_TOP");

        productRepository.saveAll(List.of(
                product(
                        "Remera Naruto Akatsuki",
                        19999.0,
                        "Remera de algodon para hombre con estampa de Naruto",
                        Set.of(hombre, hombreRemera),
                        List.of(
                                variant("REM-NAR-NEG-S", 19999.0, 10, List.of("TALLE_S", "COLOR_NEGRO")),
                                variant("REM-NAR-NEG-M", 19999.0, 8, List.of("TALLE_M", "COLOR_NEGRO")),
                                variant("REM-NAR-BLA-L", 19999.0, 6, List.of("TALLE_L", "COLOR_BLANCO")),
                                variant("REM-NAR-BLA-XL", 19999.0, 5, List.of("TALLE_XL", "COLOR_BLANCO"))
                        )
                ),
                product(
                        "Short Dragon Ball Goku",
                        15999.0,
                        "Short para hombre con diseno inspirado en Dragon Ball",
                        Set.of(hombre, hombreShort),
                        List.of(
                                variant("SHO-GOK-AZU-S", 15999.0, 12, List.of("TALLE_S", "COLOR_AZUL")),
                                variant("SHO-GOK-AZU-M", 15999.0, 10, List.of("TALLE_M", "COLOR_AZUL")),
                                variant("SHO-GOK-NAR-L", 15999.0, 7, List.of("TALLE_L", "COLOR_NARANJA")),
                                variant("SHO-GOK-NAR-XL", 15999.0, 4, List.of("TALLE_XL", "COLOR_NARANJA"))
                        )
                ),
                product(
                        "Pantalon Pokemon Retro",
                        24999.0,
                        "Pantalon para mujer con diseno retro de Pokemon",
                        Set.of(mujer, mujerPantalon),
                        List.of(
                                variant("PAN-PKM-NEG-S", 24999.0, 6, List.of("TALLE_S", "COLOR_NEGRO")),
                                variant("PAN-PKM-NEG-M", 24999.0, 9, List.of("TALLE_M", "COLOR_NEGRO")),
                                variant("PAN-PKM-GRI-L", 24999.0, 5, List.of("TALLE_L", "COLOR_GRIS")),
                                variant("PAN-PKM-GRI-XL", 24999.0, 3, List.of("TALLE_XL", "COLOR_GRIS"))
                        )
                ),
                product(
                        "Remera One Piece Luffy",
                        20999.0,
                        "Remera para mujer con estampa de One Piece",
                        Set.of(mujer, mujerRemera),
                        List.of(
                                variant("REM-LUF-NEG-S", 20999.0, 11, List.of("TALLE_S", "COLOR_NEGRO")),
                                variant("REM-LUF-NEG-M", 20999.0, 10, List.of("TALLE_M", "COLOR_NEGRO")),
                                variant("REM-LUF-BLA-L", 20999.0, 8, List.of("TALLE_L", "COLOR_BLANCO")),
                                variant("REM-LUF-BLA-XL", 20999.0, 6, List.of("TALLE_XL", "COLOR_BLANCO"))
                        )
                ),
                product(
                        "Top Sailor Scouts",
                        18999.0,
                        "Top para mujer inspirado en Sailor Moon",
                        Set.of(mujer, mujerTop),
                        List.of(
                                variant("TOP-SM-ROS-S", 18999.0, 5, List.of("TALLE_S", "COLOR_ROSA")),
                                variant("TOP-SM-ROS-M", 18999.0, 7, List.of("TALLE_M", "COLOR_ROSA")),
                                variant("TOP-SM-BLA-L", 18999.0, 4, List.of("TALLE_L", "COLOR_BLANCO")),
                                variant("TOP-SM-BLA-XL", 18999.0, 3, List.of("TALLE_XL", "COLOR_BLANCO"))
                        )
                )
        ));
    }

    private Category findCategory(String code) {
        return categoryRepository.findByCode(code)
                .orElseThrow(() -> new IllegalStateException("Category not found: " + code));
    }

    private AttributeValue findAttributeValue(String code) {
        return attributeValueRepository.findByCode(code)
                .orElseThrow(() -> new IllegalStateException("Attribute value not found: " + code));
    }

    private ProductVariant variant(String sku, Double price, Integer stock, List<String> attributeValueCodes) {
        ProductVariant variant = ProductVariant.builder()
                .sku(sku)
                .price(price)
                .stock(stock)
                .build();

        variant.setAttributeValues(
                attributeValueCodes.stream()
                        .map(this::findAttributeValue)
                        .map(attributeValue -> VariantAttributeValue.builder()
                                .variant(variant)
                                .attributeValue(attributeValue)
                                .build())
                        .toList()
        );

        return variant;
    }

    private Product product(
        String name,
        Double price,
        String description,
        Set<Category> categories,
        List<ProductVariant> variants
    ) {
        Product product = Product.builder()
                .name(name)
                .price(price)
                .description(description)
                .categories(categories)
                .variants(variants)
                .build();

        variants.forEach(variant -> variant.setProduct(product));
        return product;
    }
}
