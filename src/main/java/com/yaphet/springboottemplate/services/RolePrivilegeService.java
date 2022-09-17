package com.yaphet.springboottemplate.services;

import com.yaphet.springboottemplate.models.Privilege;
import com.yaphet.springboottemplate.models.Role;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RolePrivilegeService {

    private final RoleService roleService;

    public void assignPrivilege(List<Privilege> privileges, String roleName) {
        Role role = roleService.getRole(roleName);
        Set<Privilege> rolePrivileges = role.getPrivileges();

        rolePrivileges.addAll(privileges);
        role.setPrivileges(rolePrivileges);
        updateRolePrivilege(role);
        for (Privilege privilege : privileges) {
            privilege.addRole(role);
        }
    }

    @Transactional
    public void updateRolePrivilege(Role role) {
        Role tempRole = roleService.getRole(role.getId());
        if (Objects.equals(role.getPrivileges(), tempRole.getPrivileges())) {
            //TODO: handle no change update
        }
        roleService.updateRole(role);
    }
}
