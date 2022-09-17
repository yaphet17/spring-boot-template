package com.yaphet.springboottemplate.services;

import java.util.List;
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
        appUserService.updateAppUserRole(email, roleName);
    }
}
