package com.vass.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaInject;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaString;

import java.util.Objects;

@JsonInclude()
@JsonSchemaInject(strings = {@JsonSchemaString(path="javaType", value="com.vass.model.json.CustomerEventJson")})
public class CustomerEventJson {
    @JsonProperty
    private Long timestamp;
    @JsonProperty
    private Long  customerId;
    @JsonProperty("name")
    String name;
    @JsonProperty("type")
    CustomerEventTypeJson eventType;

    public CustomerEventJson() {
    }

    public CustomerEventJson(Long timestamp, Long customerId, String name, CustomerEventTypeJson eventType) {
        this.timestamp = timestamp;
        this.customerId = customerId;
        this.name = name;
        this.eventType = eventType;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public CustomerEventTypeJson getEventType() {
        return eventType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerEventJson that = (CustomerEventJson) o;
        return Objects.equals(timestamp, that.timestamp) && Objects.equals(customerId, that.customerId) && Objects.equals(name, that.name) && eventType == that.eventType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, customerId, name, eventType);
    }

    @Override
    public String toString() {
        return "CustomerEventJson{" +
                "timestamp=" + timestamp +
                ", customerId=" + customerId +
                ", name=" + name +
                ", type=" + eventType +
                '}';
    }
}
