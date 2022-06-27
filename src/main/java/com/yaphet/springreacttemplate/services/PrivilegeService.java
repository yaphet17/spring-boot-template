package com.yaphet.springreacttemplate.services;

import com.yaphet.springreacttemplate.exceptions.IdNotFoundException;
import com.yaphet.springreacttemplate.exceptions.PrivilegeAlreadyExistsException;
import com.yaphet.springreacttemplate.exceptions.PrivilegeNotFoundException;
import com.yaphet.springreacttemplate.models.Privilege;
import com.yaphet.springreacttemplate.repositories.PrivilegeRepository;
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

    public Privilege getPrivilegeByName(String privilegeName){
        return privilegeRepository
                .findByPrivilegeName(privilegeName)
                .orElseThrow(() -> new PrivilegeNotFoundException(privilegeName));
    }
    public Privilege getPrivilegeById(Long id){
        return privilegeRepository
                .findById(id)
                .orElseThrow(()->new IdNotFoundException("Privilege", id));
    }

    public void create(Privilege privilege){
        String privilegeName = privilege.getPrivilegeName();
         privilegeRepository
                 .findByPrivilegeName(privilegeName)
                 .orElseThrow(() -> new PrivilegeAlreadyExistsException(privilegeName));

        privilegeRepository.save(privilege);
    }

}
