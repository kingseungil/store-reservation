package com.zb.store.controller;

import com.zb.annotation.OnlyManager;
import com.zb.dto.store.StoreDto.StoreRequest;
import com.zb.dto.store.StoreDto.StoreResponse;
import com.zb.store.service.StoreServce;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "STORE")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store")
public class StoreController {

    private final StoreServce storeServce;

    @OnlyManager
    @Operation(summary = "상점 등록")
    @PostMapping("/manager/register")
    public ResponseEntity<String> registerStore(
      @Valid @RequestBody StoreRequest form) {
        storeServce.registerStore(form);
        return ResponseEntity.ok("등록 성공");
    }

    @OnlyManager
    @Operation(summary = "상점 수정")
    @PutMapping("/manager/{storeId}")
    public ResponseEntity<String> updateStore(
      @PathVariable("storeId") Long storeId,
      @Valid @RequestBody StoreRequest form) {
        storeServce.updateStore(form, storeId);
        return ResponseEntity.ok("수정 성공");
    }

    @OnlyManager
    @Operation(summary = "상점 삭제")
    @DeleteMapping("/manager/{storeId}")
    public ResponseEntity<String> deleteStore(
      @PathVariable("storeId") Long storeId) {
        storeServce.deleteStore(storeId);
        return ResponseEntity.ok("삭제 성공");
    }


    @GetMapping
    @Operation(summary = "상점 조회")
    public ResponseEntity<Slice<StoreResponse>> getStores(Pageable pageable) {
        return ResponseEntity.ok(storeServce.getStores(pageable));
    }

    @GetMapping("/detail/{storeId}")
    @Operation(summary = "상점 상세 조회")
    public ResponseEntity<StoreResponse> getStore(
      @PathVariable("storeId") Long storeId) {
        return ResponseEntity.ok(storeServce.getStore(storeId));
    }


}
