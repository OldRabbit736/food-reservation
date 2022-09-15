package com.foodlab.foodReservation.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodlab.foodReservation.auth.config.SecurityConfig;
import com.foodlab.foodReservation.store.dto.request.CreateStoreRequest;
import com.foodlab.foodReservation.store.dto.response.CreateStoreResponse;
import com.foodlab.foodReservation.store.service.StoreService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = StoreController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                )
        }
)
@WithMockUser
class StoreControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StoreService storeService;

    @DisplayName("상점 생성 수행")
    @Test
    void createStoreTest() throws Exception {

        // given
        CreateStoreRequest validRequest = CreateStoreRequest.builder()
                .address("서울시")
                .latitude(12.34)
                .longitude(56.78)
                .name("홍콩반점")
                .zipCode("123-456")
                .build();
        when(storeService.createStore(any(CreateStoreRequest.class)))
                .thenReturn(new CreateStoreResponse(25L));

        // then
        mockMvc.perform(post("/stores")
                        .with(oauth2Login()
                                .authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("storeId", Is.is(25)));

    }

    @DisplayName("상점 생성 거부 - DTO validation 실패 시 상태 코드 400, payload status 값이 'FAIL'이어야 합니다.")
    @Test
    void createStoreShouldFailWhenDTOValidationFails() throws Exception {

        // given
        CreateStoreRequest badRequest = CreateStoreRequest.builder()
                .address("서울시")
                .latitude(12.34)
                .longitude(56.78)
                .name(null) // @NotBlank violation
                .zipCode("123-456")
                .build();

        // then
        mockMvc.perform(post("/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(oauth2Login()
                                .authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER")))
                        .content(objectMapper.writeValueAsString(badRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status", Is.is("FAIL")));
    }

}
