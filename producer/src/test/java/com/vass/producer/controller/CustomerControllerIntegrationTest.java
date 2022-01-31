package com.vass.producer.controller;

import com.vass.model.avro.CustomerEventAvro;
import com.vass.model.avro.CustomerEventTypeAvro;
import com.vass.model.json.CustomerEventJson;
import com.vass.model.json.CustomerEventTypeJson;
import com.vass.model.proto.CustomerEventProtoOuterClass;
import com.vass.producer.configuration.TestKafkaConfiguration;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.utils.KafkaTestUtils;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestKafkaConfiguration.class)
class CustomerControllerIntegrationTest {
    @Autowired
    private KafkaConsumer<Long, CustomerEventAvro> avroKafkaConsumer;
    @Autowired
    private KafkaConsumer<Long, CustomerEventProtoOuterClass.CustomerEventProto> protoKafkaConsumer;
    @Autowired
    private KafkaConsumer<Long, CustomerEventJson> jsonKafkaConsumer;

    @Autowired
    private TestRestTemplate restTemplate;
    @LocalServerPort
    private int port;
    private String host = "http://localhost:";

    @Test
    void generateNewCustomerAvro(){
        var response = restTemplate.getForEntity(host + port + "/generateAvro?count=1&pause=10", String.class);
        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertEquals("OK", response.getBody());
        var result = KafkaTestUtils.getRecords(avroKafkaConsumer);
        Assertions.assertEquals(CustomerEventTypeAvro.CustomerCreated, result.iterator().next().value().getEventType());
    }
    @Test
    void generateNewCustomerProto(){
        var response = restTemplate.getForEntity(host + port + "/generateProto?count=1&pause=10", String.class);
        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertEquals("OK", response.getBody());

        var result = KafkaTestUtils.getRecords(protoKafkaConsumer);
        Assertions.assertEquals(CustomerEventProtoOuterClass.CustomerEventProto.CustomerEventTypeProto.CustomerCreated, result.iterator().next().value().getEventType());
    }

    @Test
    void generateNewPaymentJson(){
        var response = restTemplate.getForEntity(host + port + "/generateJson?count=1&pause=10", String.class);
        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertEquals("OK", response.getBody());
        var result = KafkaTestUtils.getRecords(jsonKafkaConsumer);
        Assertions.assertEquals(CustomerEventTypeJson.CustomerCreated, result.iterator().next().value().getEventType());
    }

}