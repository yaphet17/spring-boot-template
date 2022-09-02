package com.yaphet.springboottemplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages =
        {
                "com.yaphet.springboottemplate.models",
                "com.yaphet.springboottemplate.repositories",
                "com.yaphet.springboottemplate.controllers",
                "com.yaphet.springboottemplate.services",
                "com.yaphet.springboottemplate.scheduledservices",
                "com.yaphet.springboottemplate.utilities",
                "com.yaphet.springboottemplate.config.security",
                "com.yaphet.springboottemplate.security"

        })
@EnableCaching
@EnableScheduling
public class SpringbootTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootTemplateApplication.class, args);
    }
}
