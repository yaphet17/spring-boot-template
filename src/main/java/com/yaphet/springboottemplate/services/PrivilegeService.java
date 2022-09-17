package com.yaphet.springboottemplate.services;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.yaphet.springboottemplate.exceptions.ResourceAlreadyExistsException;
import com.yaphet.springboottemplate.exceptions.ResourceNotFoundException;
import com.yaphet.springboottemplate.models.Privilege;
import com.yaphet.springboottemplate.repositories.PrivilegeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrivilegeService {

    private final PrivilegeRepository privilegeRepository;
    private final String ID_NOT_FOUND_MSG = "Privilege not found with id %d";
    private final String PRIVILEGE_NOT_FOUND_MSG = "Privilege %s not found";
    private final String PRIVILEGE_ALREADY_EXISTS_MSG = "Privilege %s already exists";


    @Cacheable(value = "privileges")
    public List<Privilege> getPrivileges() {
        return privilegeRepository.findAll();
    }

    @Cacheable(value = "privileges")
    public Page<Privilege> getPrivilegesByPage(int currentPage, int size, String sortBy) {
        Pageable pageable = PageRequest.of(currentPage,
                size,
                sortBy.startsWith("-") ? Sort.by(sortBy.substring(1)).descending() : Sort.by(sortBy)
        );
        return privilegeRepository.findAll(pageable);
    }

    @CacheEvict(value = "privileges", allEntries = true)
    public void createPrivilege(Privilege privilege) {
        String privilegeName = privilege.getPrivilegeName();
        boolean privilegeExists = privilegeRepository.findByPrivilegeName(privilegeName).isPresent();

        if (privilegeExists) {
            throw new ResourceAlreadyExistsException(String.format(PRIVILEGE_ALREADY_EXISTS_MSG, privilegeName));
        }
        privilegeRepository.save(privilege);
    }

    @Cacheable(value = "privileges", key = "#privilegeName")
    public Privilege getPrivilege(String privilegeName) {
        return privilegeRepository
                .findByPrivilegeName(privilegeName)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(PRIVILEGE_NOT_FOUND_MSG, privilegeName)));
    }

    @Cacheable(cacheNames = "privileges", key = "#id")
    public Privilege getPrivilege(Long id) {
        return privilegeRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ID_NOT_FOUND_MSG, id)));
    }

}
