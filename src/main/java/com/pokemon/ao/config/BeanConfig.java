package com.pokemon.ao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfig {
    @Bean
    protected RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
