package com.yaphet.springtemplate.controllers;

import com.yaphet.springtemplate.models.Privilege;
import com.yaphet.springtemplate.services.PrivilegeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("privilege")
public class PrivilegeController {
    private final PrivilegeService privilegeService;

    @GetMapping
    public String privilegeList(Model model){
        List<Privilege> privilegeList=privilegeService.getAllPrivileges();
        model.addAttribute("privilegeList",privilegeList);
        return "privilege/privilege-list";
    }


}
