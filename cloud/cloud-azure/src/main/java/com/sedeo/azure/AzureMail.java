package com.sedeo.azure;


import com.azure.communication.email.EmailClient;
import com.azure.communication.email.models.EmailMessage;
import com.sedeo.cloudapi.azure.email.Mail;
import com.sedeo.cloudapi.azure.email.model.MailRequest;

public class AzureMail implements Mail {

    private final String senderAddress;

    private final EmailClient emailClient;

    public AzureMail(EmailClient emailClient, String senderAddress) {
        this.emailClient = emailClient;
        this.senderAddress = senderAddress;
    }

    public void sendMail(MailRequest mailRequest) {
        emailClient.beginSend(
                new EmailMessage()
                        .setSenderAddress(senderAddress)
                        .setToRecipients(mailRequest.recipient())
                        .setSubject(mailRequest.subject())
                        .setBodyHtml(mailRequest.content())
        );
    }
}
