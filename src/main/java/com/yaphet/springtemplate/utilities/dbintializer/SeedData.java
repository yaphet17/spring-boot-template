package com.yaphet.springtemplate.utilities.dbintializer;

import com.yaphet.springtemplate.models.AppUser;
import com.yaphet.springtemplate.services.AppUserService;
import com.yaphet.springtemplate.services.AppUserRoleService;
import com.yaphet.springtemplate.models.Privilege;
import com.yaphet.springtemplate.services.PrivilegeService;
import com.yaphet.springtemplate.models.Role;
import com.yaphet.springtemplate.services.RoleService;
import com.yaphet.springtemplate.services.RolePrivilegeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SeedData implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup=false;
    private final AppUserService appUserService;
    private final RoleService roleService;
    private final AppUserRoleService appUserRoleService;
    private final PrivilegeService privilegeService;
    private final RolePrivilegeService rolePrivilegeService;


    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if(alreadySetup){
            return;
        }
        //TODO: Check whether db is already initialized
        //create super admin user
        AppUser adminAppUser=new AppUser();
        adminAppUser.setFirstName("admin");
        adminAppUser.setLastName("admin");
        adminAppUser.setEmail("admin@admin.com");
        adminAppUser.setUserName("admin@admin.com");
        adminAppUser.setPassword("admin");
        adminAppUser.setDob(LocalDate.now());
        appUserService.saveAppUser(adminAppUser);
        //create default roles
        List<Role> roles=new ArrayList<>();
        roles.add(new Role("SUPER_ADMIN","Global access"));
        roles.add(new Role("ADMIN","Global access"));
        roles.add(new Role("USER","Limited Access"));
        for(Role role:roles){
            roleService.createRole(role);
        }
        //create default privilege
        List<Privilege> privileges=new ArrayList<>();
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
        for(Privilege privilege:privileges){
            privilegeService.createPrivilege(privilege);
        }
        //assign privileges to super_admin role;
        rolePrivilegeService.assignPrivilege(privileges,"SUPER_ADMIN");
        //assign super admin role
        appUserRoleService.assignRole(adminAppUser.getEmail(),"SUPER_ADMIN");

        alreadySetup=true;

    }

}
