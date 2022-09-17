package com.yaphet.springboottemplate.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Your token is expired")
public class TokenExpiredException extends RuntimeException {

    public TokenExpiredException(LocalDateTime expirationDate) {
        super("Token expired at " + expirationDate);
    }
}
