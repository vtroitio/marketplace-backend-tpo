package com.uade.tpo.grupo7.marketplace;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.uade.tpo.grupo7.marketplace.product.entity.Product;
import com.uade.tpo.grupo7.marketplace.product.repository.ProductRepository;

import java.util.TimeZone;

@SpringBootApplication()
public class MarketplaceApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		SpringApplication.run(MarketplaceApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(ProductRepository repo) {
		return args -> {
			if (repo.count() != 0) {
				return;
			}

			for (int i = 1; i <= 25; i++) {
				repo.save(
					Product.builder()
						.name("Remera " + i)
						.price(10.0 * i)
						.build());
			}
		};
	}
}
