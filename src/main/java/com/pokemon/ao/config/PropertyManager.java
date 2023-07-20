package com.pokemon.ao.config;

import com.pokemon.ao.domain.PokemonVO;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;

@Configuration
public class PropertyManager {

    private CustomProperties customProperties;

    public PropertyManager() {
        retrieveProperty();
    }

    // TODO: esternalizzare path property
    private void retrieveProperty(){
        Yaml yaml = new Yaml(new Constructor(CustomProperties.class));
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("customProperties.yaml"); //file of custom properties to convert
        customProperties = yaml.load(inputStream); //convert file yaml into a map of String-Object
        System.out.println(customProperties);
    }

    public PokemonVO getDefaultPokemon(){
        return customProperties.getPokemon();
    }

}
