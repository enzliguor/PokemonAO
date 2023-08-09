package com.pokemon.ao.dto.utility;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemon.ao.dto.SpeciesDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpeciesDTODeserializer extends JsonDeserializer<SpeciesDTO> {
    @Override
    public SpeciesDTO deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonParser);

        String name = jsonNode.get("forms").get(0).get("name").asText();
        String spriteUrl = jsonNode.get("sprites").get("other").get("home").get("front_default").asText();
        String typeName = jsonNode.get("types").get(0).get("type").get("name").asText();

        return SpeciesDTO.builder()
                .name(name)
                .spriteUrl(spriteUrl)
                .type(typeName)
                .build();
    }
}