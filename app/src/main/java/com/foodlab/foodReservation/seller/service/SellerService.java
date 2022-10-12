package com.foodlab.foodReservation.seller.service;

import com.foodlab.foodReservation.seller.dto.request.CreateSellerRequest;
import com.foodlab.foodReservation.seller.dto.response.CreateSellerResponse;
import com.foodlab.foodReservation.seller.entity.Seller;
import com.foodlab.foodReservation.seller.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public CreateSellerResponse createSeller(CreateSellerRequest request) {

        sellerRepository.findByUsername(request.getUsername())
                .ifPresent((seller) -> {
                    throw new IllegalArgumentException("이미 사용중인 username 입니다.");
                });

        Seller seller = Seller.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        Seller savedSeller = sellerRepository.save(seller);
        return new CreateSellerResponse(savedSeller.getId(), savedSeller.getUsername());
    }
}
