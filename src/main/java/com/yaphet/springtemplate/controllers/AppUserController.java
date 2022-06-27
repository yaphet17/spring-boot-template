package com.yaphet.springtemplate.controllers;

import com.yaphet.springtemplate.models.AppUser;
import com.yaphet.springtemplate.models.Role;
import com.yaphet.springtemplate.services.AppUserService;
import com.yaphet.springtemplate.services.RoleService;
import com.yaphet.springtemplate.utilities.SelectedRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("user")
public class AppUserController {
    private final AppUserService appUserService;
    private final RoleService roleService;

    @GetMapping
    public String getAppUsers(Model model){
        List<AppUser> appUserList=appUserService.getAppUsers();
        model.addAttribute("appUserList",appUserList);
        return "appuser/appuser-list";
    }
    @GetMapping("/create")
    public String createForm(Model model){
        AppUser appUser=new AppUser();
        List<Role> roleList=roleService.getRoleList();
        model.addAttribute("appUser",appUser);
        model.addAttribute("selectedRole",new SelectedRole(roleList));
        return "appuser/create-appuser";
    }
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute AppUser appUser,@Valid @ModelAttribute SelectedRole selectedRole,BindingResult result, RedirectAttributes redirectAttr){
        if(result.hasErrors()){
            return "redirect:user/create";
        }
        appUser.setRoles(new HashSet<>(selectedRole.selectedRoles));
        appUserService.saveAppUser(appUser);
        Long id=appUserService.getAppUser(appUser.getEmail()).getId();
        redirectAttr.addAttribute("id",id);
        return "redirect:/user/detail/{id}";
    }
    @GetMapping("/detail/{id}")
    public String getAppUser(@PathVariable("id") Long id,Model model){
        AppUser appUser=appUserService.getAppUser(id);
        model.addAttribute("appUser",appUser);
        return "/appuser/appuser-detail";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        appUserService.deleteAppUser(id);
        return "redirect:/user";
    }

    @GetMapping("/assign-role/{id}")
    public String assignRoleForm(@PathVariable("id") Long id,Model model){
        AppUser appUser=appUserService.getAppUser(id);
        List<Role> roleList=roleService.getRoleList();
        model.addAttribute("appUser",appUser);
        model.addAttribute("selectedRole",new SelectedRole(roleList));
        return "appuser/assign-role";
    }
    @PostMapping("/assign-role")
    public String assignRole(@RequestParam("id") Long id,@Valid @ModelAttribute SelectedRole selectedRoles,BindingResult result,RedirectAttributes redirectAttributes){
        AppUser appUser=appUserService.getAppUser(id);
        redirectAttributes.addAttribute("id",id);
        if(result.hasErrors()){
            return "redirect: user/assign-role/{id}";
        }
        appUser.setRoles(new HashSet<>(selectedRoles.getSelectedRoles()));
        appUserService.updateAppUserRole(appUser);
        return "redirect: user/detail/{id}";
    }



}
