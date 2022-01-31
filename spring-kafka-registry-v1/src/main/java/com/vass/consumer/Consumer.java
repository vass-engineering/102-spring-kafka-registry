package com.vass.consumer;

import com.vass.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Consumer {

    @KafkaListener(topics = "${spring.kafka.consumer.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(@Payload Customer customer, ConsumerRecordMetadata metadata) {
        log.info("Received message\n---\nTOPIC: {}; PARTITION: {}; OFFSET: {};\nPAYLOAD: {}\n---",
                metadata.topic(), metadata.partition(), metadata.offset(), customer);
    }
}
