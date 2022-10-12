package com.foodlab.foodReservation.seller.service;

import com.foodlab.foodReservation.seller.dto.request.CreateSellerRequest;
import com.foodlab.foodReservation.seller.dto.response.CreateSellerResponse;
import com.foodlab.foodReservation.seller.entity.Seller;
import com.foodlab.foodReservation.seller.repository.SellerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SellerServiceUnitTest {

    @Mock
    SellerRepository sellerRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    SellerService sellerService;

    @Test
    void createSeller_whenNoDuplicateUsername_thenShouldSaveSeller() {
        // given
        CreateSellerRequest request = new CreateSellerRequest("user", "1234");
        when(sellerRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(sellerRepository.save(any(Seller.class))).thenReturn(Seller.builder().id(23L).username("user").password("1234").build());
        when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");
        // when
        CreateSellerResponse response = sellerService.createSeller(request);
        // then
        verify(sellerRepository).findByUsername(eq("user"));
        assertThat(response.getUsername()).isEqualTo("user");
        assertThat(response.getId()).isEqualTo(23L);
    }

    @Test
    void createSeller_whenNoDuplicateUsername_thenPasswordShouldBeEncoded() {
        // given
        CreateSellerRequest request = new CreateSellerRequest("user", "1234");
        when(sellerRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(sellerRepository.save(any(Seller.class))).thenReturn(Seller.builder().id(23L).username("user").password("1234").build());
        when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");
        // when
        sellerService.createSeller(request);
        // then
        verify(sellerRepository).save(argThat(seller -> seller.getUsername().equals("user")));
        verify(sellerRepository).save(argThat(seller -> seller.getPassword().equals("encoded-password")));
    }

    @Test
    void createSeller_whenDuplicateUsername_thenShouldThrow() {
        // given
        CreateSellerRequest request = new CreateSellerRequest("user", "1234");
        when(sellerRepository.findByUsername(anyString())).thenReturn(Optional.of(Seller.builder().build()));
        // when, then
        assertThatThrownBy(() -> sellerService.createSeller(request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
