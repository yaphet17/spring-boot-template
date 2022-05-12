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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class AppUserService implements UserDetailsService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final AppUserRepository appUserRepository;
    private ConfirmationTokenService confirmationTokenService;
    private final String USER_NOT_FOUND_MSG="user with %s not found";


    public List<AppUser> getAppUsers() {
        return appUserRepository.findAllUndeleted();
    }
    public AppUser getAppUser(Long id){
        return appUserRepository.findByIdUndeleted(id).orElseThrow(()->new IllegalStateException("User not found with id="+id));
    }
    public AppUser getAppUserByEmail(String email){
        return appUserRepository.findByEmail(email).orElseThrow(()->new IllegalStateException("User not found with email="+email));
    }
    public void save(AppUser appUser){
        //check if email is already taken
        boolean isEmailTaken=appUserRepository.findByEmail(appUser.getEmail()).isPresent();
        if(isEmailTaken){
            throw new IllegalStateException("Email already taken");
        }
        String encodedPassword=passwordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);
        appUser.setEnabled(true);
        appUserRepository.save(appUser);
    }
    @Transactional
    public void update(AppUser au) {
        boolean updated=false;
        AppUser appUser =appUserRepository.findByIdUndeleted(au.getId()).orElseThrow(()->new IllegalStateException("student not found with id "+au.getId()));
        if(au.getFirstName()!=null&&
                au.getFirstName().length()>0&&
                !Objects.equals(appUser.getFirstName(),au.getFirstName())){
            appUser.setFirstName(au.getFirstName());
            updated=true;
        }
        if(au.getLastName()!=null&&
                au.getLastName().length()>0&&
                !Objects.equals(appUser.getLastName(),au.getLastName())){
            appUser.setLastName(au.getLastName());
            updated=true;
        }
        if(au.getEmail()!=null&&
                au.getEmail().length()>0&&
                !Objects.equals(appUser.getEmail(),au.getEmail())){
            Optional<AppUser> appUserOptional=appUserRepository.findByEmail(au.getEmail());
            if(appUserOptional.isPresent()){
                throw new IllegalStateException("email already exists");
            }
            appUser.setEmail(au.getEmail());
            updated=true;
        }
        if(updated){
            appUserRepository.save(au);
        }
    }

    public void delete(Long id) {
        boolean userExists=appUserRepository.findByIdUndeleted(id).isPresent();
        if(!userExists){
            throw new IllegalStateException("user not found with id="+id);
        }
        appUserRepository.deleteAppUser(id);
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
        save(appUser);
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
