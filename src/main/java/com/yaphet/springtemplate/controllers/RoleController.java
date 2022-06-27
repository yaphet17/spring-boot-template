package com.yaphet.springtemplate.controllers;

import com.yaphet.springtemplate.models.Privilege;
import com.yaphet.springtemplate.models.Role;
import com.yaphet.springtemplate.services.RoleService;
import com.yaphet.springtemplate.services.PrivilegeService;
import com.yaphet.springtemplate.utilities.SelectPrivilege;
import com.yaphet.springtemplate.services.RolePrivilegeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("role")
public class RoleController {

    private final RoleService roleService;
    private final PrivilegeService privilegeService;

    private final RolePrivilegeService rolePrivilegeService;

    @GetMapping
    public String roleList(Model model){
        List<Role> roleList=roleService.getRoleList();
        model.addAttribute("roleList",roleList);
        return "role/role-list";
    }
    @GetMapping("/create")
    public String createRoleForm(Model model){
        Role role=new Role();
        model.addAttribute("role",role);
        return "role/create-role";
    }

    @PostMapping("/create")
    public String createRole(@Valid @ModelAttribute Role role, BindingResult result){
        if(result.hasErrors()){
            return "role/create-role";
        }
        roleService.createRole(role);
        return "redirect:/role";
    }

    @GetMapping("/detail/{id}")
    public String roleDetail(@PathVariable("id") Long id,Model model){
        Role role=roleService.getRoleById(id);
        model.addAttribute("role",role);
        return "role/role-detail";
    }
    @GetMapping("/edit/{id}")
    public String updateForm(@PathVariable("id") Long id, Model model){
        Role role=roleService.getRoleById(id);
        model.addAttribute("role",role);
        return "role/edit-role";
    }
    @PostMapping("/edit")
    public String update(@Valid @ModelAttribute Role role,BindingResult result,RedirectAttributes redirectAttr){
        redirectAttr.addAttribute("id",role.getId());
        if(result.hasErrors()){
           return "redirect:/role/edit/{id}";
       }
        //if role is updated return success message else redirect back to update form
        if(roleService.update(role)){
            redirectAttr.addAttribute("success","successfully updated");
        }else{
            redirectAttr.addAttribute("error","no change found");
            return "redirect:/role/edit/{id}";
        }
        return "redirect:/role/detail/{id}";
    }
    @GetMapping("/delete/{id}")
    public String deleteRole(@PathVariable("id") Long id){
        roleService.deleteRole(id);
        return "redirect:/role/";
    }
    @GetMapping("/assign-privilege/{id}")
    public String assignPrivilegeForm(@PathVariable("id") Long id,Model model){
         Role role=roleService.getRoleById(id);
        List<Privilege> privilegeList=privilegeService.getAllPrivileges();
        model.addAttribute("role",role);
        model.addAttribute("selectedPrivilege",new SelectPrivilege(privilegeList));
        return "role/assign-privilege";
    }
    @PostMapping("/assign-privilege")
    public String assignPrivilege(@RequestParam("id") Long id,@Valid @ModelAttribute SelectPrivilege selectPrivilege,BindingResult result,
                                  RedirectAttributes redirectAttr){
        Role role=roleService.getRoleById(id);
        redirectAttr.addAttribute("id",role.getId());
        if(result.hasErrors()){
            return "redirect:/role/assign-privilege/{id}";
        }
        rolePrivilegeService.assignPrivilege(selectPrivilege.getSelectedPrivileges(),role.getRoleName());
        return "redirect:/role/detail/{id}";
    }



}
