package com.foodlab.foodReservation.seller.service;

import com.foodlab.foodReservation.seller.dto.request.CreateSellerRequest;
import com.foodlab.foodReservation.seller.dto.response.CreateSellerResponse;
import com.foodlab.foodReservation.seller.entity.Seller;
import com.foodlab.foodReservation.testUtils.TestContainerBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;


import static org.assertj.core.api.Assertions.*;


/**
 * SellerService -> SellerRepository -> DB
 */
@SpringBootTest
@AutoConfigureTestEntityManager
@Transactional
class SellerServiceIntegrationTest extends TestContainerBase {

    @Autowired
    TestEntityManager te;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    SellerService sellerService;

    @Test
    void createSeller_whenNoDuplicateUsername_thenShouldSaveSeller() {
        // given
        CreateSellerRequest request = new CreateSellerRequest("user", "1234");
        // when
        CreateSellerResponse savedSeller = sellerService.createSeller(request);
        te.flush();
        te.clear();
        Seller foundSeller = te.find(Seller.class, savedSeller.getId());
        // then
        assertThat(foundSeller.getUsername()).isEqualTo("user");
    }

    @Test
    void createSeller_whenSellerIsPersisted_thenPasswordShouldBeEncoded() {
        // given
        CreateSellerRequest request = new CreateSellerRequest("user", "1234");
        // when
        CreateSellerResponse savedSeller = sellerService.createSeller(request);
        te.flush();
        te.clear();
        Seller seller = te.find(Seller.class, savedSeller.getId());
        // then
        assertThat(passwordEncoder.matches("1234", seller.getPassword())).isTrue();
    }

    @Test
    void createSeller_whenDuplicateUsername_thenShouldThrow() {
        // given
        CreateSellerRequest request = new CreateSellerRequest("user", "1234");
        sellerService.createSeller(request);
        te.flush();
        te.clear();
        // when, then
        assertThatThrownBy(() -> sellerService.createSeller(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 사용중인 username 입니다.");
    }

}
