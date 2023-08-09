package com.pokemon.ao.dto.utility;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemon.ao.dto.MoveDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MoveDTODeserializer extends JsonDeserializer<MoveDTO> {
    @Override
    public MoveDTO deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonParser);

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
