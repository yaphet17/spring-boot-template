package com.yaphet.springreacttemplate.services;

import com.yaphet.springreacttemplate.models.Privilege;
import com.yaphet.springreacttemplate.services.PrivilegeService;
import com.yaphet.springreacttemplate.models.Role;
import com.yaphet.springreacttemplate.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class RolePrivilegeService {

    private final RoleService roleService;
    private final PrivilegeService privilegeService;

    public void assignPrivilege(List<Privilege> privileges, String roleName) {
        Role role=roleService.getRoleByName(roleName);
        Set<Privilege> rolePrivileges=role.getPrivileges();
        for(Privilege privilege:privileges){
            //TODO: make sure getPrivilegeByName can return null
            if(privilegeService.getPrivilegeByName(privilege.getPrivilegeName())!=null){
                rolePrivileges.add(privilege);
            }
        }
        role.setPrivileges(rolePrivileges);
        updateRolePrivilege(role);
    }

    public void updateRolePrivilege(Role role){
        Role tempRole=roleService.getRoleById(role.getId());
        if(Objects.equals(role.getPrivileges(),tempRole.getPrivileges())){
            //TODO: handle when there is no change in role
            return;
        }
        roleService.update(role);
    }
}
