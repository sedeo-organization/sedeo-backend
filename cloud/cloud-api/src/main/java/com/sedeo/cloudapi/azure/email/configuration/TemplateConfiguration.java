package com.sedeo.cloudapi.azure.email.configuration;

import com.sedeo.cloudapi.azure.email.model.ResetPasswordMessageTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TemplateConfiguration {

    @Bean
    ResetPasswordMessageTemplate ResetPasswordMessageTemplate(@Value("${frontend.base.url}") String frontendBaseUrl) {
        return new ResetPasswordMessageTemplate(frontendBaseUrl);
    }
}
