package com.vass.consumer.configuration;

import com.vass.model.avro.CustomerEventAvro;
import com.vass.model.json.CustomerEventJson;
import com.vass.model.proto.CustomerEventProtoOuterClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingDeque;

@Configuration
public class SpringConfiguration {

    @Bean
    public LinkedBlockingDeque<CustomerEventAvro> CustomerEventAvros(){
        return new LinkedBlockingDeque<>();
    }

    @Bean
    public LinkedBlockingDeque<CustomerEventProtoOuterClass.CustomerEventProto> CustomerEventProtos(){
        return new LinkedBlockingDeque<>();
    }

    @Bean
    public LinkedBlockingDeque<CustomerEventJson> CustomerEventJsons(){
        return new LinkedBlockingDeque<>();
    }
}
