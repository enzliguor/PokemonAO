package com.pokemon.ao.config;

import com.pokemon.ao.domain.PokemonVO;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;

@Configuration
public class PropertyManager {

    private PokemonVO pokemonVOProperties;

    public PropertyManager() {
        retrieveProperty();
    }

    private void retrieveProperty(){
        Yaml yaml = new Yaml(new Constructor(PokemonVO.class));
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("customProperties.yaml"); //file of custom properties to convert
        pokemonVOProperties = yaml.load(inputStream); //convert file yaml into a map of String-Object
    }



}
