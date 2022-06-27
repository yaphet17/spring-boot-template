package com.yaphet.springreacttemplate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "User already exists with email")
public class EmailAlreadyTakenException extends RuntimeException{

    public EmailAlreadyTakenException(String email){
        super("Email " + email + " already taken");
    }
}
