package com.yaphet.springboottemplate.services;

import com.yaphet.springboottemplate.exceptions.IdNotFoundException;
import com.yaphet.springboottemplate.exceptions.PrivilegeAlreadyExistsException;
import com.yaphet.springboottemplate.exceptions.PrivilegeNotFoundException;
import com.yaphet.springboottemplate.models.Privilege;
import com.yaphet.springboottemplate.repositories.PrivilegeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrivilegeService {
    public List<Privilege> getAllPrivileges() {
        return privilegeRepository.findAll();
    }

    private final PrivilegeRepository privilegeRepository;

    public void createPrivilege(Privilege privilege){
        String privilegeName = privilege.getPrivilegeName();
        boolean privilegeExists = privilegeRepository.findByPrivilegeName(privilegeName).isPresent();

        if(privilegeExists){
            throw new PrivilegeAlreadyExistsException(privilegeName);
        }
        privilegeRepository.save(privilege);
    }
    public Privilege getPrivilege(String privilegeName){
        return privilegeRepository
                .findByPrivilegeName(privilegeName)
                .orElseThrow(() -> new PrivilegeNotFoundException(privilegeName));
    }
    public Privilege getPrivilege(Long id){
        return privilegeRepository
                .findById(id)
                .orElseThrow(() -> new IdNotFoundException("Privilege", id));
    }

}
