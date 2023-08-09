package com.pokemon.ao.dto.utility;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemon.ao.dto.TypeDTO;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

public class TypeDTODeserializer extends JsonDeserializer<TypeDTO> {
    @Override
    public TypeDTO deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(p);

        String typeName = jsonNode.get("name").asText();
        Set<String> movesName = new LinkedHashSet<>();
        jsonNode.get("moves").forEach(move -> {
            String moveName = move.get("name").asText();
            movesName.add(moveName);
        });

        return TypeDTO.builder()
                .name(typeName)
                .movesName(movesName)
                .build();
    }
}
