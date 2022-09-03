package com.yaphet.springboottemplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
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
@EnableJpaAuditing
@EnableCaching
@EnableScheduling
public class SpringbootTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootTemplateApplication.class, args);
    }
}

/**
 * TODO
 * - Implement soft delete for all entities (Done)
 * - Update modified at field for each update query (Done)
 * - Change existing email template
 * - Send email to users registered by admins
 * - Send welcome email to new users after email verification
 * - Implement Flyway migration
 * - Implement a feature to assign default role for a user when a role is deleted
 * - Implement caching in a proper way
 * - Consider implementing createdBy and modifiedBy fields for each entity
 * - Fix issue with the remember me functionality
 */