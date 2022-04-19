package com.yaphet.springreacttemplate.privilege;

import com.yaphet.springreacttemplate.role.Role;
import com.yaphet.springreacttemplate.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class PrivilegeService {
    public List<Privilege> getAllPrivileges() {
        return privilegeRepository.findAll();
    }

    private final PrivilegeRepository privilegeRepository;
    private final RoleService roleService;

    public Privilege getPrivilegeByName(String privilegeName){
        return privilegeRepository.findByPrivilegeName(privilegeName).orElseThrow(()->new IllegalStateException("Privilege not found with name"+privilegeName));
    }
    public Privilege getPrivilegeById(Long id){
        return privilegeRepository.findById(id).orElseThrow(()->new IllegalStateException("Privilege bot found with id="+id));
    }

    public void create(Privilege privilege){
        boolean privilegeExists=privilegeRepository.findByPrivilegeName(privilege.getPrivilegeName()).isPresent();
        if(privilegeExists){
            throw new IllegalStateException("Privilege already exists");
        }
        privilegeRepository.save(privilege);
    }

    public void assignPrivilege(List<Privilege> privileges, String roleName) {
        Role role=roleService.getRoleByName(roleName);
        Set<Privilege> rolePrivileges=role.getPrivileges();
        for(Privilege privilege:privileges){
            if(privilegeRepository.findByPrivilegeName(privilege.getPrivilegeName()).isPresent()){
                rolePrivileges.add(privilege);
            }
        }
        role.setPrivileges(rolePrivileges);
        roleService.updateRolePrivilege(role);
    }


}
