package com.yaphet.springreacttemplate.services;

import com.yaphet.springreacttemplate.models.AppUser;
import com.yaphet.springreacttemplate.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class AppUserRoleService {

    private final AppUserService appUserService;
    private final RoleService roleService;

    public void assignRole(String email,String roleName) {
        AppUser appUser = appUserService.getAppUser(email);
        Role role = roleService.getRoleByName(roleName);
        Set<Role> roles = appUser.getRoles();
        roles.add(role);
        appUser.setRoles(roles);
        appUserService.updateAppUserRole(appUser);
    }
}
