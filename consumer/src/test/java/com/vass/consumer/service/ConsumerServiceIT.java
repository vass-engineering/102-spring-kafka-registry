package com.vass.consumer.service;

import com.vass.consumer.configuration.TestKafkaConfiguration;
import com.vass.model.avro.CustomerEventAvro;
import com.vass.model.avro.CustomerEventTypeAvro;
import com.vass.model.json.CustomerEventJson;
import com.vass.model.json.CustomerEventTypeJson;
import com.vass.model.proto.CustomerEventProtoOuterClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;


@SpringBootTest
@Import(TestKafkaConfiguration.class)
class ConsumerServiceIT {
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
    private ConsumerService consumerService;
    @Autowired
    private LinkedBlockingDeque<CustomerEventAvro> CustomerEventAvros;
    @Autowired
    private LinkedBlockingDeque<CustomerEventProtoOuterClass.CustomerEventProto> CustomerEventProtos;
    @Autowired
    private LinkedBlockingDeque<CustomerEventJson> CustomerEventJsons;


    @Test
    void consumeAvro() throws ExecutionException, InterruptedException {
        var event = new CustomerEventAvro(1L,2L, "Bob", CustomerEventTypeAvro.CustomerCreated);
        avroCustomerEventKafkaTemplate.send(topicAvro, event.getCustomerId(), event).get();
        var result = CustomerEventAvros.take();
        Assertions.assertEquals(1L, result.getTimestamp());
        Assertions.assertEquals(2L, result.getCustomerId());
        Assertions.assertEquals("Bob",result.getName());
        Assertions.assertEquals(CustomerEventTypeAvro.CustomerCreated, result.getEventType());
    }

    @Test
    void consumeProto() throws ExecutionException, InterruptedException {
        var event =  CustomerEventProtoOuterClass.CustomerEventProto
                .newBuilder()
                .setCustomerId(2L)
                .setTimestamp(1L)
                .setName("Bob")
                .setEventType(CustomerEventProtoOuterClass.CustomerEventProto.CustomerEventTypeProto.CustomerCreated)
                .build();
        protoCustomerEventKafkaTemplate.send(topicProto, event.getCustomerId(), event).get();
        var result = CustomerEventProtos.take();
        Assertions.assertEquals(1L, result.getTimestamp());
        Assertions.assertEquals(2L, result.getCustomerId());
        Assertions.assertEquals("Bob",result.getName());
        Assertions.assertEquals(CustomerEventProtoOuterClass.CustomerEventProto.CustomerEventTypeProto.CustomerCreated, result.getEventType());
    }

    @Test
    void consumeJson() throws ExecutionException, InterruptedException {
        var event = new CustomerEventJson(1L,2L, "Bob",CustomerEventTypeJson.CustomerCreated);
        jsonCustomerEventKafkaTemplate.send(topicJson, event.getCustomerId(), event).get();
        var result = CustomerEventJsons.take();
        Assertions.assertEquals(1L, result.getTimestamp());
        Assertions.assertEquals(2L, result.getCustomerId());
        Assertions.assertEquals("Bob",result.getName());
        Assertions.assertEquals(CustomerEventTypeJson.CustomerCreated, result.getEventType());
    }
}