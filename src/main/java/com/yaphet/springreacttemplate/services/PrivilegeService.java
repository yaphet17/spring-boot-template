package com.yaphet.springreacttemplate.services;

import com.yaphet.springreacttemplate.models.Privilege;
import com.yaphet.springreacttemplate.repositories.PrivilegeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
public class PrivilegeService {
    public List<Privilege> getAllPrivileges() {
        return privilegeRepository.findAll();
    }

    private final PrivilegeRepository privilegeRepository;

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




}
