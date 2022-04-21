package com.yaphet.springreacttemplate.appuser;

import com.yaphet.springreacttemplate.appuserregistration.token.ConfirmationToken;
import com.yaphet.springreacttemplate.appuserregistration.token.ConfirmationTokenService;
import com.yaphet.springreacttemplate.role.Role;
import com.yaphet.springreacttemplate.role.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Service
public class AppUserService implements UserDetailsService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final AppUserRepository appUserRepository;
    private final RoleService roleService;
    private ConfirmationTokenService confirmationTokenService;
    private final String USER_NOT_FOUND_MSG="user with %s not found";


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser=appUserRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG,email)));
        return new AppUserDetails(appUser);
    }
    public AppUser getAppUser(String email){
        AppUser appUser=appUserRepository.findByEmail(email).orElseThrow(()->new IllegalStateException("User not found"));
        return appUser;
    }
    public void saveAppUser(AppUser appUser){
        //check if email is already taken
        boolean isEmailTaken=appUserRepository.findByEmail(appUser.getEmail()).isPresent();
        if(isEmailTaken){
            throw new IllegalStateException("Email already taken");
        }
        //save user to the database
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
    public void assignRole(String email,String roleName) {
        AppUser appUser = getAppUser(email);
        Role role = roleService.getRoleByName(roleName);
        Set<Role> roles = appUser.getRoles();
        roles.add(role);
        appUser.setRoles(roles);
        updateAppUserRole(appUser);
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
