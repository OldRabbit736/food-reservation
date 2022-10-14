package com.foodlab.foodReservation.seller.repository;

import com.foodlab.foodReservation.seller.entity.Seller;
import com.foodlab.foodReservation.testUtils.TestContainerBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 다음 요소들의 integration test
 * Repository -> DB
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SellerRepositoryIntegrationTest extends TestContainerBase {

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
