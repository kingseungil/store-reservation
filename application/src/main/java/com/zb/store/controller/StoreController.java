package com.zb.store.controller;

import com.zb.annotation.OnlyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store")
public class StoreController {

    /**
     * 상점 등록
     */
    @OnlyManager
    @PostMapping("/manager")
    public void registerStore() {
    }

    /**
     * 상점 수정
     */
    @OnlyManager
    @PutMapping("/manager")
    public void updateStore() {
    }

    /**
     * 상점 삭제
     */
    @OnlyManager
    @DeleteMapping("/manager")
    public void deleteStore() {
    }

    /**
     * 상점 조회
     */
    @GetMapping
    public void getStores() {
    }

    /**
     * 상점 상세 조회
     */
    @GetMapping("/{storeId}")
    public void getStore(
      @PathVariable("storeId") Long storeId) {
    }


}
