package com.zb.kafka.consumer;

import com.zb.entity.Store;
import com.zb.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final StoreRepository storeRepository;

    @KafkaListener(topics = "test", groupId = "group_id")
    public void listen(String message) {
        System.out.println("Received Messasge : " + message);
        Store store = storeRepository.findByStoreName(message).get();
        System.out.println(store.getStoreName());
        System.out.println(store.getDescription());

    }

}
