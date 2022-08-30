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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Cacheable(cacheNames = "roles", key = "#root.methodName")
    public Page<Role> getRolesByPage(int currentPage, int size, String sortBy) {
        Pageable pageable = PageRequest.of(currentPage,
                                            size,
                                            sortBy.startsWith("-") ? Sort.by(sortBy.substring(1)).descending() : Sort.by(sortBy)
                                           );
        return roleRepository.findAll(pageable);
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

    @CacheEvict(cacheNames = "roles", allEntries = true)
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

    @CacheEvict(cacheNames = "roles", allEntries = true)
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
