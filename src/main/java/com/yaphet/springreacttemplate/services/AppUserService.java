package com.yaphet.springreacttemplate.services;

import com.yaphet.springreacttemplate.exceptions.EmailAlreadyTakenException;
import com.yaphet.springreacttemplate.exceptions.EmailNotFoundException;
import com.yaphet.springreacttemplate.exceptions.IdNotFoundException;
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
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@Service
public class AppUserService implements UserDetailsService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final AppUserRepository appUserRepository;
    private ConfirmationTokenService confirmationTokenService;
    private final String USER_NOT_FOUND_MSG = "user with %s not found";
    private final String RESOURCE_NAME = "App user";
    private final int TOKEN_EXPIRATION_DELAY = 15;


    public List<AppUser> getAppUsers() {
        return appUserRepository.findAllUndeleted();
    }
    public AppUser getAppUser(Long id){
        return appUserRepository
                .findByIdUndeleted(id)
                .orElseThrow(() -> new IdNotFoundException(RESOURCE_NAME ,id));
    }
    public AppUser getAppUser(String email){
        return appUserRepository
                .findByEmail(email)
                .orElseThrow(()->new EmailNotFoundException(email));
    }
    public void saveAppUser(AppUser appUser){
        String email = appUser.getEmail();

        appUserRepository
                .findByEmail(email)
                .orElseThrow(() -> new EmailAlreadyTakenException(email));
        String encodedPassword = passwordEncoder.encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);
        appUser.setEnabled(true);
        appUserRepository.save(appUser);
    }
    @Transactional
    public void updateAppUser(AppUser au) {
        boolean updated = false;
        String email = au.getEmail();
        String firstName = au.getFirstName();
        String lastName = au.getLastName();
        AppUser appUser = appUserRepository
                .findByIdUndeleted(au.getId())
                .orElseThrow(() -> new IdNotFoundException(RESOURCE_NAME, au.getId()));

        if(firstName != null &&
                firstName.length() > 0 &&
                !Objects.equals(appUser.getFirstName(), firstName)){
            appUser.setFirstName(firstName);
            updated = true;
        }
        if(lastName != null &&
                lastName.length() > 0 &&
                !Objects.equals(appUser.getLastName(), lastName)){
            appUser.setLastName(lastName);
            updated = true;
        }
        if(email != null &&
                email.length() > 0 &&
                !Objects.equals(appUser.getEmail(), email)){
            appUserRepository
                    .findByEmail(au.getEmail())
                    .orElseThrow(() -> new EmailAlreadyTakenException("email already exists"));
            appUser.setEmail(email);
            updated = true;
        }
        if(updated){
            appUserRepository.save(au);
        }
    }

    public void deleteAppUser(Long id) {
        appUserRepository
                .findByIdUndeleted(id)
                .orElseThrow(() -> new IdNotFoundException(RESOURCE_NAME, id));
        appUserRepository.deleteAppUser(id);
    }

    @Transactional
    public void updateAppUserRole(AppUser appUser){
        String email = appUser.getEmail();
       appUserRepository
                .findByEmail(appUser.getEmail())
                .filter(user -> Objects.equals(appUser.getRoles(), user.getRoles()))
                .map(appUserRepository::save)
                .orElseThrow(()->new EmailNotFoundException(email));
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return appUserRepository.findByEmail(email)
                .map(AppUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String signUpUser(AppUser appUser){
        String encodedPassword = passwordEncoder.encode(appUser.getPassword());
        String token = UUID.randomUUID().toString();

        appUser.setPassword(encodedPassword);
        saveAppUser(appUser);
        ConfirmationToken confirmationToken = new ConfirmationToken(token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(TOKEN_EXPIRATION_DELAY),
                appUser
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }
    public void enableAppUser(String email) {
       appUserRepository
               .findByEmail(email)
               .orElseThrow(() -> new EmailNotFoundException(email));
        appUserRepository.enableAppUser(email);
    }


}
