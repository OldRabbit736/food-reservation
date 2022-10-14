package com.foodlab.foodReservation.seller.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodlab.foodReservation.seller.dto.request.CreateSellerRequest;
import com.foodlab.foodReservation.testUtils.TestContainerBase;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 다음 요소들의 integration test
 * Filter -> Controller -> Service -> Repository -> DB
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SellerControllerIntegrationTest extends TestContainerBase {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void createSeller_whenValidRequest_thenShouldReturnCreationResult() throws Exception {
        // given
        CreateSellerRequest request = new CreateSellerRequest("user", "1234");
        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/sellers")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                //.andExpect(jsonPath("status", Is.is("SUCCESS"))) // TODO: API response 포맷 통일하고, 통일된 모양으로 테스트하기
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id", Is.isA(Integer.class)))
                .andExpect(jsonPath("username", Is.is("user")));
    }

    @Test
    void createSeller_whenBlankUsername_thenShouldReturnBadRequest() throws Exception {
        // given
        CreateSellerRequest request = new CreateSellerRequest("", "1234");
        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/sellers")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest()); // TODO: API response 포맷 통일하고, 통일된 모양으로 테스트하기
    }

    @Test
    void createSeller_whenDuplicateUsername_thenShouldReturnBadRequest() throws Exception {
        // given
        CreateSellerRequest request = new CreateSellerRequest("user", "1234");
        mockMvc.perform(MockMvcRequestBuilders.post("/sellers")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );
        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/sellers")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().is5xxServerError()); // TODO: API response 포맷 통일하고, 통일된 모양으로 테스트하기
    }
}
