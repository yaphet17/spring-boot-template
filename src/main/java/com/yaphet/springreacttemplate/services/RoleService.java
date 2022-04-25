package com.yaphet.springreacttemplate.services;

import com.yaphet.springreacttemplate.models.Role;
import com.yaphet.springreacttemplate.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public List<Role> getRoleList() {
        return roleRepository.findAll();
    }
    public void createRole(Role role) {
        boolean roleExists=roleRepository.findByRoleName(role.getRoleName()).isPresent();
        //throw exception if role already exists
        if(roleExists){
            throw new IllegalStateException("Role already exist");
        }
        //create role
        roleRepository.save(role);
    }
    public Role getRoleByName(String roleName) {
        return roleRepository.findByRoleName(roleName).orElseThrow(()->new IllegalStateException("Role doesn't exist with name="+roleName));
    }
    public Role getRoleById(Long id){
        return roleRepository.findById(id).orElseThrow(()->new IllegalStateException("Role doesn't exist"));
    }

    public void deleteRole(Long roleId) {
        boolean isRoleExist=roleRepository.findById(roleId).isPresent();
        if(!isRoleExist){
            throw new IllegalStateException("Role no found with id="+roleId);
        }
        //delete role
        roleRepository.deleteById(roleId);
    }

    @Transactional
    public boolean update(Role r) {
        boolean isUpdated=false;
        Role role=roleRepository.findById(r.getId()).orElseThrow(()->new IllegalStateException("Role not found"));
        if(r.getRoleName()!=null&&r.getRoleName().length()>0&&!Objects.equals(role.getRoleName(),r.getRoleName())){
            role.setRoleName(r.getRoleName());
            isUpdated=true;
        }
        if(r.getRoleDescription()!=null&&r.getRoleDescription().length()>0&&!Objects.equals(r.getRoleDescription(),role.getRoleDescription())){
            role.setRoleDescription(r.getRoleDescription());
            isUpdated=true;
        }
        return isUpdated;
    }




}
