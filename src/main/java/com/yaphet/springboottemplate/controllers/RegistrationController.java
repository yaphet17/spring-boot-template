package com.yaphet.springboottemplate.controllers;

import com.yaphet.springboottemplate.models.AppUser;
import com.yaphet.springboottemplate.services.RegistrationService;

import lombok.RequiredArgsConstructor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.yaphet.springboottemplate.controllers.error.CustomErrorController.getBindingErrorMessage;

@RequiredArgsConstructor
@Controller
@RequestMapping("")
public class RegistrationController {
    private static final Logger log = LogManager.getLogger(RegistrationController.class);
    private final RegistrationService appUserRegistrationService;

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/register")
    public String registrationForm(Model model) {
        AppUser appUser = new AppUser();
        model.addAttribute("appUser", appUser);
        return "registration/register-user";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("appUser") AppUser appUser, BindingResult result) {
        if (result.hasErrors()) {
            log.error(getBindingErrorMessage() + " : " + result.getAllErrors());
            return "registration/register-user";
        }
        appUserRegistrationService.register(appUser);
        return "redirect:/login";
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token) {
        appUserRegistrationService.confirmToken(token);
        return "registration/email-verified";
    }


}
