package com.foodlab.foodReservation.auth.config;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;


@Getter
@Builder(access = AccessLevel.PRIVATE)
public class OAuth2Attribute {

    private final Map<String, Object> attributes;
    private final String attributeKey;
    private final String email;

    static OAuth2Attribute of(String provider, String attributeKey, Map<String, Object> attributes) {
        if ("kakao".equals(provider)) {
            return ofKakao("email", attributes);
        }
        throw new RuntimeException();
    }

    private static OAuth2Attribute ofKakao(String attributeKey, Map<String, Object> attributes) {

        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        return OAuth2Attribute.builder()
                .email((String) kakaoAccount.get("email"))
                .attributes(kakaoAccount)
                .attributeKey(attributeKey)
                .build();
    }

    Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", attributeKey);
        map.put("email", email);
        return map;
    }
}
