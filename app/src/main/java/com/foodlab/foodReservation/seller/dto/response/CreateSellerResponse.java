package com.foodlab.foodReservation.seller.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateSellerResponse {
    private final Long id;
    private final String username;
}
