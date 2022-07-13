package com.yaphet.springtemplate.services;

import com.yaphet.springtemplate.exceptions.IdNotFoundException;
import com.yaphet.springtemplate.exceptions.PrivilegeAlreadyExistsException;
import com.yaphet.springtemplate.exceptions.PrivilegeNotFoundException;
import com.yaphet.springtemplate.models.Privilege;
import com.yaphet.springtemplate.repositories.PrivilegeRepository;
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
                .orElseThrow(()->new IdNotFoundException("Privilege", id));
    }


}
