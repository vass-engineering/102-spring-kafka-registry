package com.vass.producer;

import com.vass.model.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class Producer {

    @Value("${topic.name}")
    private String TOPIC;

    private final KafkaTemplate<Long, Customer> kafkaTemplate;

    public void send(Customer customer) {
        this.kafkaTemplate.send(this.TOPIC, customer.getCustomerId(), customer);
        log.info(String.format("Produced customer -> %s", customer));
    }
}
