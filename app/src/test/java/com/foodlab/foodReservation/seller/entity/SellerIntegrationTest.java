package com.foodlab.foodReservation.seller.entity;

import com.foodlab.foodReservation.testUtils.TestContainerBase;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.PersistenceException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 다음 요소들의 integration test
 * Entity -> DB
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SellerIntegrationTest extends TestContainerBase {

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    void sellerEntity_whenValidSellerProvided_thenShouldBeStored() {
        // given
        Seller seller = Seller.builder().username("user").password("1234").build();
        // when
        Seller savedSeller = testEntityManager.persistFlushFind(seller);
        // then
        assertThat(savedSeller.getUsername()).isEqualTo("user");
        assertThat(savedSeller.getPassword()).isEqualTo("1234");
        assertThat(savedSeller.getStoreList()).isEmpty();
    }

    @Test
    void sellerEntity_whenDuplicateUsernameProvided_thenShouldRejectToStore() {
        // given
        Seller seller1 = Seller.builder().username("user").password("1234").build();
        testEntityManager.persistAndFlush(seller1);
        // when
        Seller seller2 = Seller.builder().username("user").password("abcd").build();
        // then
        assertThatThrownBy(() -> testEntityManager.persistAndFlush(seller2))
                .isInstanceOf(PersistenceException.class)
                .hasCauseInstanceOf(ConstraintViolationException.class);
    }
}
