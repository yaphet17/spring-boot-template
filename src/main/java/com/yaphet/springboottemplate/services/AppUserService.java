package com.yaphet.springboottemplate.services;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yaphet.springboottemplate.exceptions.ResourceAlreadyExistsException;
import com.yaphet.springboottemplate.exceptions.ResourceNotFoundException;
import com.yaphet.springboottemplate.models.AppUser;
import com.yaphet.springboottemplate.models.ConfirmationToken;
import com.yaphet.springboottemplate.models.Role;
import com.yaphet.springboottemplate.repositories.AppUserRepository;
import com.yaphet.springboottemplate.security.AppUserDetails;
import com.yaphet.springboottemplate.security.AuthenticationType;

@Service
public class AppUserService implements UserDetailsService {

    private Logger logger = LogManager.getLogger(AppUserService.class);

    private final BCryptPasswordEncoder passwordEncoder;
    private final AppUserRepository appUserRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final RoleService roleService;
    private final String USER_NOT_FOUND_MSG = "user with %s not found";

    private final String EMAIL_NOT_FOUND_MSG = "User not found with email %s" ;
    private final String ID_NOT_FOUND_MSG = "App user not found with id %d";
    private final String EMAIL_ALREADY_EXISTS_MSG = "Email %s already taken";
    private final int TOKEN_EXPIRATION_DELAY = 15;

    @Autowired
    public AppUserService(BCryptPasswordEncoder passwordEncoder,
                          AppUserRepository appUserRepository,
                          ConfirmationTokenService confirmationTokenService, RoleService roleService) {
        this.passwordEncoder = passwordEncoder;
        this.appUserRepository = appUserRepository;
        this.confirmationTokenService = confirmationTokenService;
        this.roleService = roleService;
    }

    @Cacheable(value = "appUsers")
    public Page<AppUser> getAppUsers(int currentPage, int size, String sortBy) {
        PageRequest pageable = PageRequest.of(currentPage,
                size,
                sortBy.startsWith("-") ? Sort.by(sortBy.substring(1)).descending() : Sort.by(sortBy)
        );
        return appUserRepository.findAll(pageable);
    }


    @Cacheable(value = "appUsers", key = "#id")
    public AppUser getAppUser(Long id) {
        return appUserRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ID_NOT_FOUND_MSG, id)));
    }

    @Cacheable(value = "appUsers", key = "#email")
    public AppUser getAppUser(String email) {
        return appUserRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(EMAIL_NOT_FOUND_MSG, email)));
    }

    @CacheEvict(value = "appUsers", allEntries = true)
    public void saveAppUser(AppUser appUser) {
        String email = appUser.getEmail();
        boolean emailExists = appUserRepository.findByEmail(email).isPresent();

        if (emailExists) {
            throw new ResourceAlreadyExistsException(String.format(EMAIL_ALREADY_EXISTS_MSG, email));
        }

        if (appUser.getRoles() == null) {
            appUser.setRoles(Set.of(roleService.getRole("ROLE_USER")));
        }

        if (appUser.getPassword() != null) {
            String encodedPassword = passwordEncoder.encode(appUser.getPassword());
            appUser.setPassword(encodedPassword);
        }

        if(appUser.getAuthType() == null){
            appUser.setAuthType(AuthenticationType.LOCAL);
        }

        appUserRepository.save(appUser);
    }

    @CacheEvict(value = "appUsers", allEntries = true)
    @Transactional
    public boolean updateAppUser(AppUser au) {
        boolean isUpdated = false;
        String newEmail = au.getEmail();
        String firstName = au.getFirstName();
        String lastName = au.getLastName();
        AppUser appUser = ((AppUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()).getAppUser();

        if (newEmail != null &&
                newEmail.length() > 0 &&
                !Objects.equals(newEmail, appUser.getEmail())) {
            if (appUserRepository.findByEmail(newEmail).isPresent()) {
                throw new ResourceAlreadyExistsException(String.format(EMAIL_ALREADY_EXISTS_MSG, newEmail));
            }
            appUser.setEmail(newEmail);
            isUpdated = true;
        }

        if (firstName != null &&
                firstName.length() > 0 &&
                !Objects.equals(appUser.getFirstName(), firstName)) {
            appUser.setFirstName(firstName);
            isUpdated = true;
        }
        if (lastName != null &&
                lastName.length() > 0 &&
                !Objects.equals(appUser.getLastName(), lastName)) {
            appUser.setLastName(lastName);
            isUpdated = true;
        }
        if (isUpdated) {
            appUserRepository.save(appUser);
        }
        return isUpdated;
    }

    @CacheEvict(value = "appUsers", allEntries = true)
    public void deleteAppUser(Long id) {
        AppUser appUser = appUserRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ID_NOT_FOUND_MSG, id)));
        Set<Role> appUserRoles = appUser.getRoles();
        for (Role role : appUserRoles) {
            role.removeAppUser(appUser);
        }
        appUserRepository.deleteById(id);
    }

    @CacheEvict(value = "appUsers", allEntries = true)
    @Transactional
    public int removeUnVerifiedUsers() {
        // remove users that have not been verified after 3 days
        return appUserRepository.deleteAllUnverifiedUsers(LocalDateTime.now().minusDays(3));
    }

    @CacheEvict(value = "appUsers", allEntries = true)
    @Transactional
    public void updateAuthenticationType(String username, String authType) {
        appUserRepository.updateAuthType(username, AuthenticationType.valueOf(authType.toUpperCase()));
    }

    @CacheEvict(value = "appUsers", allEntries = true)
    @Transactional
    public void updateAppUserRole(AppUser appUser) {
        String email = appUser.getEmail();

        appUserRepository
                .findByEmail(email)
//                .filter(user -> Objects.equals(appUser.getRoles(), user.getRoles()))
                .map(appUserRepository::save)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(EMAIL_NOT_FOUND_MSG, email)));
    }

    public String signUpUser(AppUser appUser) {
        String token = UUID.randomUUID().toString();

        saveAppUser(appUser);
        ConfirmationToken confirmationToken = new ConfirmationToken(token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(TOKEN_EXPIRATION_DELAY),
                appUser
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }

    @CacheEvict(value = "appUsers", allEntries = true)
    public void enableAppUser(String email) {
        appUserRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(EMAIL_NOT_FOUND_MSG, email)));
        appUserRepository.enableAppUser(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return appUserRepository.findByEmail(email)
                .map(AppUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public boolean isUserExists(String email) {
        return appUserRepository.findByEmail(email).isPresent();
    }
}
