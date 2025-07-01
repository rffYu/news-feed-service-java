package com.newsfeed.listener.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import common.models.Information;
import common.models.Serialization;

@Component
public class SerializationHelper {
    private final ObjectMapper objectMapper;

    @Autowired
    public SerializationHelper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String extractInfoType(String jsonPayload) throws Exception {
        JsonNode node = this.objectMapper.readTree(jsonPayload);
        return node.get("info_type").asText();
    }

    public Information decodeDataObject(String jsonPayload) throws Exception {
        String infoType = extractInfoType(jsonPayload);
        Class<? extends Information> clazz = Serialization.getDataTypeClass(infoType);
        if (clazz == null) {
            throw new IllegalArgumentException("Unknown info_type: " + infoType);
        }
        return this.objectMapper.readValue(jsonPayload, clazz);
    }
}
