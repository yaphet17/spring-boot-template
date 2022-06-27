package com.yaphet.springreacttemplate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RoleNotFoundException extends RuntimeException{

    public RoleNotFoundException(String roleName){
        super("Role " + roleName + " not found");
    }
    public RoleNotFoundException(Long id){
        super("Role not found with id " + id);
    }
}
