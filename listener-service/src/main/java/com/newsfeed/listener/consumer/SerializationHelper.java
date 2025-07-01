package com.newsfeed.listener.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.models.Information;
import common.models.Serialization;

public class SerializationHelper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String extractInfoType(String jsonPayload) throws Exception {
        JsonNode node = objectMapper.readTree(jsonPayload);
        return node.get("info_type").asText();
    }

    public static Information decodeDataObject(String jsonPayload) throws Exception {
        String infoType = extractInfoType(jsonPayload);
        Class<? extends Information> clazz = Serialization.getDataTypeClass(infoType);
        if (clazz == null) {
            throw new IllegalArgumentException("Unknown info_type: " + infoType);
        }
        return objectMapper.readValue(jsonPayload, clazz);
    }
}
