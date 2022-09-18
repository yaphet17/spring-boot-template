package com.yaphet.springboottemplate.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Resource already exists")
public class ResourceAlreadyExistsException extends Exception {
    private static final Logger logger = LogManager.getLogger(ResourceAlreadyExistsException.class);

    public ResourceAlreadyExistsException(String message) {
        super(message);
        logger.debug(getMessage());
    }
}
