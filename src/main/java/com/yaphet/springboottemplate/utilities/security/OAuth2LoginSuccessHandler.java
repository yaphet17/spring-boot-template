package com.yaphet.springboottemplate.utilities.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.yaphet.springboottemplate.services.AppUserService;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final AppUserService appUserService;

    public OAuth2LoginSuccessHandler(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        CustomOAuth2User customOAuth2User =  (CustomOAuth2User) authentication.getPrincipal();
        appUserService.updateAuthenticationType(customOAuth2User.getEmail(), customOAuth2User.getOauth2ClientName());
        setDefaultTargetUrl("/home");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
