package com.uade.tpo.grupo7.marketplace;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:marketplace-test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;NON_KEYWORDS=VALUE",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.docker.compose.enabled=false"
})
@AutoConfigureMockMvc
class MarketplaceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    @Test
    void getsCategoriesWithoutServerError() throws Exception {
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk());
    }

    @Test
    void getsProductsWithoutServerError() throws Exception {
        mockMvc.perform(get("/products")
                        .param("page", "0")
                        .param("size", "5")
                        .param("sort", "id,ASC"))
                .andExpect(status().isOk());
    }

    @Test
    void getsProductByIdWithoutServerError() throws Exception {
        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk());
    }

    @Test
    void createsProductWithoutServerError() throws Exception {
        String requestBody = """
                {
                  "name": "Remera azul lila",
                  "price": 19999,
                  "description": "Remera de algodon con variante azul lila",
                  "categoryIds": [3],
                  "variants": [
                    {
                      "sku": "REM-AZL-TEST-S",
                      "price": 19999,
                      "stock": 10,
                      "attributeValues": [
                        { "attributeValueId": 1 },
                        { "attributeValueId": 10 }
                      ]
                    }
                  ]
                }
                """;

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

}
