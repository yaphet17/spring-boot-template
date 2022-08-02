package com.yaphet.springboottemplate.services;

import com.yaphet.springboottemplate.exceptions.RoleAlreadyExistException;
import com.yaphet.springboottemplate.exceptions.RoleNotFoundException;
import com.yaphet.springboottemplate.models.Privilege;
import com.yaphet.springboottemplate.models.Role;
import com.yaphet.springboottemplate.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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

    @Cacheable(cacheNames = "roles", key = "#root.methodName")
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Cacheable(cacheNames = "roles", key = "#roleName")
    public Role getRole(String roleName) {
        return roleRepository
                .findByRoleName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(roleName));
    }

    @Cacheable(cacheNames = "roles", key = "#roleName")
    public Role getRole(Long id){
        return roleRepository
                .findById(id)
                .orElseThrow(() -> new RoleNotFoundException(id));
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "roles", key = "#id", beforeInvocation = false),
            @CacheEvict(cacheNames = "roles", key = "#result", beforeInvocation = false),
    }
    )
    public String deleteRole(Long id) {
        Role role = roleRepository
                .findById(id)
                .orElseThrow(() -> new RoleNotFoundException(id));

        for(Privilege privilege : role.getPrivileges()){
            privilege.removeRole(role);
        }
        roleRepository.deleteById(id);
        return role.getRoleName();
    }

    @Caching(put = {
            @CachePut( cacheNames = "roles", key = "#newRole.roleName"),
            @CachePut( cacheNames = "roles", key = "#newRole.id")
    })
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
