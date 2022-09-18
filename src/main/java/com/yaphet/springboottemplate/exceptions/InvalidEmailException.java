package com.yaphet.springboottemplate.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid email address")
public class InvalidEmailException extends RuntimeException {
    private static final Logger logger = LogManager.getLogger(InvalidEmailException.class);

    public InvalidEmailException(String email) {
        super(email);
        logger.debug(getMessage());
    }
}
