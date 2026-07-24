package com.uade.tpo.grupo7.marketplace.products.seeder;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.uade.tpo.grupo7.marketplace.products.entity.AttributeValue;
import com.uade.tpo.grupo7.marketplace.products.entity.Category;
import com.uade.tpo.grupo7.marketplace.products.entity.Product;
import com.uade.tpo.grupo7.marketplace.products.entity.ProductImage;
import com.uade.tpo.grupo7.marketplace.products.entity.ProductVariant;
import com.uade.tpo.grupo7.marketplace.products.entity.VariantAttributeValue;
import com.uade.tpo.grupo7.marketplace.products.repository.AttributeValueRepository;
import com.uade.tpo.grupo7.marketplace.products.repository.CategoryRepository;
import com.uade.tpo.grupo7.marketplace.products.repository.ProductRepository;
import com.uade.tpo.grupo7.marketplace.users.repository.UserRepository;

@Component
@Order(5)
public class ProductSeeder implements CommandLineRunner {

        private final ProductRepository productRepository;
        private final CategoryRepository categoryRepository;
        private final AttributeValueRepository attributeValueRepository;
        private final UserRepository userRepository;

        private static final String IMAGE_BASE_URL = "http://localhost:8080/uploads/products/";

        public ProductSeeder(
                        ProductRepository productRepository,
                        CategoryRepository categoryRepository,
                        AttributeValueRepository attributeValueRepository,
                        UserRepository userRepository) {
                this.productRepository = productRepository;
                this.categoryRepository = categoryRepository;
                this.attributeValueRepository = attributeValueRepository;
                this.userRepository = userRepository;
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
                Category hombrePantalon = findCategory("HOMBRE_PANTALON");
                Category mujerRemera = findCategory("MUJER_REMERA");
                Category mujerPantalon = findCategory("MUJER_PANTALON");
                Category mujerTop = findCategory("MUJER_TOP");

                Product remNaruto = product(
                                "Remera Naruto Uzumaki",
                                19999.0,
                                "Remera de algodon para hombre con estampa de Naruto",
                                Set.of(hombre, hombreRemera),
                                List.of(
                                                variant("REM-NAR-NEG-S", 19999.0, 10,
                                                                List.of("TALLE_S", "COLOR_NEGRO")),
                                                variant("REM-NAR-NEG-M", 19999.0, 8,
                                                                List.of("TALLE_M", "COLOR_NEGRO")),
                                                variant("REM-NAR-NEG-L", 19999.0, 8,
                                                                List.of("TALLE_L", "COLOR_NEGRO"))
                                // variant("REM-NAR-BLA-L", 19999.0, 6,
                                // List.of("TALLE_L", "COLOR_BLANCO")),
                                // variant("REM-NAR-BLA-XL", 19999.0, 5,
                                // List.of("TALLE_XL", "COLOR_BLANCO"))
                                ));

                Product shortDragonBall = product(
                                "Short Dragon Ball",
                                15999.0,
                                "Short para hombre con diseno inspirado en Dragon Ball",
                                Set.of(hombre, hombreShort),
                                List.of(
                                                variant("SHO-GOK-GRI-L", 15999.0, 7,
                                                                List.of("TALLE_L", "COLOR_GRIS")),
                                                variant("SHO-GOK-GRI-XL", 15999.0, 4,
                                                                List.of("TALLE_XL", "COLOR_GRIS")),
                                                variant("SHO-GOK-NEG-S", 15999.0, 12,
                                                                List.of("TALLE_S", "COLOR_NEGRO")),
                                                variant("SHO-GOK-NEG-M", 15999.0, 10,
                                                                List.of("TALLE_M", "COLOR_NEGRO"))));

                Product pantalonPokemon = product(
                                "Pantalon Pokemon Gengar",
                                24999.0,
                                "Pantalon para hombre con diseno retro de Pokemon",
                                Set.of(hombre, hombrePantalon),
                                List.of(
                                                variant("PAN-PKM-BLA-L", 24999.0, 5,
                                                                List.of("TALLE_L", "COLOR_BLANCO")),
                                                variant("PAN-PKM-BLA-XL", 24999.0, 3,
                                                                List.of("TALLE_XL", "COLOR_BLANCO")),
                                                variant("PAN-PKM-NEG-S", 24999.0, 6,
                                                                List.of("TALLE_S", "COLOR_NEGRO")),
                                                variant("PAN-PKM-NEG-M", 24999.0, 9,
                                                                List.of("TALLE_M", "COLOR_NEGRO"))));

                Product topOnePice = product(
                                "Top One Piece Luffy",
                                20999.0,
                                "Top para mujer con estampa de One Piece",
                                Set.of(mujer, mujerTop),
                                List.of(
                                                variant("TOP-LUF-BLA-L", 20999.0, 8,
                                                                List.of("TALLE_L", "COLOR_BLANCO")),
                                                variant("TOP-LUF-BLA-XL", 30999.0, 6,
                                                                List.of("TALLE_XL", "COLOR_BLANCO"))));

                Product topSailorMoon = product(
                                "Top Sailor Scouts",
                                18999.0,
                                "Top para mujer inspirado en Sailor Moon",
                                Set.of(mujer, mujerTop),
                                List.of(
                                                variant("TOP-SM-ROS-S", 18999.0, 5,
                                                                List.of("TALLE_S", "COLOR_ROSA")),
                                                variant("TOP-SM-ROS-M", 18999.0, 7,
                                                                List.of("TALLE_M", "COLOR_ROSA"))));

                Product remLevi = product(
                                "Remera Levi Attack on Titan",
                                18999.0,
                                "Remera para hombre inspirada en Attack on Titan",
                                Set.of(hombre, hombreRemera),
                                List.of(
                                                variant("TOP-AOT-VER-S", 18999.0, 5,
                                                                List.of("TALLE_S", "COLOR_VERDE")),
                                                variant("TOP-AOT-VER-M", 18999.0, 7,
                                                                List.of("TALLE_M", "COLOR_VERDE")),
                                                variant("TOP-AOT-BLA-L", 18999.0, 4,
                                                                List.of("TALLE_L", "COLOR_BLANCO")),
                                                variant("TOP-AOT-BLA-XL", 18999.0, 3, List.of("TALLE_XL",
                                                                "COLOR_BLANCO"))));

                Product shortMinecraft = product(
                                "Short Minecraft",
                                15999.0,
                                "Short para hombre con diseno inspirado en Minecraft",
                                Set.of(hombre, hombreShort),
                                List.of(
                                                variant("SHO-MIN-GRI-L", 15999.0, 7,
                                                                List.of("TALLE_L", "COLOR_GRIS")),
                                                variant("SHO-MIN-GRI-XL", 15999.0, 4,
                                                                List.of("TALLE_XL", "COLOR_GRIS")),
                                                variant("SHO-MIN-NEG-L", 15999.0, 7,
                                                                List.of("TALLE_L", "COLOR_NEGRO")),
                                                variant("SHO-MIN-NEG-XL", 15999.0, 4,
                                                                List.of("TALLE_XL", "COLOR_NEGRO"))));

                Product remeraBatman = product(
                                "Remera Batman",
                                99999.0,
                                "Remera de hombre inspirada en Batman",
                                Set.of(hombre, hombreRemera),
                                List.of(
                                                variant("REM-BAT-ROJ-XL", 99999.0, 9,
                                                                List.of("TALLE_XL", "COLOR_ROJO")),
                                                variant("REM-BAT-NEG-XL", 99999.0, 8,
                                                                List.of("TALLE_XL", "COLOR_NEGRO")),
                                                variant("REM-BAT-AMA-XL", 99999.0, 6,
                                                                List.of("TALLE_XL", "COLOR_AMARILLO")),
                                                variant("REM-BAT-ROS-XL", 99999.0, 6,
                                                                List.of("TALLE_XL", "COLOR_ROSA"))));

                Product remeraSpiderMan = product(
                                "Remera Spider-Man",
                                19999.0,
                                "Remera de hombre inspirada en Spider-Man",
                                Set.of(hombre, hombreRemera),
                                List.of(
                                                variant("REM-SPM-BLA-S", 19999.0, 10,
                                                                List.of("TALLE_S", "COLOR_BLANCO")),
                                                variant("REM-SPM-BLA-M", 29999.0, 8,
                                                                List.of("TALLE_M", "COLOR_BLANCO")),
                                                variant("REM-SPM-NEG-L", 24999.0, 6,
                                                                List.of("TALLE_L", "COLOR_NEGRO"))));

                Product remeraNarutoKonoha = product(
                                "Remera Naruto Konoha",
                                20999.0,
                                "Remera de anime de Naruto con simbolo de Konoha",
                                Set.of(mujer, mujerRemera),
                                List.of(
                                                variant("REM-NRT-BEI-S", 20999.0, 7,
                                                                List.of("TALLE_S", "COLOR_BEIGE")),
                                                variant("REM-NRT-BEI-M", 20999.0, 7,
                                                                List.of("TALLE_M", "COLOR_BEIGE")),
                                                variant("REM-NRT-NEG-L", 20999.0, 5,
                                                                List.of("TALLE_L", "COLOR_NEGRO"))));

                Product pantalonSonic = product(
                                "Pantalon Sonic",
                                12999.0,
                                "Pantalon inspirado en Sonic the Hedgehog",
                                Set.of(hombre, hombrePantalon),
                                List.of(
                                                variant("PAN-SNC-AZU-S", 12999.0, 6,
                                                                List.of("TALLE_S", "COLOR_AZUL")),
                                                variant("PAN-SNC-AZU-M", 22999.0, 6,
                                                                List.of("TALLE_M", "COLOR_AZUL")),
                                                variant("PAN-SNC-AZU-L", 32999.0, 4,
                                                                List.of("TALLE_L", "COLOR_AZUL"))));

                Product topPikachu = product(
                                "Top Pokemon Pikachu",
                                17999.0,
                                "Top para mujer con diseño de Pikachu",
                                Set.of(mujer, mujerTop),
                                List.of(
                                                variant("TOP-PKM-AMA-S", 17999.0, 6,
                                                                List.of("TALLE_S", "COLOR_AMARILLO")),
                                                variant("TOP-PKM-AMA-M", 21999.0, 6,
                                                                List.of("TALLE_M", "COLOR_AMARILLO")),
                                                variant("TOP-PKM-AMA-L", 24999.0, 5,
                                                                List.of("TALLE_L", "COLOR_AMARILLO"))));

                Product pantalonFinalFantasy = product(
                                "Pantalon Final Fantasy",
                                23999.0,
                                "Pantalon gamer inspirado en Final Fantasy",
                                Set.of(mujer, mujerPantalon),
                                List.of(
                                                variant("PAN-FFF-NEG-S", 23999.0, 5,
                                                                List.of("TALLE_S", "COLOR_NEGRO")),
                                                variant("PAN-FFF-NEG-M", 33999.0, 5,
                                                                List.of("TALLE_M", "COLOR_NEGRO")),
                                                variant("PAN-FFF-BLA-L", 23999.0, 4,
                                                                List.of("TALLE_L", "COLOR_BLANCO"))));

                Product remeraGoku = product(
                                "Remera Goku",
                                20999.0,
                                "Remera inspirada en Dragon Ball Z",
                                Set.of(hombre, hombreRemera),
                                List.of(
                                                variant("REM-GOK-NAR-S", 20999.0, 9,
                                                                List.of("TALLE_S", "COLOR_NARANJA")),
                                                variant("REM-GOK-NAR-M", 20999.0, 9,
                                                                List.of("TALLE_M", "COLOR_NARANJA")),
                                                variant("REM-GOK-AZU-L", 20999.0, 6,
                                                                List.of("TALLE_L", "COLOR_AZUL"))));

                Product shortMario = product(
                                "Short Mario Bros",
                                15999.0,
                                "Short inspirado en Super Mario",
                                Set.of(hombre, hombreShort),
                                List.of(
                                                variant("SHO-MAR-RED-S", 15999.0, 8,
                                                                List.of("TALLE_S", "COLOR_ROJO")),
                                                variant("SHO-MAR-RED-M", 15999.0, 8,
                                                                List.of("TALLE_M", "COLOR_ROJO")),
                                                variant("SHO-MAR-AZU-L", 15999.0, 5,
                                                                List.of("TALLE_L", "COLOR_AZUL"))));

                Product remeraHarryPotter = product(
                                "Remera Harry Potter",
                                19999.0,
                                "Remera de pelicula inspirada en Harry Potter",
                                Set.of(mujer, mujerRemera),
                                List.of(
                                                variant("REM-HPT-NEG-S", 19999.0, 7,
                                                                List.of("TALLE_S", "COLOR_NEGRO")),
                                                variant("REM-HPT-NEG-M", 19999.0, 7,
                                                                List.of("TALLE_M", "COLOR_NEGRO")),
                                                variant("REM-HPT-BLA-L", 19999.0, 5,
                                                                List.of("TALLE_L", "COLOR_BLANCO"))));

                Product pantalonZelda = product(
                                "Pantalon Zelda",
                                23999.0,
                                "Pantalon inspirado en The Legend of Zelda",
                                Set.of(hombre, hombrePantalon),
                                List.of(
                                                variant("PAN-ZLD-VER-S", 23999.0, 5,
                                                                List.of("TALLE_S", "COLOR_VERDE")),
                                                variant("PAN-ZLD-VER-M", 23999.0, 5,
                                                                List.of("TALLE_M", "COLOR_VERDE")),
                                                variant("PAN-ZLD-VER-L", 23999.0, 4,
                                                                List.of("TALLE_L", "COLOR_VERDE"))));

                Product remeraNarutoAkatsuki = product(
                                "Remera Akatsuki",
                                20999.0,
                                "Remera de anime inspirada en Akatsuki",
                                Set.of(hombre, hombreRemera),
                                List.of(
                                                variant("REM-AKA-BLA-S", 20999.0, 8,
                                                                List.of("TALLE_S", "COLOR_BLANCO")),
                                                variant("REM-AKA-NEG-M", 20999.0, 8,
                                                                List.of("TALLE_M", "COLOR_NEGRO")),
                                                variant("REM-AKA-NEG-L", 20999.0, 6,
                                                                List.of("TALLE_L", "COLOR_NEGRO"))));

                Product topJigglypuff = product(
                                "Top Jigglypuff",
                                17999.0,
                                "Top de mujer inspirado en Jigglypuff",
                                Set.of(mujer, mujerTop),
                                List.of(
                                                variant("TOP-JGL-ROS-S", 17999.0, 6,
                                                                List.of("TALLE_S", "COLOR_ROSA")),
                                                variant("TOP-JGL-ROS-M", 17999.0, 6,
                                                                List.of("TALLE_M", "COLOR_ROSA")),
                                                variant("TOP-JGL-ROS-L", 17999.0, 4,
                                                                List.of("TALLE_L", "COLOR_ROSA"))));

                Product remeraPulpFiction = product(
                                "Remera Pulp Fiction",
                                15999.0,
                                "Remera inspirada en Pulp Fiction",
                                Set.of(hombre, hombreRemera),
                                List.of(
                                                variant("SHO-PULP-NEG-S", 16999.0, 6,
                                                                List.of("TALLE_S", "COLOR_NEGRO")),
                                                variant("SHO-PULP-NEG-M", 16999.0, 6,
                                                                List.of("TALLE_M", "COLOR_NEGRO")),
                                                variant("SHO-PULP-NEG-L", 16999.0, 4,
                                                                List.of("TALLE_L", "COLOR_NEGRO"))));

                saveProductWithImages(
                                remNaruto,
                                Map.of(
                                                "REM-NAR-NEG-S", List.of("naruto-negro-1.webp"),
                                                "REM-NAR-NEG-M", List.of("naruto-negro-1.webp"),
                                                "REM-NAR-NEG-L", List.of("naruto-negro-1.webp"),
                                                "REM-NAR-BLA-L", List.of("naruto-blanca-1.webp"),
                                                "REM-NAR-BLA-XL", List.of("naruto-blanca-1.webp")));
                saveProductWithImages(
                                shortDragonBall,
                                Map.of(
                                                "SHO-GOK-NEG-S", List.of("db-negro-1.webp", "db-negro-2.webp"),
                                                "SHO-GOK-NEG-M", List.of("db-negro-1.webp", "db-negro-2.webp"),
                                                "SHO-GOK-GRI-L", List.of("db-gris-1.webp", "db-gris-2.webp"),
                                                "SHO-GOK-GRI-XL", List.of("db-gris-1.webp", "db-gris-2.webp")));
                saveProductWithImages(
                                pantalonPokemon,
                                Map.of(
                                                "PAN-PKM-NEG-S", List.of("poke-negro-1.webp"),
                                                "PAN-PKM-NEG-M", List.of("poke-negro-1.webp"),
                                                "PAN-PKM-BLA-L", List.of("poke-blanco-1.webp"),
                                                "PAN-PKM-BLA-XL", List.of("poke-blanco-1.webp")));
                saveProductWithImages(
                                topOnePice,
                                Map.of(
                                                "TOP-LUF-BLA-L", List.of("op-blanco-1.png"),
                                                "TOP-LUF-BLA-XL", List.of("op-blanco-1.png")));
                saveProductWithImages(
                                topSailorMoon,
                                Map.of(
                                                "TOP-SM-ROS-S", List.of("sailor-rosa-1.png"),
                                                "TOP-SM-ROS-M", List.of("sailor-rosa-1.png")));
                saveProductWithImages(
                                remLevi,
                                Map.of(
                                                "TOP-AOT-VER-S", List.of("aot-verde-1.png"),
                                                "TOP-AOT-VER-M", List.of("aot-verde-1.png"),
                                                "TOP-AOT-BLA-L", List.of("aot-blanco-1.png"),
                                                "TOP-AOT-BLA-XL", List.of("aot-blanco-1.png")));
                saveProductWithImages(
                                shortMinecraft,
                                Map.of(
                                                "SHO-MIN-GRI-L", List.of("mc-gris-1.png"),
                                                "SHO-MIN-GRI-XL", List.of("mc-gris-1.png"),
                                                "SHO-MIN-NEG-L", List.of("mc-negro-1.png"),
                                                "SHO-MIN-NEG-XL", List.of("mc-negro-1.png")));
                saveProductWithImages(
                                remeraBatman,
                                Map.of(
                                                "REM-BAT-ROJ-XL", List.of("batman-rojo-1.png"),
                                                "REM-BAT-NEG-XL", List.of("batman-negro-1.png"),
                                                "REM-BAT-AMA-XL", List.of("batman-amarillo-1.png"),
                                                "REM-BAT-ROS-XL", List.of("batman-rosa-1.png")
                                        ));
                saveProductWithImages(
                                remeraSpiderMan,
                                Map.of(
                                                "REM-SPM-BLA-S", List.of("spiderman-blanco-1.png"),
                                                "REM-SPM-BLA-M", List.of("spiderman-blanco-1.png"),
                                                "REM-SPM-NEG-L", List.of("spiderman-negro-1.png")));
                saveProductWithImages(
                                remeraNarutoKonoha,
                                Map.of(
                                                "REM-NRT-BEI-S", List.of("naruto-beige-1.png"),
                                                "REM-NRT-BEI-M", List.of("naruto-beige-1.png"),
                                                "REM-NRT-NEG-L", List.of("naruto-negro-1.png")));
                saveProductWithImages(
                                pantalonSonic,
                                Map.of(
                                                "PAN-SNC-AZU-S", List.of("sonic-azul-1.png"),
                                                "PAN-SNC-AZU-M", List.of("sonic-azul-1.png"),
                                                "PAN-SNC-GRI-L", List.of("sonic-azul-1.png")));
                saveProductWithImages(
                                topPikachu,
                                Map.of(
                                                "TOP-PKM-AMA-S", List.of("poke-amarillo-1.png"),
                                                "TOP-PKM-AMA-M", List.of("poke-amarillo-1.png"),
                                                "TOP-PKM-AMA-L", List.of("poke-amarillo-1.png")));
                saveProductWithImages(
                                pantalonFinalFantasy,
                                Map.of(
                                                "PAN-FFF-NEG-S", List.of("ff-negro-1.png"),
                                                "PAN-FFF-NEG-M", List.of("ff-negro-1.png"),
                                                "PAN-FFF-BLA-L", List.of("ff-blanco-1.png")));
                saveProductWithImages(
                                remeraGoku,
                                Map.of(
                                                "REM-GOK-NAR-S", List.of("goku-naranja-1.png"),
                                                "REM-GOK-NAR-M", List.of("goku-naranja-1.png"),
                                                "REM-GOK-NAR-L", List.of("goku-naranja-1.png")));
                saveProductWithImages(
                                shortMario,
                                Map.of(
                                                "SHO-MAR-RED-S", List.of("mario-rojo-1.png"),
                                                "SHO-MAR-RED-M", List.of("mario-rojo-1.png"),
                                                "SHO-MAR-AZU-L", List.of("mario-azul-1.png")));
                saveProductWithImages(
                                remeraHarryPotter,
                                Map.of(
                                                "REM-HPT-NEG-S", List.of("hp-negro-1.png"),
                                                "REM-HPT-NEG-M", List.of("hp-negro-1.png"),
                                                "REM-HPT-BLA-L", List.of("hp-blanco-1.png")));
                saveProductWithImages(
                                pantalonZelda,
                                Map.of(
                                                "PAN-ZLD-VER-S", List.of("zelda-verde-1.png"),
                                                "PAN-ZLD-VER-M", List.of("zelda-verde-1.png"),
                                                "PAN-ZLD-VER-L", List.of("zelda-verde-1.png")));
                saveProductWithImages(
                                remeraNarutoAkatsuki,
                                Map.of(
                                                "REM-AKA-BLA-S", List.of("nar-blanco-1.png"),
                                                "REM-AKA-NEG-M", List.of("nar-negro-1.png"),
                                                "REM-AKA-NEG-L", List.of("nar-negro-1.png")));
                saveProductWithImages(
                                topJigglypuff,
                                Map.of(
                                                "TOP-JGL-ROS-S", List.of("j-rosa-1.png"),
                                                "TOP-JGL-ROS-M", List.of("j-rosa-1.png"),
                                                "TOP-JGL-ROS-L", List.of("j-rosa-1.png")));
                saveProductWithImages(
                                remeraPulpFiction,
                                Map.of(
                                                "SHO-PULP-NEG-S", List.of("pulp-negro-1.png"),
                                                "SHO-PULP-NEG-M", List.of("pulp-negro-1.png"),
                                                "SHO-PULP-NEG-L", List.of("pulp-negro-1.png")));

        }

