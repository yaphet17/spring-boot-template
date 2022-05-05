package com.yaphet.springreacttemplate.controllers;

import com.yaphet.springreacttemplate.models.AppUser;
import com.yaphet.springreacttemplate.models.Role;
import com.yaphet.springreacttemplate.services.AppUserService;
import com.yaphet.springreacttemplate.services.RoleService;
import com.yaphet.springreacttemplate.utilities.SelectedRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
            redirectAttr.addAttribute("error","failed to create user");
            return "redirect:/create";
        }
        appUser.setRoles(selectedRole.selectedRoles.stream().collect(Collectors.toSet()));
        appUserService.save(appUser);
        redirectAttr.addAttribute("success","user successfully updated");
        return "redirect:/user/detail";
    }
    @GetMapping("/detail/{id}")
    public String getAppUser(@PathVariable("id") Long id,Model model){
        AppUser appUser=appUserService.getAppUser(id);
        model.addAttribute("appUser",appUser);
        return "/appuser/appuser-detail";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable("id") Long id,Model model){
        AppUser appUser=appUserService.getAppUser(id);
        model.addAttribute("appUser",appUser);
        return "/appuser/appuser-update";
    }
    @PostMapping("/update")
    public String update(@Valid @ModelAttribute AppUser appUSer,BindingResult result,RedirectAttributes redirectAttr){
        if(result.hasErrors()){
            redirectAttr.addAttribute("id",appUSer.getId());
            redirectAttr.addAttribute("error","failed to update user");
            return "redirect:/update/{id}";
        }
        appUserService.update(appUSer);
        redirectAttr.addAttribute("success","user successfully update");
        return "redirect:/user/detail";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        appUserService.delete(id);
        return "redirect:/user";
    }



}
