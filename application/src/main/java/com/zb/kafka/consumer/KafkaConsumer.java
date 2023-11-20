package com.zb.kafka.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    @KafkaListener(topics = "test", groupId = "group_id")
    public void listen(String message) {
        System.out.println("Received Messasge : " + message);

    }

}
