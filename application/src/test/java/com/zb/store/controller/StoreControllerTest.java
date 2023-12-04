package com.zb.store.controller;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.zb.common.ControllerTest;
import com.zb.dto.store.StoreDto.StoreRequest;
import com.zb.dto.store.StoreDto.StoreResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

class StoreControllerTest extends ControllerTest {

    @Test
    @DisplayName("[성공]상점 등록")
    void registerStore() {
        // given
        StoreRequest form = StoreRequest.builder()
                                        .storeName("store")
                                        .location("location")
                                        .description("description")
                                        .build();

        // when
        ExtractableResponse<Response> response = saveStore(form, managerToken);

        // then
        assertThat(response.statusCode()).isEqualTo(200);
    }


    @Test
    @DisplayName("[성공]상점 수정")
    void update() {
        // given
        saveStore(StoreRequest.builder()
                              .storeName("store")
                              .location("location")
                              .description("description")
                              .build(), managerToken);

        StoreRequest form = StoreRequest.builder()
                                        .storeName("store2")
                                        .location("location2")
                                        .description("description2")
                                        .build();

        // when
        ExtractableResponse<Response> response = updateStore(form, 1L, managerToken);

        // then
        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("[성공]상점 삭제")
    void delete() {
        // given
        saveStore(StoreRequest.builder()
                              .storeName("store")
                              .location("location")
                              .description("description")
                              .build(), managerToken);

        // when
        ExtractableResponse<Response> response = deleteStore(1L, managerToken);

        // then
        assertThat(response.statusCode()).isEqualTo(200);
    }


    @Test
    @DisplayName("[성공]상점 조회")
    void getStores() {
        // given
        saveStore(StoreRequest.builder()
                              .storeName("store")
                              .location("location")
                              .description("description")
                              .build(), managerToken);

        saveStore(StoreRequest.builder()
                              .storeName("store2")
                              .location("location2")
                              .description("description2")
                              .build(), managerToken);

        // when
        ExtractableResponse<Response> response = getStores(Pageable.ofSize(5));

        // then
        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("[성공]상점 상세 조회")
    void getStore() {
        // given
        saveStore(StoreRequest.builder()
                              .storeName("store")
                              .location("location")
                              .description("description")
                              .build(), managerToken);

        // when
        ExtractableResponse<Response> response = getStore(1L);
        StoreResponse actualResponse = response.as(StoreResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(actualResponse.getInfo().getStoreName()).isEqualTo("store");

    }

    static ExtractableResponse<Response> saveStore(StoreRequest form, String token) {
        return given()
          .contentType("application/json")
          .header("Authorization", "Bearer " + token)
          .body(form)
          .when()
          .post("/api/store/manager/register")
          .then()
          .extract();
    }

    static ExtractableResponse<Response> updateStore(StoreRequest form, Long storeId, String token) {
        return given()
          .contentType("application/json")
          .header("Authorization", "Bearer " + token)
          .body(form)
          .when()
          .put("/api/store/manager/" + storeId)
          .then()
          .extract();
    }

    static ExtractableResponse<Response> deleteStore(long storeId, String managerToken) {
        return given()
          .contentType("application/json")
          .header("Authorization", "Bearer " + managerToken)
          .when()
          .delete("/api/store/manager/" + storeId)
          .then()
          .extract();
    }


    static ExtractableResponse<Response> getStore(Long storeId) {
        return given()
          .contentType("application/json")
          .when()
          .get("/api/store/detail/" + storeId)
          .then()
          .extract();
    }

    static ExtractableResponse<Response> getStores(Pageable pageable) {
        return given()
          .contentType("application/json")
          .when()
          .get("/api/store")
          .then()
          .extract();
    }
}