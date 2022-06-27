package com.yaphet.springreacttemplate.services;

import com.yaphet.springreacttemplate.models.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final AppUserService appUserService;
    private final BCryptPasswordEncoder passwordEncoder;

    public boolean changePassword(Long id, String oldPassword, String newPassword ){
        AppUser appUser=appUserService.getAppUser(id);

        if(!passwordEncoder.matches(oldPassword,appUser.getPassword())){
            return false;
        }
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        appUser.setPassword(encodedNewPassword);
        appUserService.updateAppUser(appUser);
        return true;
    }
}
