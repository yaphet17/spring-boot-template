package com.yaphet.springreacttemplate.exceptions;

public class EmailAlreadyConfirmedException extends RuntimeException{

    public EmailAlreadyConfirmedException(){
        super("Your email is already confirmed");
    }
}
