package com.yaphet.springboottemplate.controllers;

import com.yaphet.springboottemplate.models.Privilege;
import com.yaphet.springboottemplate.models.Role;
import com.yaphet.springboottemplate.services.PrivilegeService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("privilege")
@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
public class PrivilegeController {
    private final PrivilegeService privilegeService;

    @GetMapping
    public String privilegeList(Model model,
                                @RequestParam(value = "page", required = false, defaultValue = "1") int currentPage,
                                @RequestParam(value = "size", required = false, defaultValue = "5") int size,
                                @RequestParam(value = "sort", required = false, defaultValue = "privilegeName") String sortBy) {
        Page<Privilege> privilegesPage = privilegeService.getPrivilegesByPage(currentPage - 1, size, sortBy);
        List<Privilege> privilegeList = privilegesPage.getContent();

        model.addAttribute("privilegeList", privilegeList);
        model.addAttribute("totalPages", privilegesPage.getTotalPages());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalElements", privilegesPage.getTotalElements());
        return "privilege/privilege-list";
    }


}
