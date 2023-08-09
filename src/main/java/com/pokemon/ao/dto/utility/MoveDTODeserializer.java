package com.pokemon.ao.dto.utility;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemon.ao.dto.MoveDTO;

import java.io.IOException;

public class MoveDTODeserializer extends JsonDeserializer<MoveDTO> {
    @Override
    public MoveDTO deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(p);

        String moveName = jsonNode.get("name").asText();
        String moveTypeName = jsonNode.get("type").get("name").asText();
        int movePower = jsonNode.get("power").asInt();

        return MoveDTO.builder()
                .name(moveName)
                .power(movePower)
                .typeName(moveTypeName)
                .build();
    }
}
