package com.yaphet.springreacttemplate.appuserrole;

import com.yaphet.springreacttemplate.appuser.AppUser;
import com.yaphet.springreacttemplate.appuser.AppUserService;
import com.yaphet.springreacttemplate.role.Role;
import com.yaphet.springreacttemplate.role.RoleService;
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
