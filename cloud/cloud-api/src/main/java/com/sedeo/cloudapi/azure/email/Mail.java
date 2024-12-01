package com.sedeo.cloudapi.azure.email;

import com.sedeo.cloudapi.azure.email.model.MailRequest;

public interface Mail {

    void sendMail(MailRequest mailRequest);
}
