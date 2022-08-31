package com.yaphet.springboottemplate.scheduledservices;

import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yaphet.springboottemplate.services.AppUserService;

@Component
public class UnVerifiedAccountRemoverService {

    private final AppUserService appUserService;

    public UnVerifiedAccountRemoverService(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Scheduled(fixedDelayString = "${app.spring-boot-template.unverified-account-remover.delay}", timeUnit = TimeUnit.DAYS)
    public void removeUnverifiedAccounts() {
        appUserService.removeUnVerifiedUsers();
        // TODO: log this
    }


}
