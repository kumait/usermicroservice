package io.primecoders.voctrainer.userservice.config;

import io.primecoders.voctrainer.userservice.infra.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public IdGenerator idGenerator() {
        return () -> UUID.randomUUID().toString();
    }
}
