package com.yaphet.springreacttemplate.controllers;

import com.yaphet.springreacttemplate.models.AppUser;
import com.yaphet.springreacttemplate.services.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("user")
public class AppUserController {
    private final AppUserService appUserService;

    @GetMapping
    public String getAppUsers(Model model){
        List<AppUser> appUserList=appUserService.getAppUsers();
        model.addAttribute("appUserList",appUserList);
        return "appuser/appuser-list";
    }
    @GetMapping("/create")
    public String createForm(Model model){
        AppUser appUser=new AppUser();
        model.addAttribute("appUser",appUser);
        return "appuser/create-role";
    }
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute AppUser appUser, BindingResult result, RedirectAttributes redirectAttr){
        if(result.hasErrors()){
            redirectAttr.addAttribute("error","failed to create user");
            return "redirect:/role/create";
        }
        appUserService.save(appUser);
        redirectAttr.addAttribute("success","user successfully updated");
        return "redirect:/user/detail";
    }
    @GetMapping("/detaill/{id}")
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
