package com.sedeo.azure;

import com.sedeo.cloudapi.azure.email.Mail;
import com.sedeo.cloudapi.azure.email.model.MailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleOutputMail implements Mail {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleOutputMail.class);

    public void sendMail(MailRequest mailRequest) {
        LOGGER.info("Sending email to {} with subject \"{}\"", mailRequest.recipient(), mailRequest.subject());
    }
}
