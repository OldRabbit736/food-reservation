package com.foodlab.foodReservation.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodlab.foodReservation.auth.config.SecurityConfig;
import com.foodlab.foodReservation.common.Address;
import com.foodlab.foodReservation.item.dto.request.CreateItemRequest;
import com.foodlab.foodReservation.item.dto.request.UpdateItemRequest;
import com.foodlab.foodReservation.item.dto.response.CreateItemResponse;
import com.foodlab.foodReservation.item.dto.response.DeleteItemResponse;
import com.foodlab.foodReservation.item.dto.response.UpdateItemResponse;
import com.foodlab.foodReservation.item.service.ItemService;
import com.foodlab.foodReservation.seller.entity.Seller;
import com.foodlab.foodReservation.store.entity.Store;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(
        controllers = ItemController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                )
        }
)
@WithMockUser
public class ItemControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ItemService itemService;

    Store getStore() {
        Seller seller = Seller.builder()
                .username("TEST_USER")
                .password("TEST_PASSWORD").build();
        Address address = Address.builder()
                .address("TEST_ADDRESS")
                .longitude(126.58)
                .latitude(37.33)
                .zipCode("04323")
                .build();
        Store store = Store.builder()
                .id(1L)
                .name("TEST_STORE")
                .address(address)
                .seller(seller)
                .build();
        return store;
    }


    @DisplayName("매뉴 등록 성공")
    @Test
    void createItemSuccess() throws Exception {
        // given
        CreateItemRequest createItemRequest = CreateItemRequest.builder()
                .name("아이스아메리카노")
                .price(4500)
                .storeId(getStore().getId())
                .build();

        when(itemService.createItem(createItemRequest))
                .thenReturn(new CreateItemResponse(1L));


        // expected
        mockMvc.perform(post("/items")
                        .with(oauth2Login()
                                .authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER")))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createItemRequest))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.savedItemId").value(1L))
                .andDo(print());
    }

    @DisplayName("매뉴 등록 실패 - 매뉴 이름 누락")
    @Test
    void createItemFailWhenNotFoundName() throws Exception {

        // given
        CreateItemRequest createItemRequest = CreateItemRequest.builder()
                .name(null)
                .price(4500)
                .storeId(getStore().getId())
                .build();

        when(itemService.createItem(createItemRequest))
                .thenReturn(new CreateItemResponse(1L));

        // expected
        mockMvc.perform(post("/items").with(oauth2Login()
                                .authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER")))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createItemRequest))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("FAIL"))
                .andDo(print());
    }

    @DisplayName("매뉴 수정 성공")
    @Test
    void updateItemSuccess() throws Exception {
        // given
        UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                .name("TEST_MENU")
                .price(10000)
                .build();

        when(itemService.updateItem(1L, updateItemRequest))
                .thenReturn(new UpdateItemResponse("TEST_MENU", 10000));

        // expected
        mockMvc.perform(put("/items/{itemId}", 1L).with(oauth2Login()
                                .authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER")))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateItemRequest))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("매뉴 수정 실패 - 매뉴 가격이 음수값인 경우")
    @Test
    void updateItemFail() throws Exception {
        // given
        UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                .name("TEST_MENU")
                .price(-10000)
                .build();

        // expected
        mockMvc.perform(put("/items/{itemId}", 1L).with(oauth2Login()
                                .authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER")))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateItemRequest))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("FAIL"))
                .andExpect(jsonPath("$.data[0].errorMessage").value("가격은 0 이하일 수 없습니다."))
                .andDo(print());
    }

    @DisplayName("매뉴 삭제 성공")
    @Test
    void deleteItemSuccess() throws Exception {
        // given
        when(itemService.deleteItem(1L))
                .thenReturn(new DeleteItemResponse(1L, true));

        // expected
        mockMvc.perform(delete("/items/{itemId}", 1L).with(oauth2Login()
                                .authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER")))
                        .contentType(APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deletedItemId").value(1L))
                .andDo(print());
    }

}
