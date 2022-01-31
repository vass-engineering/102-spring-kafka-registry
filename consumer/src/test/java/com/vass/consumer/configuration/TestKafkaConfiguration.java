package com.vass.consumer.configuration;

import com.vass.model.avro.CustomerEventAvro;
import com.vass.model.json.CustomerEventJson;
import com.vass.model.proto.CustomerEventProtoOuterClass;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import io.confluent.kafka.serializers.KafkaJsonSerializer;
import io.confluent.kafka.serializers.protobuf.KafkaProtobufSerializer;
import io.confluent.kafka.serializers.subject.RecordNameStrategy;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;

@TestConfiguration
public class TestKafkaConfiguration {
    @Value("${bootstrap-servers}")
    private String bootstrapServers;
    @Value("${schema-registry}")
    private String schemaRegistry;
    @Bean
    public KafkaTemplate<Long, CustomerEventAvro> avroCustomerEventKafkaTemplate(){
        var properties = commonConfiguration();
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        properties.put(KafkaAvroSerializerConfig.VALUE_SUBJECT_NAME_STRATEGY, RecordNameStrategy.class);
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(properties));
    }

    @Bean
    public KafkaTemplate<Long, CustomerEventProtoOuterClass.CustomerEventProto> protoCustomerEventKafkaTemplate(){
        var properties = commonConfiguration();
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaProtobufSerializer.class);
        properties.put(KafkaAvroSerializerConfig.VALUE_SUBJECT_NAME_STRATEGY, RecordNameStrategy.class);
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(properties));
    }

    @Bean
    public KafkaTemplate<Long, CustomerEventJson> jsonCustomerEventKafkaTemplate(){
        var properties = commonConfiguration();
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaJsonSerializer.class);
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(properties));
    }

    public Map<String, Object> commonConfiguration(){
        var properties = new HashMap<String, Object>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 1000);
        properties.put(ProducerConfig.RETRIES_CONFIG, 0);
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 10);
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 10000);
        properties.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistry);
        properties.put(KafkaAvroSerializerConfig.AUTO_REGISTER_SCHEMAS, true);
        return properties;
    }
}
