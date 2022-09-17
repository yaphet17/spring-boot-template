package com.yaphet.springboottemplate.services;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.yaphet.springboottemplate.models.AppUser;
import com.yaphet.springboottemplate.security.AuthenticationType;
import com.yaphet.springboottemplate.security.CustomOAuth2User;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AppUserService appUserService;

    public CustomOAuth2UserService(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String oauth2ClientName = userRequest.getClientRegistration().getClientName();
        String email = oAuth2User.getAttribute("email");

        if (!appUserService.isUserExists((String)oAuth2User.getAttribute(email))) {
            AppUser appUser = createUserIfDoesNotExist(oAuth2User, oauth2ClientName);
            return new CustomOAuth2User(appUser, oauth2ClientName, oAuth2User);
        }
        return new CustomOAuth2User(appUserService.getAppUser(email), oauth2ClientName, oAuth2User);
    }


    private AppUser createUserIfDoesNotExist(OAuth2User oAuth2User, String oauth2ClientName) {
        AppUser appUser = new AppUser();
        String name = oAuth2User.getAttribute("name");

        if (name != null) {
            String[] nameParts = name.split(" ");
            appUser.setFirstName(nameParts[0]);
            if (nameParts.length > 1) {
                appUser.setLastName(nameParts[1]);
            }
        }
        appUser.setEmail(oAuth2User.getAttribute("email"));
        appUser.setAuthType(AuthenticationType.valueOf(oauth2ClientName.toUpperCase()));
        appUser.setEnabled(true);
        appUserService.saveAppUser(appUser);
        return appUser;
    }
}
