package com.yaphet.springreacttemplate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class IdNotFoundException extends RuntimeException{

    public IdNotFoundException(String resourceName, Long id){
        super(resourceName + " not found with id " + id);
    }
}
