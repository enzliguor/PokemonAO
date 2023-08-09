package com.pokemon.ao.dto.utility;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemon.ao.dto.TypeDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TypeDTODeserializer extends JsonDeserializer<TypeDTO> {
    @Override
    public TypeDTO deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonParser);

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
