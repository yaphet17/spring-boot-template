package com.yaphet.springboottemplate.utilities.email;


public interface EmailSender {

    void send(String to,String subject, String email);
}
