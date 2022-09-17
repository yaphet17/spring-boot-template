package com.yaphet.springboottemplate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.GONE)
public class EmailAlreadyConfirmedException extends RuntimeException {

    public EmailAlreadyConfirmedException() {
        super("Your email is already confirmed");
    }
}
