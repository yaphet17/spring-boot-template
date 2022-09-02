package com.yaphet.springboottemplate.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.yaphet.springboottemplate.services.AppUserService;

@Component
public class LocalLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final AppUserService appUserService;

    public LocalLoginSuccessHandler(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        AppUserDetails appUserDetails = (AppUserDetails) authentication.getPrincipal();
        appUserService.updateAuthenticationType(appUserDetails.getUsername(), "local");
        setDefaultTargetUrl("/home");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
