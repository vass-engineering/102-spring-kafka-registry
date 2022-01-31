package com.vass.producer.service;

import com.vass.model.avro.CustomerEventAvro;
import com.vass.model.avro.CustomerEventTypeAvro;
import com.vass.model.json.CustomerEventJson;
import com.vass.model.json.CustomerEventTypeJson;
import com.vass.model.proto.CustomerEventProtoOuterClass;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Random;

@Service
public class GeneratorService {
    private static String[] names = { "Kr", "Ca", "Ra", "Mrok", "Cru",
            "Ray", "Bre", "Zed", "Drak", "Mor", "Jag", "Mer", "Jar", "Mjol",
            "Zork", "Mad", "Cry", "Zur", "Creo", "Azak", "Azur", "Rei", "Cro",
            "Mar", "Luk" };
    private final Random rand = new Random();

    public List<CustomerEventAvro> generateChainForAvro() {
        var timeStamp = System.currentTimeMillis();
        var customerId = (long) rand.nextInt(100);
        return List.of(
                new CustomerEventAvro(timeStamp, customerId, names[rand.nextInt(names.length)], CustomerEventTypeAvro.CustomerCreated),
                new CustomerEventAvro(timeStamp, customerId,   names[rand.nextInt(names.length)], CustomerEventTypeAvro.CustomerUpdated),
                new CustomerEventAvro(timeStamp, customerId,   names[rand.nextInt(names.length)], CustomerEventTypeAvro.CustomerDeleted),
                new CustomerEventAvro(timeStamp, customerId,   names[rand.nextInt(names.length)], CustomerEventTypeAvro.CustomerPending),
                new CustomerEventAvro(timeStamp, customerId,   names[rand.nextInt(names.length)], CustomerEventTypeAvro.CustomerClosed)
        );
    }

    public List<CustomerEventProtoOuterClass.CustomerEventProto> generateChainForProto() {
        var timeStamp = System.currentTimeMillis();
        var customerId = (long) rand.nextInt(100);
        return List.of(
                CustomerEventProtoOuterClass.CustomerEventProto
                    .newBuilder()
                    .setCustomerId(customerId)
                    .setTimestamp(timeStamp)
                    .setName( names[rand.nextInt(names.length)])
                    .setEventType(CustomerEventProtoOuterClass.CustomerEventProto.CustomerEventTypeProto.CustomerCreated)
                    .build(),
                CustomerEventProtoOuterClass.CustomerEventProto
                        .newBuilder()
                        .setCustomerId(customerId)
                        .setTimestamp(timeStamp)
                        .setName( names[rand.nextInt(names.length)])
                        .setEventType(CustomerEventProtoOuterClass.CustomerEventProto.CustomerEventTypeProto.CustomerUpdated)
                        .build(),
                CustomerEventProtoOuterClass.CustomerEventProto
                        .newBuilder()
                        .setCustomerId(customerId)
                        .setTimestamp(timeStamp)
                        .setName( names[rand.nextInt(names.length)])
                        .setEventType(CustomerEventProtoOuterClass.CustomerEventProto.CustomerEventTypeProto.CustomerDeleted)
                        .build(),
                CustomerEventProtoOuterClass.CustomerEventProto
                        .newBuilder()
                        .setCustomerId(customerId)
                        .setTimestamp(timeStamp)
                        .setName( names[rand.nextInt(names.length)])
                        .setEventType(CustomerEventProtoOuterClass.CustomerEventProto.CustomerEventTypeProto.CustomerPending)
                        .build(),
                CustomerEventProtoOuterClass.CustomerEventProto
                        .newBuilder()
                        .setCustomerId(customerId)
                        .setTimestamp(timeStamp)
                        .setName( names[rand.nextInt(names.length)])
                        .setEventType(CustomerEventProtoOuterClass.CustomerEventProto.CustomerEventTypeProto.CustomerClosed)
                        .build()
        );
    }

    public List<CustomerEventJson> generateChainForJson() {
        var timeStamp = System.currentTimeMillis();
        var customerId = (long) rand.nextInt(100);
        return List.of(
                new CustomerEventJson(timeStamp, customerId, names[rand.nextInt(names.length)], CustomerEventTypeJson.CustomerCreated),
                new CustomerEventJson(timeStamp, customerId, names[rand.nextInt(names.length)], CustomerEventTypeJson.CustomerUpdated),
                new CustomerEventJson(timeStamp, customerId, names[rand.nextInt(names.length)], CustomerEventTypeJson.CustomerDeleted),
                new CustomerEventJson(timeStamp, customerId, names[rand.nextInt(names.length)], CustomerEventTypeJson.CustomerPending),
                new CustomerEventJson(timeStamp, customerId, names[rand.nextInt(names.length)], CustomerEventTypeJson.CustomerClosed)
        );
    }
}
