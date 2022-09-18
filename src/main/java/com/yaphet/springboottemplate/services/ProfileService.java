package com.yaphet.springboottemplate.services;

import com.yaphet.springboottemplate.exceptions.ResourceAlreadyExistsException;
import com.yaphet.springboottemplate.models.AppUser;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final AppUserService appUserService;
    private final BCryptPasswordEncoder passwordEncoder;

    public void changePassword(String username, String oldPassword, String newPassword) throws ResourceAlreadyExistsException {
        AppUser appUser = appUserService.getAppUser(username);

        if (!passwordEncoder.matches(oldPassword, appUser.getPassword())) {
            return;
        }
        appUser.setPassword(passwordEncoder.encode(newPassword));
        appUserService.updateAppUser(appUser);
    }
}
