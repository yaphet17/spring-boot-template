package com.yaphet.springtemplate.services;

import com.yaphet.springtemplate.exceptions.RoleAlreadyExistException;
import com.yaphet.springtemplate.exceptions.RoleNotFoundException;
import com.yaphet.springtemplate.models.Privilege;
import com.yaphet.springtemplate.models.Role;
import com.yaphet.springtemplate.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public void createRole(Role role) {
        String roleName = role.getRoleName().toUpperCase();
        if(!roleName.startsWith("ROLE_")){
            roleName =  "ROLE_" + roleName;
        }
        role.setRoleName(roleName);
        boolean roleExists = roleRepository.findByRoleName(roleName).isPresent();

        if(roleExists){
            throw new RoleAlreadyExistException(roleName);
        }
        roleRepository.save(role);
    }
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }
    public Role getRole(String roleName) {
        return roleRepository
                .findByRoleName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(roleName));
    }
    public Role getRole(Long id){
        return roleRepository
                .findById(id)
                .orElseThrow(() -> new RoleNotFoundException(id));
    }

    public void deleteRole(Long id) {
        Role role = roleRepository
                .findById(id)
                .orElseThrow(() -> new RoleNotFoundException(id));

        for(Privilege privilege : role.getPrivileges()){
            privilege.removeRole(role);
        }
        roleRepository.deleteById(id);
    }

    @Transactional
    public boolean updateRole(Role newRole) {
        boolean isUpdated = false;
        Long id = newRole.getId();
        Role updatedRole = roleRepository
                .findById(newRole.getId())
                .orElseThrow(() -> new RoleNotFoundException(id));
        Role oldRole = updatedRole;

        if(newRole.getRoleName() != null &&
                newRole.getRoleName().length() > 0 &&
                !Objects.equals(updatedRole.getRoleName(), newRole.getRoleName())){
            updatedRole.setRoleName(newRole.getRoleName());

            isUpdated = true;
        }
        if(newRole.getRoleDescription() != null &&
                newRole.getRoleDescription().length() > 0 &&
                !Objects.equals(newRole.getRoleDescription(), updatedRole.getRoleDescription())){
            updatedRole.setRoleDescription(newRole.getRoleDescription());
            isUpdated = true;
        }
        if(isUpdated){
            roleRepository.save(updatedRole);
            Set<Privilege> privileges = oldRole.getPrivileges();
            if(privileges != null){
                for(Privilege privilege : privileges){
                    privilege.removeRole(oldRole);
                }
                for(Privilege privilege : privileges){
                    privilege.addRole(updatedRole);
                }
            }

        }


        return isUpdated;
    }
}
