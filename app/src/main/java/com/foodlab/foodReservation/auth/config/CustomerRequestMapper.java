package com.foodlab.foodReservation.auth.config;

import com.foodlab.foodReservation.customer.entity.Customer;
import com.foodlab.foodReservation.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomerRequestMapper {

    private final CustomerRepository customerRepository;

    public UserInfo toDto(OAuth2User oAuth2User) {

        Map<String, Object> attributes = oAuth2User.getAttributes();

        return UserInfo.builder()
                .id(getCustomer((String) attributes.get("email")).getId())
                .email(getCustomer((String) attributes.get("email")).getEmail())
                .build();
    }

    private Customer getCustomer(String email) {
        Customer savedCustomer = customerRepository.findByEmail(email);
        if (savedCustomer == null) {
            return customerRepository.save(new Customer(email));
        }
        return savedCustomer;
    }
}
