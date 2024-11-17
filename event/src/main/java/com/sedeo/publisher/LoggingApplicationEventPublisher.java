package com.sedeo.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

public class LoggingApplicationEventPublisher implements ApplicationEventPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingApplicationEventPublisher.class);

    private final ApplicationEventPublisher applicationEventPublisher;

    public LoggingApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        LOGGER.info(event.getClass().getSimpleName() + " has been emitted from " + event.getSource().toString());
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishEvent(Object event) {
        LOGGER.info(event.getClass().getSimpleName() + " has been emitted");
        applicationEventPublisher.publishEvent(event);
    }
}
