package com.yaphet.springtemplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages =
        {
                "com.yaphet.springtemplate.models",
                "com.yaphet.springtemplate.repositories",
                "com.yaphet.springtemplate.controllers",
                "com.yaphet.springtemplate.services",
                "com.yaphet.springtemplate.utilities",
                "com.yaphet.springtemplate.config.security"

        })
public class SpringTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringTemplateApplication.class, args);
    }
}
