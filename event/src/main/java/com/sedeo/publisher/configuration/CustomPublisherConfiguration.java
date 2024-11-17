package com.sedeo.publisher.configuration;

import com.sedeo.publisher.LoggingApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class CustomPublisherConfiguration {

    @Bean
    @Primary
    ApplicationEventPublisher loggingApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new LoggingApplicationEventPublisher(applicationEventPublisher);
    }
}
