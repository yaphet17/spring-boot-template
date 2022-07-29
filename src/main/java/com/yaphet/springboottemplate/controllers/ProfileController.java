package com.yaphet.springboottemplate.controllers;

import com.yaphet.springboottemplate.models.AppUser;
import com.yaphet.springboottemplate.services.AppUserService;
import com.yaphet.springboottemplate.services.ProfileService;
import com.yaphet.springboottemplate.utilities.security.ChangePassword;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
@RequestMapping("profile")
public class ProfileController {
    private static final Logger log = LogManager.getLogger(ProfileController.class);
    private final AppUserService appUserService;
    private final ProfileService profileService;

    @GetMapping
    public String getAppUser(Model model){
        AppUser appUser = appUserService.getAppUser(getLoggedInUser());
        model.addAttribute("appUser", appUser);
        return "/profile/profile-detail";
    }

    @GetMapping("/edit")
    public String updateForm(Model model){
        AppUser appUser = appUserService.getAppUser(getLoggedInUser());
        model.addAttribute("appUser", appUser);
        return "/profile/profile-edit";
    }
    @PostMapping("/edit")
    public String update(@Valid @ModelAttribute AppUser appUser, BindingResult result){
        if(result.hasErrors()){
            log.error(result.getAllErrors());
            return "redirect:/profile/edit";
        }
        appUserService.updateAppUser(appUser);
        return "redirect:/profile";
    }

    @GetMapping("/change-password")
    public String changePasswordForm(Model model){
        ChangePassword changePassword = new ChangePassword();

        changePassword.setUsername(getLoggedInUser());
        model.addAttribute("changePassword", changePassword);
        return "/profile/change-password";
    }
    @PostMapping("/change-password")
    @PreAuthorize("#changePassword.username == principal.username")
    public String changePassword(@ModelAttribute ChangePassword changePassword, BindingResult result){
        if(result.hasErrors()){
            log.error(result.getAllErrors());
            return "redirect: /profile/change-password";
        }
        profileService.changePassword(changePassword.getUsername(),
                changePassword.getOldPassword(),
                changePassword.getNewPassword());
        return "redirect:/profile";
    }

    private String getLoggedInUser(){
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }
}
