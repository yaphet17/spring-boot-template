package com.yaphet.springboottemplate.controllers;

import com.yaphet.springboottemplate.exceptions.ResourceAlreadyExistsException;
import com.yaphet.springboottemplate.models.AppUser;
import com.yaphet.springboottemplate.services.AppUserService;
import com.yaphet.springboottemplate.services.ProfileService;
import com.yaphet.springboottemplate.security.ChangePassword;

import lombok.RequiredArgsConstructor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
@RequestMapping("profile")
public class ProfileController {
    private static final Logger logger = LogManager.getLogger(ProfileController.class);
    private final AppUserService appUserService;
    private final ProfileService profileService;

    @Cacheable(value = "appUserProfile", key = "")
    @GetMapping
    public String getAppUser(Model model) {
        AppUser appUser = appUserService.getAppUser(getLoggedInUser());
        model.addAttribute("appUser", appUser);
        return "/profile/profile-detail";
    }

    @GetMapping("/edit")
    public String updateForm(Model model) {
        AppUser appUser = appUserService.getAppUser(getLoggedInUser());
        model.addAttribute("appUser", appUser);
        return "/profile/profile-edit";
    }

    @PostMapping("/edit")
    public String update(@Valid @ModelAttribute AppUser appUser,
                         BindingResult result,
                         RedirectAttributes redirectAttr) {
        if (result.hasErrors()) {
            logger.error(result.getAllErrors());
            return "/profile/profile-edit";
        }
        try {
            appUser = appUserService.updateAppUser(appUser);
            redirectAttr.addFlashAttribute("successMessage", "Profile successfully updated");
            return "redirect:/profile";
        } catch (ResourceAlreadyExistsException e) {
            redirectAttr.addFlashAttribute("errorMessage", e.getMessage());
            logger.debug(e.getMessage(), e);
            return "redirect:/profile/edit";
        }
    }

    @GetMapping("/change-password")
    public String changePasswordForm(Model model) {
        ChangePassword changePassword = new ChangePassword();

        changePassword.setUsername(getLoggedInUser());
        model.addAttribute("changePassword", changePassword);
        return "/profile/change-password";
    }

    @PostMapping("/change-password")
    @PreAuthorize("#changePassword.username == principal.username")
    public String changePassword(@ModelAttribute ChangePassword changePassword,
                                 BindingResult result,
                                 RedirectAttributes redirectAttr) {
        if (result.hasErrors()) {
            redirectAttr.addFlashAttribute("errorMessage", result.getFieldError());
            logger.error(result.getAllErrors());
            return "redirect: /profile/change-password";
        }
        try {
            profileService.changePassword(changePassword.getUsername(),
                    changePassword.getOldPassword(),
                    changePassword.getNewPassword());
            redirectAttr.addFlashAttribute("successMessage", "Password successfully changed");
            return "redirect:/profile";
        } catch (ResourceAlreadyExistsException e) {
            redirectAttr.addFlashAttribute("errorMessage", e.getMessage());
            logger.debug(e.getMessage(), e);
            return "redirect: /profile/change-password";
        }
    }

    private String getLoggedInUser() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }
}
