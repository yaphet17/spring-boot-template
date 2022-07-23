package com.yaphet.springboottemplate.exceptions;

import java.time.LocalDateTime;

public class TokenExpiredException extends RuntimeException{

    public TokenExpiredException(LocalDateTime expirationDate){
        super("Token expired at " + expirationDate);
    }
}
