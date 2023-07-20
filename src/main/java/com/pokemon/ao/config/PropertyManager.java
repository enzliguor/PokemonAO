package com.pokemon.ao.config;

import com.pokemon.ao.domain.PokemonVO;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.util.Set;

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
    }

    public PokemonVO getDefaultPokemon(){
        return customProperties.getPokemon();
    }

    public String getIcon(String iconName){
        return customProperties.getIcons().get(iconName);
    }

    public Set<String> getTypeNames() {
        return customProperties.getIcons().keySet();
    }

}
