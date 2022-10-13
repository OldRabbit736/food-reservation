package com.foodlab.foodReservation.seller.controller;

import com.foodlab.foodReservation.seller.dto.request.CreateSellerRequest;
import com.foodlab.foodReservation.seller.dto.response.CreateSellerResponse;
import com.foodlab.foodReservation.seller.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/sellers")
    public CreateSellerResponse createSeller(@RequestBody @Valid CreateSellerRequest createSellerRequest) {
        return sellerService.createSeller(createSellerRequest);
    }
}
