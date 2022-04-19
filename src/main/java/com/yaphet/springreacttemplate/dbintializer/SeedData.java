package com.yaphet.springreacttemplate.dbintializer;

import com.yaphet.springreacttemplate.appuser.AppUser;
import com.yaphet.springreacttemplate.appuser.AppUserService;
import com.yaphet.springreacttemplate.privilege.Privilege;
import com.yaphet.springreacttemplate.privilege.PrivilegeService;
import com.yaphet.springreacttemplate.role.Role;
import com.yaphet.springreacttemplate.role.RoleService;
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
    private final PrivilegeService privilegeService;


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
        adminAppUser.setPassword(bCryptPasswordEncoder.encode("admin"));
        adminAppUser.setEnabled(true);
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
            privilegeService.create(privilege);
        }
        //assign privileges to super_admin role;
        privilegeService.assignPrivilege(privileges,"SUPER_ADMIN");
        //assign super admin role
        roleService.assignRole(adminAppUser.getEmail(),"SUPER_ADMIN");

        alreadySetup=true;

    }

}
