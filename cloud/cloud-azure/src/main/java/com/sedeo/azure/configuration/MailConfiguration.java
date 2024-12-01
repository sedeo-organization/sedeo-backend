package com.sedeo.azure.configuration;

import com.azure.communication.email.EmailClient;
import com.azure.communication.email.EmailClientBuilder;
import com.sedeo.azure.AzureMail;
import com.sedeo.azure.ConsoleOutputMail;
import com.sedeo.cloudapi.azure.email.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class MailConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailConfiguration.class);

    @Value("${azure.connection.string:}")
    private String azureConnectionString;

    @Value("${mail.sender.address:}")
    private String senderAddress;

    @Bean
    @Profile("prod")
    public EmailClient emailClient() {
        return new EmailClientBuilder()
                .connectionString(azureConnectionString)
                .buildClient();
    }

    @Bean
    @Profile("prod")
    public Mail productionMail(EmailClient emailClient) {
        LOGGER.info("Azure mail is being used for mailing");
        return new AzureMail(emailClient, senderAddress);
    }

    @Bean
    @Profile("dev")
    public Mail developmentMail() {
        LOGGER.info("Console mail is being used for mailing");
        return new ConsoleOutputMail();
    }
}
