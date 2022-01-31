package com.vass.consumer.service;

import com.vass.model.avro.CustomerEventAvro;
import com.vass.model.json.CustomerEventJson;
import com.vass.model.proto.CustomerEventProtoOuterClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ConsumerService {
    @Autowired
    private LinkedBlockingDeque<CustomerEventAvro> CustomerEventAvros;
    @Autowired
    private LinkedBlockingDeque<CustomerEventProtoOuterClass.CustomerEventProto> CustomerEventProtos;
    @Autowired
    private LinkedBlockingDeque<CustomerEventJson> CustomerEventJsons;

    @KafkaListener(topics = "${topic.avro}", groupId = "${group.avro}", concurrency = "${partitions}", containerFactory = "avroListenerContainerFactory")
    public void consumeAvro(Message<CustomerEventAvro> message) throws InterruptedException {
        log.info(">> received avro: kafka_groupId={} kafka_receivedTopic={} kafka_receivedPartitionId={}, key={}, value={}",
                message.getHeaders().get(KafkaHeaders.GROUP_ID),
                message.getHeaders().get(KafkaHeaders.RECEIVED_TOPIC),
                message.getHeaders().get(KafkaHeaders.RECEIVED_PARTITION_ID),
                message.getHeaders().get(KafkaHeaders.RECEIVED_MESSAGE_KEY),
                message.getPayload()
        );
        CustomerEventAvros.offer(message.getPayload(), 100, TimeUnit.MILLISECONDS);
    }

    @KafkaListener(topics = "${topic.proto}", groupId = "${group.proto}", concurrency = "${partitions}", containerFactory = "protoListenerContainerFactory")
    public void consumeProto(Message<CustomerEventProtoOuterClass.CustomerEventProto> message) throws InterruptedException {
        log.info(">> received proto: kafka_groupId={} kafka_receivedTopic={} kafka_receivedPartitionId={}, key={}, value={}",
                message.getHeaders().get(KafkaHeaders.GROUP_ID),
                message.getHeaders().get(KafkaHeaders.RECEIVED_TOPIC),
                message.getHeaders().get(KafkaHeaders.RECEIVED_PARTITION_ID),
                message.getHeaders().get(KafkaHeaders.RECEIVED_MESSAGE_KEY),
                message.getPayload()
        );
        CustomerEventProtos.offer(message.getPayload(), 100, TimeUnit.MILLISECONDS);
    }


    @KafkaListener(topics = "${topic.json}", groupId = "${group.json}", concurrency = "${partitions}", containerFactory = "jsonListenerContainerFactory")
    public void consumeJson(Message<CustomerEventJson> message) throws InterruptedException {
        log.info(">> received json: kafka_groupId={} kafka_receivedTopic={} kafka_receivedPartitionId={}, key={}, value={}",
                message.getHeaders().get(KafkaHeaders.GROUP_ID),
                message.getHeaders().get(KafkaHeaders.RECEIVED_TOPIC),
                message.getHeaders().get(KafkaHeaders.RECEIVED_PARTITION_ID),
                message.getHeaders().get(KafkaHeaders.RECEIVED_MESSAGE_KEY),
                message.getPayload()
        );
        CustomerEventJsons.offer(message.getPayload(), 100, TimeUnit.MILLISECONDS);
    }
}
