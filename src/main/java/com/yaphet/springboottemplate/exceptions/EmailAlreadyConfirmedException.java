package com.yaphet.springboottemplate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.GONE, reason = "Email already confirmed")
public class EmailAlreadyConfirmedException extends RuntimeException {

    public EmailAlreadyConfirmedException() {
        super("Your email is already confirmed");
    }
}