        private Category findCategory(String code) {
                return categoryRepository.findByCode(code)
                                .orElseThrow(() -> new IllegalStateException("Category not found: " + code));
        }

        private AttributeValue findAttributeValue(String code) {
                return attributeValueRepository.findByCode(code)
                                .orElseThrow(() -> new IllegalStateException("Attribute value not found: " + code));
        }

        private String productImagePath(Long productId, String fileName) {
                return IMAGE_BASE_URL + productId + "/" + fileName;
        }

        private List<ProductImage> imagesForVariant(
                        Long productId,
                        ProductVariant variant,
                        List<String> fileNames) {
                List<ProductImage> images = new java.util.ArrayList<>();

                for (int i = 0; i < fileNames.size(); i++) {
                        images.add(ProductImage.builder()
                                        .path(productImagePath(productId, fileNames.get(i)))
                                        .position(i)
                                        .variant(variant)
                                        .build());
                }

                return images;
        }

        private void assignImages(Product product, java.util.Map<String, List<String>> imagesBySku) {
                Long productId = product.getId();

                for (ProductVariant variant : product.getVariants()) {
                        List<String> fileNames = imagesBySku.getOrDefault(
                                        variant.getSku(),
                                        List.of());

                        variant.setImages(imagesForVariant(productId, variant, fileNames));
                }

                String coverImagePath = product.getVariants().stream()
                                .filter(variant -> variant.getImages() != null && !variant.getImages().isEmpty())
                                .map(variant -> variant.getImages().get(0).getPath())
                                .findFirst()
                                .orElse(null);

                product.setCoverImagePath(coverImagePath);
        }

