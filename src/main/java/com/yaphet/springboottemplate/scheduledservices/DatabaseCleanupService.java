package com.yaphet.springboottemplate.scheduledservices;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yaphet.springboottemplate.services.AppUserService;
import com.yaphet.springboottemplate.services.PersistentLoginService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class DatabaseCleanupService {
    private static final Logger logger = LogManager.getLogger(DatabaseCleanupService.class);

    private final AppUserService appUserService;
    private final PersistentLoginService persistentLoginService;

    @Scheduled(fixedDelayString = "${app.spring-boot-template.scheduled-services.delay.remove-unverified-accounts}", initialDelay = 1, timeUnit = TimeUnit.DAYS)
    public void removeUnverifiedAccounts() {
        int count = appUserService.removeUnVerifiedUsers();
        logger.info(count + " unverified accounts removed");
    }

    @Scheduled(fixedDelayString = "${app.spring-boot-template.scheduled-services.delay.remove-expired-remember-me-tokens}", initialDelay = 1, timeUnit = TimeUnit.DAYS)
    public void removeExpiredTokens() {
        int count  = persistentLoginService.removeAllExpiredTokens();
        logger.info(count + " expired tokens removed");
    }


}
