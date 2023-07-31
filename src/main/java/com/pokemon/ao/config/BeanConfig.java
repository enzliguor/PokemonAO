package com.pokemon.ao.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
@Slf4j
public class BeanConfig {
    @Value("${path.customProperties}")
    private String customPropertiesPath;
    @Bean
    protected RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    protected CustomProperties retrieveProperty() {
        CustomProperties customProperties = null;
        try {
            Yaml yaml = new Yaml(new Constructor(CustomProperties.class));
            FileInputStream inputStream = new FileInputStream(customPropertiesPath);

            customProperties = yaml.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            log.error("An error occurred while trying to read customProperties.yaml.");
            e.printStackTrace();
        }
        return customProperties;
    }
}
