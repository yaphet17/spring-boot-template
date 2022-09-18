package com.yaphet.springboottemplate.exceptions;

import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Your token is expired")
public class TokenExpiredException extends RuntimeException {
    private static final Logger logger = LogManager.getLogger(TokenExpiredException.class);

    public TokenExpiredException(LocalDateTime expirationDate) {
        super("Token expired at " + expirationDate);
        logger.debug(getMessage());
    }
}
