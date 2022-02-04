package com.vass.producer.service;

import com.vass.model.avro.CustomerEventAvro;
import com.vass.model.json.CustomerEventJson;
import com.vass.model.proto.CustomerEventProtoOuterClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Objects;
import java.util.Random;

@Service
@Slf4j
public class ProducerService {
    @Value("${topic.avro}")
    private String topicAvro;
    @Value("${topic.proto}")
    private String topicProto;
    @Value("${topic.json}")
    private String topicJson;
    @Autowired
    private KafkaTemplate<Long, CustomerEventAvro> avroCustomerEventKafkaTemplate;
    @Autowired
    private KafkaTemplate<Long, CustomerEventProtoOuterClass.CustomerEventProto> protoCustomerEventKafkaTemplate;
    @Autowired
    private KafkaTemplate<Long, CustomerEventJson> jsonCustomerEventKafkaTemplate;
    @Autowired
    private GeneratorService generator;
    private Random random = new Random();

    public void produceAvro(Integer count) throws InterruptedException {
        count = Objects.requireNonNullElse(count,1);
        for (int i = 0; i < count; i++) {
            var events = generator.generateChainForAvro();
            for (CustomerEventAvro event : events) {
                var result  = avroCustomerEventKafkaTemplate.send(topicAvro, null, System.currentTimeMillis(), event.getCustomerId(), event);
                final var n = i;
                result.addCallback(new ListenableFutureCallback<>() {
                    @Override
                    public void onSuccess(SendResult<Long, CustomerEventAvro> result) {
                        log.info(">>> produceAvro {} succeed: topic={}, key={}, eventType={}", n, topicAvro, event.getCustomerId(), event.getEventType());
                    }
                    @Override
                    public void onFailure(Throwable ex) {
                        log.info(">>> produceAvro {} onFailure: {}", n, ex.getMessage());
                    }
                });
                log.info(">>> sleep {} ms", 1000);
                Thread.sleep(1000);
            }
        }
    }

    public void produceProto(Integer count) throws InterruptedException {
        count = Objects.requireNonNullElse(count,1);
        for (int i = 0; i < count; i++) {
            var events = generator.generateChainForProto();
            for (CustomerEventProtoOuterClass.CustomerEventProto event : events) {
                var result  = protoCustomerEventKafkaTemplate.send(topicProto, null, System.currentTimeMillis(), event.getCustomerId(), event);
                final var n = i;
                result.addCallback(new ListenableFutureCallback<>() {
                    @Override
                    public void onSuccess(SendResult<Long, CustomerEventProtoOuterClass.CustomerEventProto> result) {
                        log.info(">>> produceProto {} succeed: topic={}, key={}, eventType={}", n, topicProto, event.getCustomerId(), event.getEventType());
                    }
                    @Override
                    public void onFailure(Throwable ex) {
                        log.info(">>> produceProto {} onFailure: {}", n, ex.getMessage());
                    }
                });
                log.info(">>> sleep {} ms", 1000);
                Thread.sleep(1000);
            }
        }
    }


    public void produceJson(Integer count) throws InterruptedException {
        count = Objects.requireNonNullElse(count,1);
        for (int i = 0; i < count; i++) {
            var events = generator.generateChainForJson();
            for (CustomerEventJson event : events) {
                var result  = jsonCustomerEventKafkaTemplate.send(topicJson, null, System.currentTimeMillis(), event.getCustomerId(), event);
                final var n = i;
                result.addCallback(new ListenableFutureCallback<>() {
                    @Override
                    public void onSuccess(SendResult<Long, CustomerEventJson> result) {
                        log.info(">>> produceJson {} succeed: topic={}, key={}, eventType={}", n, topicJson, event.getCustomerId(), event.getEventType());
                    }
                    @Override
                    public void onFailure(Throwable ex) {
                        log.info(">>> produceJson {} onFailure: {}", n, ex.getMessage());
                    }
                });
                log.info(">>> sleep {} ms", 1000);
                Thread.sleep(1000);
            }
        }
    }
}
