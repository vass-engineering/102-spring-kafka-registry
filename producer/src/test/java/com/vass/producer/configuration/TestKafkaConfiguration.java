package com.vass.producer.configuration;

import com.vass.model.avro.CustomerEventAvro;
import com.vass.model.json.CustomerEventJson;
import com.vass.model.proto.CustomerEventProtoOuterClass;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.serializers.KafkaJsonDeserializer;
import io.confluent.kafka.serializers.KafkaJsonDeserializerConfig;
import io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializer;
import io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializerConfig;
import io.confluent.kafka.serializers.subject.RecordNameStrategy;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.List;

@TestConfiguration
public class TestKafkaConfiguration {
    @Value("${bootstrap-servers}")
    private String bootstrapServers;
    @Value("${schema-registry}")
    private String schemaRegistry;
    @Value("${topic.avro}")
    private String topicAvro;
    @Value("${topic.proto}")
    private String topicProto;
    @Value("${topic.json}")
    private String topicJson;


    @Bean
    public KafkaConsumer<Long, CustomerEventAvro> avroKafkaConsumer() {
        var properties = new HashMap<String, Object>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        properties.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistry);
        properties.put(KafkaAvroDeserializerConfig.AUTO_REGISTER_SCHEMAS, true);
        properties.put(KafkaAvroDeserializerConfig.VALUE_SUBJECT_NAME_STRATEGY, RecordNameStrategy.class);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group-id-test-avro");
        properties.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
        var customer =  new KafkaConsumer<Long, CustomerEventAvro>(properties);
        customer.subscribe(List.of(topicAvro));
        return customer;
    }

    @Bean
    public KafkaConsumer<Long, CustomerEventProtoOuterClass.CustomerEventProto> protoKafkaConsumer() {
        var properties = new HashMap<String, Object>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        properties.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistry);
        properties.put(KafkaAvroDeserializerConfig.AUTO_REGISTER_SCHEMAS, true);
        properties.put(KafkaAvroDeserializerConfig.VALUE_SUBJECT_NAME_STRATEGY, RecordNameStrategy.class);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaProtobufDeserializer.class);
        properties.put(KafkaProtobufDeserializerConfig.SPECIFIC_PROTOBUF_VALUE_TYPE, CustomerEventProtoOuterClass.CustomerEventProto.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group-id-test-proto");
        properties.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
        var customer =  new KafkaConsumer<Long, CustomerEventProtoOuterClass.CustomerEventProto>(properties);
        customer.subscribe(List.of(topicProto));
        return customer;
    }

    @Bean
    public KafkaConsumer<Long, CustomerEventJson> jsonKafkaConsumer() {
        var properties = new HashMap<String, Object>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaJsonDeserializer.class);
        properties.put(KafkaJsonDeserializerConfig.JSON_VALUE_TYPE, CustomerEventJson.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group-id-test-json");
        var customer =  new KafkaConsumer<Long, CustomerEventJson>(properties);
        customer.subscribe(List.of(topicJson));
        return customer;
    }

}
