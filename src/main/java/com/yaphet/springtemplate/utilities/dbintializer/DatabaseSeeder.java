package com.yaphet.springtemplate.utilities.dbintializer;

import com.yaphet.springtemplate.models.AppUser;
import com.yaphet.springtemplate.models.Privilege;
import com.yaphet.springtemplate.models.Role;
import com.yaphet.springtemplate.services.*;
import lombok.RequiredArgsConstructor;
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

    private static boolean alreadySetup = false;
    private final AppUserService appUserService;
    private final RoleService roleService;
    private final PrivilegeService privilegeService;



    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if(alreadySetup){
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
        for(Privilege privilege : privileges){
            privilegeService.createPrivilege(privilege);
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
        for(Role role : roles){
            roleService.createRole(role);
        }

        //create super admin user
        AppUser adminAppUser = new AppUser();
        adminAppUser.setFirstName("admin");
        adminAppUser.setLastName("admin");
        adminAppUser.setEmail("admin@admin.com");
        adminAppUser.setUserName("admin@admin.com");
        adminAppUser.setPassword("admin");
        adminAppUser.setRoles(roles);
        adminAppUser.setEnabled(true);
        appUserService.saveAppUser(adminAppUser);

        //create super admin user
        AppUser user = new AppUser();
        user.setFirstName("user");
        user.setLastName("user");
        user.setEmail("user@user.com");
        user.setUserName("user@user.com");
        user.setPassword("user");
        user.setRoles(Set.of(roleService.getRole("ROLE_USER")));
        user.setEnabled(true);
        appUserService.saveAppUser(user);

        alreadySetup = true;

    }

}
