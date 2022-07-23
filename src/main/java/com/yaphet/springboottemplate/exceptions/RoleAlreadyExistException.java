package com.yaphet.springboottemplate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RoleAlreadyExistException extends RuntimeException {

    public RoleAlreadyExistException(String roleName){
        super("Role " + roleName + "already exists");
    }
}
