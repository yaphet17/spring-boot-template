package com.yaphet.springtemplate.services;

import com.yaphet.springtemplate.exceptions.RoleAlreadyExistException;
import com.yaphet.springtemplate.exceptions.RoleNotFoundException;
import com.yaphet.springtemplate.models.Role;
import com.yaphet.springtemplate.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public void createRole(Role role) {
        String roleName = role.getRoleName();
        boolean roleExists =   roleRepository.findByRoleName(roleName).isPresent();

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
        roleRepository
                .findById(id)
                .orElseThrow(() -> new RoleNotFoundException(id));
        roleRepository.deleteById(id);
    }

    @Transactional
    public boolean updateRole(Role r) {
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
            isUpdated = true;
        }
        return isUpdated;
    }


}
