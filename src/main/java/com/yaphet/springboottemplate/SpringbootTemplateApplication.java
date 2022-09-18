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
 * --- DONE ---
 * - Implement soft delete for all entities (Done)
 * - Update modified at field for each update query (Done)
 * - Create a scheduled service to cleanup expired remember-me tokens (Done)
 * - Implement Flyway migration (Done)
 * - Consider implementing createdBy and modifiedBy fields for each entity (Done)
 * - Implement caching in a proper way (Done)
 * - Fix issue with remember me functionality (Done)
 * - Implement a feature to assign default role for a user when a role is deleted (Done-prevent deletion)
 * ---In progress ---
 * - Display error message as flash messages
 * - Change existing email template
 * - Send email to users registered by admins
 * - Send welcome email to new users after email verification
 * - Implement error handler in a proper way
 * - Handle admin that to delete super admin or it self
 * - Consider creating an aspect to delete remember-me tokens when a user is deleted
 * - Adhere to UpperCase for final property names
 * - Clean up property files
 * - Create indexes for all entities
 */