        private Product saveProductWithImages(
                        Product product,
                        Map<String, List<String>> imagesBySku) {
                Product savedProduct = productRepository.saveAndFlush(product);

                assignImages(savedProduct, imagesBySku);

                return productRepository.save(savedProduct);
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
                                                .toList());

                variant.setImages(List.of(
                                ProductImage.builder()
                                                .path("https://placehold.co/600x800?text=" + sku + "-1")
                                                .position(0)
                                                .variant(variant)
                                                .build(),
                                ProductImage.builder()
                                                .path("https://placehold.co/600x800?text=" + sku + "-2")
                                                .position(1)
                                                .variant(variant)
                                                .build()));

                return variant;
        }

        private Product product(
                        String name,
                        Double price,
                        String description,
                        Set<Category> categories,
                        List<ProductVariant> variants) {
                Product product = Product.builder()
                                .name(name)
                                .price(price)
                                .description(description)
                                .categories(categories)
                                .variants(variants)
                                .seller(this.userRepository.findByEmail("seller1@user.com")
                                                .orElseThrow(() -> new IllegalStateException("Seller not found")))
                                .build();

                variants.forEach(variant -> variant.setProduct(product));

                String coverImageUri = variants.stream()
                                .filter(variant -> variant.getImages() != null && !variant.getImages().isEmpty())
                                .map(variant -> variant.getImages().get(0).getPath())
                                .findFirst()
                                .orElse(null);

                product.setCoverImagePath(coverImageUri);

                return product;
        }
}
