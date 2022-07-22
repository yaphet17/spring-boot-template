package com.yaphet.springtemplate.services;

import com.yaphet.springtemplate.models.AppUser;
import com.yaphet.springtemplate.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

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
