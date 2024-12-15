package org.example.blogapp.config;

import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean

    public ModelMapper mapper()
    {
        return new ModelMapper();
    }
}
