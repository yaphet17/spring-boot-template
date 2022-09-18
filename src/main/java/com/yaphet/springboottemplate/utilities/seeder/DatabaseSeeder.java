package com.yaphet.springboottemplate.utilities.seeder;

import com.yaphet.springboottemplate.exceptions.ResourceAlreadyExistsException;
import com.yaphet.springboottemplate.models.AppUser;
import com.yaphet.springboottemplate.models.Privilege;
import com.yaphet.springboottemplate.models.Role;
import com.yaphet.springboottemplate.services.*;
import com.yaphet.springboottemplate.security.AuthenticationType;

import lombok.RequiredArgsConstructor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class DatabaseSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LogManager.getLogger(DatabaseSeeder.class);
    private static boolean alreadySetup = false;
    private final AppUserService appUserService;
    private final RoleService roleService;
    private final PrivilegeService privilegeService;


    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup) {
            return;
        }

        if (!privilegeService.getPrivileges().isEmpty()) {
            alreadySetup = true;
            return;
        }

        //create default privilege
        Set<Privilege> privileges = new HashSet<>();
        privileges.add(new Privilege("ROLE-CREATE"));
        privileges.add(new Privilege("ROLE-DETAIL"));
        privileges.add(new Privilege("ROLE-EDIT"));
        privileges.add(new Privilege("ROLE-DELETE"));
        privileges.add(new Privilege("ROLE-ASSIGN_PRIVILEGE"));
        privileges.add(new Privilege("PRIVILEGE-DETAIL"));
        privileges.add(new Privilege("APPUSER-REGISTER"));
        privileges.add(new Privilege("APPUSER-DETAIL"));
        privileges.add(new Privilege("APPUSER-EDIT"));
        privileges.add(new Privilege("APPUSER-DELETE"));
        privileges.add(new Privilege("APPUSER-ASSIGN_ROLE"));

        try{
            for (Privilege privilege : privileges) {
                privilegeService.createPrivilege(privilege);
            }
        } catch (ResourceAlreadyExistsException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }


        //create default roles
        Set<Role> roles = new HashSet<>();
        roles.add(new Role("ROLE_SUPER_ADMIN", "Global access", privileges));
        roles.add(new Role("ROLE_ADMIN", "Limited Global access", privileges
                .stream()
                .filter(privilege -> !privilege.getPrivilegeName().equals("ROLE-CREATE"))
                .collect(Collectors.toSet())
        ));
        roles.add(new Role("ROLE_USER", "Limited Access", Set.of()));
        try {
            for (Role role : roles) {
                roleService.createRole(role);
            }
        } catch (ResourceAlreadyExistsException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }


        //create super admin user
        AppUser adminAppUser = new AppUser();
        adminAppUser.setFirstName("admin");
        adminAppUser.setLastName("admin");
        adminAppUser.setEmail("admin@admin.com");
        adminAppUser.setUserName("admin@admin.com");
        adminAppUser.setPassword("admin");
        adminAppUser.setRoles(roles);
        adminAppUser.setAuthType(AuthenticationType.LOCAL);
        adminAppUser.setEnabled(true);
        try {
            appUserService.saveAppUser(adminAppUser);
        } catch (ResourceAlreadyExistsException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        //create super admin user
        AppUser user = new AppUser();
        user.setFirstName("user");
        user.setLastName("user");
        user.setEmail("user@user.com");
        user.setUserName("user@user.com");
        user.setPassword("user");
        user.setRoles(Set.of(roleService.getRole("ROLE_USER")));
        user.setAuthType(AuthenticationType.LOCAL);
        user.setEnabled(true);
        try {
            appUserService.saveAppUser(user);
        } catch (ResourceAlreadyExistsException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        alreadySetup = true;

    }

}
