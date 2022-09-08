package com.yaphet.springboottemplate.services;

import com.yaphet.springboottemplate.exceptions.IdNotFoundException;
import com.yaphet.springboottemplate.exceptions.PrivilegeAlreadyExistsException;
import com.yaphet.springboottemplate.exceptions.PrivilegeNotFoundException;
import com.yaphet.springboottemplate.models.Privilege;
import com.yaphet.springboottemplate.repositories.PrivilegeRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrivilegeService {

    private final PrivilegeRepository privilegeRepository;

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

    public void createPrivilege(Privilege privilege) {
        String privilegeName = privilege.getPrivilegeName();
        boolean privilegeExists = privilegeRepository.findByPrivilegeName(privilegeName).isPresent();

        if (privilegeExists) {
            throw new PrivilegeAlreadyExistsException(privilegeName);
        }
        privilegeRepository.save(privilege);
    }

    @Cacheable(value = "privileges", key = "#privilegeName")
    public Privilege getPrivilege(String privilegeName) {
        return privilegeRepository
                .findByPrivilegeName(privilegeName)
                .orElseThrow(() -> new PrivilegeNotFoundException(privilegeName));
    }

    @Cacheable(cacheNames = "privileges", key = "#id")
    public Privilege getPrivilege(Long id) {
        return privilegeRepository
                .findById(id)
                .orElseThrow(() -> new IdNotFoundException("Privilege", id));
    }

}
