package com.yaphet.springboottemplate.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.GONE, reason = "Email already confirmed")
public class EmailAlreadyConfirmedException extends Exception {
    private static final Logger logger = LogManager.getLogger(EmailAlreadyConfirmedException.class);
    public EmailAlreadyConfirmedException() {
        super("Your email is already confirmed");
    }
}
