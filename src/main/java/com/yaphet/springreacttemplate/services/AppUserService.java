package com.yaphet.springreacttemplate.services;

import com.yaphet.springreacttemplate.utilities.AppUserDetails;
import com.yaphet.springreacttemplate.models.ConfirmationToken;
import com.yaphet.springreacttemplate.models.AppUser;
import com.yaphet.springreacttemplate.repositories.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@Service
public class AppUserService implements UserDetailsService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final AppUserRepository appUserRepository;
    private ConfirmationTokenService confirmationTokenService;
    private final String USER_NOT_FOUND_MSG="user with %s not found";


    public AppUser getAppUser(String email){
        return appUserRepository.findByEmail(email).orElseThrow(()->new IllegalStateException("User not found"));
    }
    public void saveAppUser(AppUser appUser){
        //check if email is already taken
        boolean isEmailTaken=appUserRepository.findByEmail(appUser.getEmail()).isPresent();
        if(isEmailTaken){
            throw new IllegalStateException("Email already taken");
        }
        appUserRepository.save(appUser);
    }
    @Transactional
    public void updateAppUserRole(AppUser appUser){
        AppUser tempAppUser=appUserRepository.findByEmail(appUser.getEmail()).orElseThrow(()->new IllegalStateException("User not found"));
        if(Objects.equals(appUser.getRoles(),tempAppUser.getRoles())){
            return;
        }
        appUserRepository.save(appUser);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser=appUserRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG,email)));
        return new AppUserDetails(appUser);
    }

    public String signUpUser(AppUser appUser){

        //hash password
        String encodedPassword=passwordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);
        saveAppUser(appUser);
        String token= UUID.randomUUID().toString();
        ConfirmationToken confirmationToken=new ConfirmationToken(token,
                LocalDateTime.now(),LocalDateTime.now().plusMinutes(15),
                appUser
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }
    public void enableAppUser(String email) {
        boolean emailExists=appUserRepository.findByEmail(email).isPresent();
        if(!emailExists){
            throw new IllegalStateException("email doesn't exist");
        }
        appUserRepository.enableAppUser(email);
    }

}
