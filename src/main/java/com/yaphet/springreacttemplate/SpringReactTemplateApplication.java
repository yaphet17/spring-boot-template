package com.yaphet.springreacttemplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages =
        {
                "com.yaphet.springreacttemplate.models",
                "com.yaphet.springreacttemplate.repositories",
                "com.yaphet.springreacttemplate.controllers",
                "com.yaphet.springreacttemplate.services",
                "com.yaphet.springreacttemplate.utilities"

        })
public class SpringReactTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringReactTemplateApplication.class, args);
    }
}
