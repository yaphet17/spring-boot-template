package com.yaphet.springreacttemplate.services;

import com.yaphet.springreacttemplate.models.Privilege;
import com.yaphet.springreacttemplate.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class RolePrivilegeService {

    private final RoleService roleService;

    public void assignPrivilege(List<Privilege> privileges, String roleName) {
        Role role=roleService.getRoleByName(roleName);
        Set<Privilege> rolePrivileges=role.getPrivileges();
        rolePrivileges.addAll(privileges);
        role.setPrivileges(rolePrivileges);
        updateRolePrivilege(role);
    }

    public void updateRolePrivilege(Role role){
        Role tempRole=roleService.getRoleById(role.getId());
        if(Objects.equals(role.getPrivileges(),tempRole.getPrivileges())){
//            throw new IllegalStateException("No change found on role privileges");
            //TODO: handle no change update
        }
        roleService.update(role);
    }
}
