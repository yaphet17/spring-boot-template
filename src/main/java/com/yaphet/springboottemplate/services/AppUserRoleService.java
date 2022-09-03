package com.yaphet.springboottemplate.services;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.yaphet.springboottemplate.models.AppUser;
import com.yaphet.springboottemplate.models.Role;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AppUserRoleService {

    private final AppUserService appUserService;
    private final RoleService roleService;

    public void assignRole(String email, String roleName) {
        AppUser appUser = appUserService.getAppUser(email);
        Role role = roleService.getRole(roleName);
        Set<Role> roles = appUser.getRoles();

        roles.add(role);
        appUser.setRoles(roles);
        role.addAppUser(appUser);
        appUserService.updateAppUserRole(appUser);
    }
}
