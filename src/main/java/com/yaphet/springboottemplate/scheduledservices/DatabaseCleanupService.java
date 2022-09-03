package com.yaphet.springboottemplate.scheduledservices;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yaphet.springboottemplate.services.AppUserService;

@Component
public class DatabaseCleanupService {
    private static final Logger logger = LogManager.getLogger(DatabaseCleanupService.class);

    private final AppUserService appUserService;

    public DatabaseCleanupService(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Scheduled(fixedDelayString = "${app.spring-boot-template.unverified-account-remover.delay}", timeUnit = TimeUnit.DAYS)
    public void removeUnverifiedAccounts() {
        appUserService.removeUnVerifiedUsers();
        logger.info("Unverified accounts removed");
    }


}
