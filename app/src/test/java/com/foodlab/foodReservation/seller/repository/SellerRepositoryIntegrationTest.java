package com.foodlab.foodReservation.seller.repository;

import com.foodlab.foodReservation.seller.entity.Seller;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("test")
class SellerRepositoryIntegrationTest {

    @Container
    static final MariaDBContainer<?> mariadb = new MariaDBContainer<>("mariadb:10.9.2");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mariadb::getJdbcUrl);
        registry.add("spring.datasource.username", mariadb::getUsername);
        registry.add("spring.datasource.password", mariadb::getPassword);
    }

    @Autowired
    SellerRepository sellerRepository;

    @Test
    void findByUsername_whenNoSellerUsernameMatch_thenShouldReturnEmpty() {
        // when
        Optional<Seller> foundSeller = sellerRepository.findByUsername("oldrabbit");
        // then
        assertThat(foundSeller).isEmpty();
    }

    @Test
    void findByUsername_whenSellerUsernameMatch_thenShouldReturnSeller() {
        // given
        Seller seller = Seller.builder().username("oldrabbit").password("1234").build();
        sellerRepository.saveAndFlush(seller);
        // when
        Seller foundSeller = sellerRepository.findByUsername("oldrabbit").orElseThrow();
        // then
        assertThat(foundSeller.getUsername()).isEqualTo("oldrabbit");
        assertThat(foundSeller.getPassword()).isEqualTo("1234");
        assertThat(foundSeller.getStoreList()).isNull();
    }
}
