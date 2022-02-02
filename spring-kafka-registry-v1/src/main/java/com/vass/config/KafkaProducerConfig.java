package com.vass.config;

import com.vass.model.Customer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
//import io.confluent.kafka.serializers.subject.TopicNameStrategy;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class KafkaProducerConfig {

    private final KafkaProperties kafkaProperties;

    @Value("${topic.name}")
    private String topicName;

    @Value("${topic.partitions-num}")
    private Integer partitions;

    @Value("${topic.replication-factor}")
    private short replicationFactor;

    @Value("${spring.kafka.bootstrap.servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.schema.registry.url}")
    private String schemaRegistry;

    @Bean
    KafkaTemplate<Long, Customer> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    ProducerFactory<Long, Customer> producerFactory() {
        var props = new HashMap<String, Object>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 1000);
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 10);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 10000);
        props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistry);
        //props.put(KafkaAvroSerializerConfig.KEY_SUBJECT_NAME_STRATEGY, TopicNameStrategy.class.getName());
        props.put(KafkaAvroSerializerConfig.AUTO_REGISTER_SCHEMAS, true);
        return new DefaultKafkaProducerFactory<>(props);
    }

    // As the application will create a topic in Kafka, it is better to update the KafkaAdmin's BOOTSTRAP_SERVERS_CONFIG
    // property with the bootstrap servers' url configured, otherwise it will get the default 'localhost:9082'
    @Bean
    KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        return new KafkaAdmin(configs);
    }

    @Bean
    NewTopic newTopic() {
        return new NewTopic(topicName, partitions, replicationFactor);
    }
}
