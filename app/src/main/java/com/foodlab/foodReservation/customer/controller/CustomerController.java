package com.foodlab.foodReservation.customer.controller;

import com.foodlab.foodReservation.auth.config.UserInfo;
import com.foodlab.foodReservation.auth.token.JwtProvider;
import com.foodlab.foodReservation.auth.token.Token;
import com.foodlab.foodReservation.customer.entity.Customer;
import com.foodlab.foodReservation.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final JwtProvider jwtProvider;
    private final CustomerRepository customerRepository;


    @GetMapping("/token/refresh")
    public Map<String, String> refreshAuth(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("refreshToken");

        if (token != null && jwtProvider.verifyToken(token)) {
            String email = String.valueOf(jwtProvider.getJwtContents("email"));
            Customer savedCustomer = customerRepository.findByEmail(email);
            Token newToken = jwtProvider.generateToken(savedCustomer.getId(), savedCustomer.getEmail(), "ROLE_CUSTOMER");

            response.addHeader("accessToken", newToken.getAccessToken());
            response.addHeader("refreshToken", newToken.getRefreshToken());
            response.setContentType("application/json;charset=UTF-8");
            Map<String, String> result = new HashMap<>();
            result.put("result", "success");
            return result;
        }

        throw new RuntimeException();
    }
}
