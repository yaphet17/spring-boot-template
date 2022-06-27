package com.yaphet.springreacttemplate.services;

import com.yaphet.springreacttemplate.exceptions.RoleAlreadyExistException;
import com.yaphet.springreacttemplate.exceptions.RoleNotFoundException;
import com.yaphet.springreacttemplate.models.Role;
import com.yaphet.springreacttemplate.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public List<Role> getRoleList() {
        return roleRepository.findAll();
    }
    public void createRole(Role role) {
        String roleName = role.getRoleName();
        roleRepository
                .findByRoleName(roleName)
                .orElseThrow(() -> new RoleAlreadyExistException(roleName));
        roleRepository.save(role);
    }
    public Role getRoleByName(String roleName) {
        return roleRepository
                .findByRoleName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(roleName));
    }
    public Role getRoleById(Long id){
        return roleRepository
                .findById(id)
                .orElseThrow(() -> new RoleNotFoundException(id));
    }

    public void deleteRole(Long id) {
        roleRepository
                .findById(id)
                .orElseThrow(() -> new RoleNotFoundException(id));
        roleRepository.deleteById(id);
    }

    @Transactional
    public boolean update(Role r) {
        boolean isUpdated = false;
        Long id = r.getId();
        Role role = roleRepository
                .findById(r.getId())
                .orElseThrow(() -> new RoleNotFoundException(id));

        if(r.getRoleName() != null &&
                r.getRoleName().length() > 0 &&
                !Objects.equals(role.getRoleName(), r.getRoleName())){
            role.setRoleName(r.getRoleName());
            isUpdated = true;
        }
        if(r.getRoleDescription() != null &&
                r.getRoleDescription().length() > 0 &&
                !Objects.equals(r.getRoleDescription(), role.getRoleDescription())){
            role.setRoleDescription(r.getRoleDescription());
            isUpdated=true;
        }
        return isUpdated;
    }


}
