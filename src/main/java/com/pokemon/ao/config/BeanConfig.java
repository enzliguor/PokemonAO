package com.pokemon.ao.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;

@Configuration
@Slf4j
public class BeanConfig {
    @Bean
    protected RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    protected CustomProperties retrieveProperty(){
        Yaml yaml = new Yaml(new Constructor(CustomProperties.class));
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("customProperties.yaml");
        return yaml.load(inputStream);
    }
}
