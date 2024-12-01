package com.sedeo.cloudapi.azure.email.model;

import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResetPasswordMessageTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResetPasswordMessageTemplate.class);
    private static final String FORGOT_PASSWORD_TEMPLATE_PATH = "html/ForgotPasswordTemplate.html";
    private static final String FORGOT_PASSWORD_BACKGROUND_PATH = "/assets/images/reset_password_background.png";
    private static final String RESET_PASSWORD_PATH = "/reset-password";

    private final String frontendBaseUrl;

    public ResetPasswordMessageTemplate(String frontendBaseUrl) {
        this.frontendBaseUrl = frontendBaseUrl;
    }

    public Optional<String> withResetPasswordMessage(String recipientFirstName, String recipientLastName, String token) {
        try {
            String urlWithToken = frontendBaseUrl.concat(RESET_PASSWORD_PATH).concat("?token=").concat(token);

            String formattedContent = readFileIntoString(FORGOT_PASSWORD_TEMPLATE_PATH)
                    .replace("[resetPasswordUrl]", urlWithToken)
                    .replace("[firstName]", recipientFirstName)
                    .replace("[lastName]", recipientLastName)
                    .replace("[backgroundImgUrl]", frontendBaseUrl.concat(FORGOT_PASSWORD_BACKGROUND_PATH));

            return Optional.of(formattedContent);
        } catch (Exception e) {
            LOGGER.error("Failed to prepare reset password html template: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private static String readFileIntoString(String pathToFile) throws IOException {
        InputStream resource = new ClassPathResource(pathToFile).getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource));
        return bufferedReader.lines().collect(Collectors.joining("\n"));
    }
}
