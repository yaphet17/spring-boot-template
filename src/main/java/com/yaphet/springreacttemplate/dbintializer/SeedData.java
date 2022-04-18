package com.yaphet.springreacttemplate.dbintializer;

import com.yaphet.springreacttemplate.appuser.AppUser;
import com.yaphet.springreacttemplate.appuser.AppUserService;
import com.yaphet.springreacttemplate.role.Role;
import com.yaphet.springreacttemplate.role.RoleService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
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


    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if(alreadySetup){
            return;
        }
        //create super admin user
        AppUser appUser=new AppUser();
        appUser.setFirstName("admin");
        appUser.setLastName("admin");
        appUser.setEmail("admin@admin.com");
        appUser.setPassword(bCryptPasswordEncoder.encode("admin"));
        appUser.setEnabled(true);
        appUser.setDob(LocalDate.now());

        appUserService.saveAppUser(appUser);
        //create default roles
        List<Role> roles=new ArrayList<>();
        roles.add(new Role("SUPER_ADMIN","Global access"));
        roles.add(new Role("ADMIN","Global access"));
        roles.add(new Role("USER","Limited Access"));
        for(Role role:roles){
            roleService.createRole(role);
        }
        //assign super admin role
        roleService.assignRole(appUser.getEmail(),"SUPER_ADMIN");

        alreadySetup=true;

    }
}
