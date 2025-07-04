package common.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StringListDeserializer extends JsonDeserializer<List<String>> {

    @Override
    public List<String> deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        String text = p.getValueAsString();

        if (text != null && !text.equals("[]")) {
            // Deserialize string like "[]"
            return new ObjectMapper().readValue(text, new com.fasterxml.jackson.core.type.TypeReference<List<String>>() {});
        }
        return new ArrayList<>();
    }
}
