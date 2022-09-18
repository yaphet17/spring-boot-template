package com.yaphet.springboottemplate.controllers;

import com.yaphet.springboottemplate.exceptions.EmailAlreadyConfirmedException;
import com.yaphet.springboottemplate.exceptions.ResourceAlreadyExistsException;
import com.yaphet.springboottemplate.models.AppUser;
import com.yaphet.springboottemplate.services.RegistrationService;

import lombok.RequiredArgsConstructor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import static com.yaphet.springboottemplate.controllers.error.CustomErrorController.getBindingErrorMessage;

@RequiredArgsConstructor
@Controller
@RequestMapping("")
public class RegistrationController {
    private static final Logger logger = LogManager.getLogger(RegistrationController.class);
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
    public String register(@Valid @ModelAttribute("appUser") AppUser appUser,
                           BindingResult result,
                           RedirectAttributes redirectAttr) {
        if (result.hasErrors()) {
            redirectAttr.addFlashAttribute("errorMessage", result.getFieldError());
            logger.error(getBindingErrorMessage() + " : " + result.getAllErrors());
            return "redirect:/register";
        }

        try{
            appUserRegistrationService.register(appUser);
            return "redirect:/login";
        } catch (ResourceAlreadyExistsException e) {
            redirectAttr.addFlashAttribute("errorMessage", e.getMessage());
            logger.debug(e.getMessage(), e);
            return "redirect:/register";
        }
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token, Model model) {
        try {
            appUserRegistrationService.confirmToken(token);
        } catch (EmailAlreadyConfirmedException e) {
            logger.debug(e.getMessage(), e);
            model.addAttribute("errorMessage", e.getMessage());
            return "registration/email-verified";
        }
        model.addAttribute("successMessage", "Your email is successfully verified");
        return "registration/email-verified";
    }


}
