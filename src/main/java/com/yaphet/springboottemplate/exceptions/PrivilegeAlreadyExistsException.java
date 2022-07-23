package com.yaphet.springboottemplate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PrivilegeAlreadyExistsException extends RuntimeException{

    public PrivilegeAlreadyExistsException(String privilegeName){
        super("Privilege " + privilegeName + "already exists");
    }
}
