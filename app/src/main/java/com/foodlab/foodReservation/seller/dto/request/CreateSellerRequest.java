package com.foodlab.foodReservation.seller.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@RequiredArgsConstructor
public class CreateSellerRequest {
    @NotBlank
    private final String username;
    @NotBlank
    private final String password;
}
