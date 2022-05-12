package com.yaphet.springreacttemplate.controllers.error;

import com.yaphet.springreacttemplate.models.AppUser;
import com.yaphet.springreacttemplate.services.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("profile")
public class ProfileController {

    private AppUserService appUserService;

    @GetMapping("/detail/{id}")
    public String getAppUser(@PathVariable("id") Long id,Model model){
        AppUser appUser=appUserService.getAppUser(id);
        model.addAttribute("appUser",appUser);
        return "/profile/profile-detail";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable("id") Long id, Model model){
        AppUser appUser=appUserService.getAppUser(id);
        model.addAttribute("appUser",appUser);
        return "/profile/profile-edit";
    }
    @PostMapping("/update")
    public String update(@Valid @ModelAttribute AppUser appUSer, BindingResult result, RedirectAttributes redirectAttr){
        redirectAttr.addAttribute("id",appUSer.getId());
        if(result.hasErrors()){
            return "redirect:/profile/update";
        }
        appUserService.update(appUSer);
        return "redirect:/profile/detail/{id}";
    }



}
