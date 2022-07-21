package com.yaphet.springtemplate.controllers;

import com.yaphet.springtemplate.models.AppUser;
import com.yaphet.springtemplate.services.AppUserService;
import com.yaphet.springtemplate.services.ProfileService;
import com.yaphet.springtemplate.viewmodels.ChangePassword;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("profile")
public class ProfileController {

    private final AppUserService appUserService;
    private final ProfileService profileService;

    @GetMapping("/{id}")
    @PreAuthorize("#id == principal.id")
    public String getAppUser(@PathVariable("id") Long id, Model model){
        AppUser appUser = appUserService.getAppUser(id);
        model.addAttribute("appUser", appUser);
        return "/profile/profile-detail";
    }

    @GetMapping("/update/{id}")
    @PreAuthorize("#id == principal.id")
    public String updateForm(@PathVariable("id") Long id, Model model){
        AppUser appUser = appUserService.getAppUser(id);
        model.addAttribute("appUser", appUser);
        return "/profile/profile-edit";
    }
    @PostMapping("/update")
    @PreAuthorize("#appUser.id == principal.id")
    public String update(@Valid @ModelAttribute AppUser appUSer, BindingResult result, RedirectAttributes redirectAttr){
        if(result.hasErrors()){
            return "redirect:/profile/update";
        }
        redirectAttr.addAttribute("id", appUSer.getId());
        appUserService.updateAppUser(appUSer);
        return "redirect:/profile/{id}";
    }

    @GetMapping("/change-password/{id}")
    @PreAuthorize("#id == principal.id")
    public String changePasswordForm(@PathVariable("id") Long id, Model model){
        ChangePassword changePassword = new ChangePassword();

        changePassword.setId(id);
        model.addAttribute("changePassword",changePassword);
        return "/profile/change-password";
    }
    @PostMapping("/change-password")
    @PreAuthorize("#changePassword.id == principal.id")
    public String changePasswordForm(@ModelAttribute ChangePassword changePassword,BindingResult result,RedirectAttributes redirectAttributes){
        Long id = changePassword.getId();

        redirectAttributes.addAttribute("id",id);
        if(result.hasErrors()){
            return "redirect: /profile/change-password/{id}";
        }
        profileService.changePassword(id,changePassword.getOldPassword(),changePassword.getNewPassword());
        return "redirect:/profile/{id}";
    }
}
