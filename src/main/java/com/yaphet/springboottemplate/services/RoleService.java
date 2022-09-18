package com.yaphet.springboottemplate.services;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yaphet.springboottemplate.exceptions.ResourceAlreadyExistsException;
import com.yaphet.springboottemplate.exceptions.ResourceNotFoundException;
import com.yaphet.springboottemplate.models.Privilege;
import com.yaphet.springboottemplate.models.Role;
import com.yaphet.springboottemplate.repositories.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final String ROLE_NOT_FOUND_WITH_ID_MSG = "Role not found with id %d";
    private final String ROLE_NOT_FOUND_WITH_NAME_MSG = "Role %s not found";

    private final String ROLE_ALREADY_EXISTS = "Role %s already exists";

    @CacheEvict(cacheNames = "roles", allEntries = true)
    public Role createRole(Role role) throws ResourceAlreadyExistsException {
        String roleName = role.getRoleName().toUpperCase();
        if (!roleName.startsWith("ROLE_")) {
            roleName = "ROLE_" + roleName;
        }
        role.setRoleName(roleName);
        boolean roleExists = roleRepository.findByRoleName(roleName).isPresent();

        if (roleExists) {
            throw new ResourceAlreadyExistsException(String.format(ROLE_ALREADY_EXISTS, roleName));
        }
       return roleRepository.save(role);
    }

    @Cacheable(value = "roles")
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Cacheable(value = "roles")
    public Page<Role> getRolesByPage(int currentPage, int size, String sortBy) {
        Pageable pageable = PageRequest.of(currentPage,
                size,
                sortBy.startsWith("-") ? Sort.by(sortBy.substring(1)).descending() : Sort.by(sortBy)
        );

        return roleRepository.findAll(pageable);
    }

    @Cacheable(value = "roles", key = "#roleName")
    public Role getRole(String roleName) {
        return roleRepository
                .findByRoleName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ROLE_NOT_FOUND_WITH_NAME_MSG, roleName)));
    }

    @Cacheable(value = "roles", key = "#id")
    public Role getRole(Long id) {
        return roleRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ROLE_NOT_FOUND_WITH_ID_MSG, id)));
    }

    @CacheEvict(cacheNames = "roles", allEntries = true)
    public void deleteRole(Long id) {
        Role role = roleRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ROLE_NOT_FOUND_WITH_ID_MSG, id)));

        if (roleRepository.findByRole(id, 1) > 0) {
            throw new RuntimeException("Role is assigned to users");
            // TODO handle this exception
        }

        for (Privilege privilege : role.getPrivileges()) {
            privilege.removeRole(role);
        }
        roleRepository.deleteById(id);
    }

    @CacheEvict(cacheNames = "roles", allEntries = true)
    @Transactional
    public boolean updateRole(Role newRole) {
        boolean isUpdated = false;
        Long id = newRole.getId();
        Role updatedRole = roleRepository
                .findById(newRole.getId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ROLE_NOT_FOUND_WITH_ID_MSG, id)));

        if (newRole.getRoleName() != null &&
                newRole.getRoleName().length() > 0 &&
                !Objects.equals(updatedRole.getRoleName(), newRole.getRoleName())) {
            updatedRole.setRoleName(newRole.getRoleName());
            isUpdated = true;
        }
        if (newRole.getRoleDescription() != null &&
                newRole.getRoleDescription().length() > 0 &&
                !Objects.equals(newRole.getRoleDescription(), updatedRole.getRoleDescription())) {
            updatedRole.setRoleDescription(newRole.getRoleDescription());
            isUpdated = true;
        }

        if (isUpdated) {
            roleRepository.save(updatedRole);
            Set<Privilege> privileges = updatedRole.getPrivileges();
            if (!privileges.isEmpty()) {
                for (Privilege privilege : privileges) {
                    privilege.removeRole(updatedRole);
                }
                for (Privilege privilege : privileges) {
                    privilege.addRole(updatedRole);
                }
            }

        }


        return isUpdated;
    }
}
