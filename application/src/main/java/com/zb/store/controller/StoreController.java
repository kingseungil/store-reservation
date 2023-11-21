package com.zb.store.controller;

import com.zb.annotation.OnlyManager;
import com.zb.dto.store.StoreDto;
import com.zb.dto.store.StoreDto.Response;
import com.zb.store.service.StoreServce;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store")
public class StoreController {

    private final StoreServce storeServce;

    // 상점 등록
    @OnlyManager
    @PostMapping("/manager")
    public ResponseEntity<String> registerStore(
      @Valid @RequestBody StoreDto.Request form) {
        storeServce.registerStore(form);
        return ResponseEntity.ok("등록 성공");
    }

    // 상점 수정
    @OnlyManager
    @PutMapping("/manager/{storeId}")
    public ResponseEntity<String> updateStore(
      @PathVariable("storeId") Long storeId,
      @Valid @RequestBody StoreDto.Request form) {
        storeServce.updateStore(form, storeId);
        return ResponseEntity.ok("수정 성공");
    }

    // 상점 삭제
    @OnlyManager
    @DeleteMapping("/manager/{storeId}")
    public ResponseEntity<String> deleteStore(
      @PathVariable("storeId") Long storeId) {
        storeServce.deleteStore(storeId);
        return ResponseEntity.ok("삭제 성공");
    }


    // 상점 조회
    @GetMapping
    public ResponseEntity<Slice<Response>> getStores(Pageable pageable) {
        return ResponseEntity.ok(storeServce.getStores(pageable));
    }

    // 상점 상세 조회
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreDto.Response> getStore(
      @PathVariable("storeId") Long storeId) {
        return ResponseEntity.ok(storeServce.getStore(storeId));
    }


}
