package com.foodlab.foodReservation.auth.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    private String accessToken;
    private String refreshToken;

}
