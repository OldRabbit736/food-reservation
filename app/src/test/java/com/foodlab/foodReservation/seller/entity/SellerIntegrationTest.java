package com.foodlab.foodReservation.seller.entity;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.persistence.PersistenceException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("test")
class SellerIntegrationTest {

    @Container
    static final MariaDBContainer<?> mariadb = new MariaDBContainer<>("mariadb:10.9.2");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mariadb::getJdbcUrl);
        registry.add("spring.datasource.username", mariadb::getUsername);
        registry.add("spring.datasource.password", mariadb::getPassword);
    }

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    void sellerEntity_whenValidSellerProvided_thenShouldBeStored() {
        // given
        Seller seller = Seller.builder().username("oldrabbit").password("1234").build();
        // when
        Seller savedSeller = testEntityManager.persistFlushFind(seller);
        // then
        assertThat(savedSeller.getUsername()).isEqualTo("oldrabbit");
        assertThat(savedSeller.getPassword()).isEqualTo("1234");
        assertThat(savedSeller.getStoreList()).isEmpty();
    }

    @Test
    void sellerEntity_whenDuplicateUsernameProvided_thenShouldRejectToStore() {
        // given
        Seller seller1 = Seller.builder().username("oldrabbit").password("1234").build();
        testEntityManager.persistAndFlush(seller1);
        // when
        Seller seller2 = Seller.builder().username("oldrabbit").password("abcd").build();
        // then
        assertThatThrownBy(() -> testEntityManager.persistAndFlush(seller2))
                .isInstanceOf(PersistenceException.class)
                .hasCauseInstanceOf(ConstraintViolationException.class);
    }
}
