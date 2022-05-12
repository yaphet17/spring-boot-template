package com.yaphet.springreacttemplate.controllers.error;

import com.yaphet.springreacttemplate.models.AppUser;
import com.yaphet.springreacttemplate.services.AppUserService;
import com.yaphet.springreacttemplate.services.ProfileService;
import com.yaphet.springreacttemplate.viewmodels.ChangePassword;
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

    private final AppUserService appUserService;
    private final ProfileService profileService;

    @GetMapping("/{id}")
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
        return "redirect:/profile/{id}";
    }

    @GetMapping("/change-password/{id}")
    public String changePasswordForm(@PathVariable("id") Long id, Model model){
        ChangePassword changePassword=new ChangePassword();
        changePassword.setId(id);
        model.addAttribute("changePassword",changePassword);
        return "/profile/change-password";
    }
    @PostMapping("/change-password")
    public String changePasswordForm(@ModelAttribute ChangePassword changePassword,BindingResult result,RedirectAttributes redirectAttributes){
        Long id= changePassword.getId();
        redirectAttributes.addAttribute("id",id);
        if(result.hasErrors()){
            return "redirect: /profile/change-password/{id}";
        }
        profileService.changePassword(id,changePassword.getOldPassword(),changePassword.getNewPassword());
        return "redirect:/profile/{id}";
    }




}
