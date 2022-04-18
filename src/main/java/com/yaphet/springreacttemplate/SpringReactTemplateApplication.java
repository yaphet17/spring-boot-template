package com.yaphet.springreacttemplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@ComponentScan(basePackages =
        {
                "com.yaphet.springreacttemplate.dbintializer",
                "com.yaphet.springreacttemplate.appuser",
                "com.yaphet.springreacttemplate.appuserregistration",
                "com.yaphet.springreacttemplate.email",
                "com.yaphet.springreacttemplate.error",
                "com.yaphet.springreacttemplate.security",
                "com.yaphet.springreacttemplate.role",

        })
public class SpringReactTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringReactTemplateApplication.class, args);
    }
}
