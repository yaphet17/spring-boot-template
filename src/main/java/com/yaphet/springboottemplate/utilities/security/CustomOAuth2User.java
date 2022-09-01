package com.yaphet.springboottemplate.utilities.security;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.yaphet.springboottemplate.models.AppUser;

public class CustomOAuth2User extends  AppUserDetails implements OAuth2User {


    private final AppUser appUser;
    private final String oauth2ClientName;
    private final OAuth2User oauth2User;

    public CustomOAuth2User(AppUser appUser, String oauth2ClientName, OAuth2User oauth2User) {
        super(appUser);
        this.appUser = appUser;
        this.oauth2ClientName = oauth2ClientName;
        this.oauth2User = oauth2User;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return super.getAuthorities();
    }

    @Override
    public String getName() {
        return oauth2User.getAttribute("name");
    }

    public String getEmail() {
        return oauth2User.getAttribute("email");
    }

    public String getOauth2ClientName() {
        return oauth2ClientName;
    }
}
