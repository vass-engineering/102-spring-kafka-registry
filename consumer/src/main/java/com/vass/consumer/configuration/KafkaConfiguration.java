package com.vass.consumer.configuration;

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
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {
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
    @Value("${partitions}")
    private Integer partitions;
    @Value("${replications}")
    private Short replications;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, CustomerEventAvro> avroListenerContainerFactory() {
        var properties = getCommonConfig();
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        properties.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
        var factory = new ConcurrentKafkaListenerContainerFactory<Long, CustomerEventAvro> ();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(properties));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, CustomerEventProtoOuterClass.CustomerEventProto> protoListenerContainerFactory() {
        var properties = getCommonConfig();
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaProtobufDeserializer.class);
        properties.put(KafkaProtobufDeserializerConfig.SPECIFIC_PROTOBUF_VALUE_TYPE, CustomerEventProtoOuterClass.CustomerEventProto.class);
        var factory = new ConcurrentKafkaListenerContainerFactory<Long, CustomerEventProtoOuterClass.CustomerEventProto> ();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(properties));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, CustomerEventJson> jsonListenerContainerFactory() {
        var properties = getCommonConfig();
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaJsonDeserializer.class);
        properties.put(KafkaJsonDeserializerConfig.JSON_VALUE_TYPE, CustomerEventJson.class);
        var factory = new ConcurrentKafkaListenerContainerFactory<Long, CustomerEventJson> ();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(properties));
        return factory;
    }

    private Map<String, Object> getCommonConfig(){
        var properties = new HashMap<String, Object>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        properties.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistry);
        properties.put(KafkaAvroDeserializerConfig.AUTO_REGISTER_SCHEMAS, true);
        properties.put(KafkaAvroDeserializerConfig.VALUE_SUBJECT_NAME_STRATEGY, RecordNameStrategy.class);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return properties;
    }

    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topicAvro() {
        return new NewTopic(topicAvro, partitions, replications);
    }
    @Bean
    public NewTopic topicProto() {
        return new NewTopic(topicProto, partitions, replications);
    }
    @Bean
    public NewTopic topicJson() {
        return new NewTopic(topicJson, partitions, replications);
    }
}
