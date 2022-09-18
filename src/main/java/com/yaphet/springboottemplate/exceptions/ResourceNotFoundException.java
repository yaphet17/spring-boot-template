package com.yaphet.springboottemplate.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The page you were looking for could not be found on our server")
public class ResourceNotFoundException extends RuntimeException {
    private static final Logger logger = LogManager.getLogger(ResourceNotFoundException.class);

    public ResourceNotFoundException(String message) {
        super(message);
        logger.debug(getMessage());
    }
}
