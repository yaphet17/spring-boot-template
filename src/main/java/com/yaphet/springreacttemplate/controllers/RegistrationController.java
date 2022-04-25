package com.yaphet.springreacttemplate.controllers;

import com.yaphet.springreacttemplate.services.RegistrationService;
import com.yaphet.springreacttemplate.models.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@AllArgsConstructor
@Controller
@RequestMapping("user")
public class RegistrationController {

    private final RegistrationService appUserRegistrationService;

    @GetMapping("/register")
    public String registerForm(Model model){
        AppUser appUser=new AppUser();
        model.addAttribute("appUser",appUser);
        return "registration/register-user";
    }
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("appUser") AppUser appUser, BindingResult result){
        if(result.hasErrors()){
            return "registration/register-user";
        }
        appUserRegistrationService.register(appUser);
        return "redirect:/login";
    }
    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token){
        appUserRegistrationService.confirmToken(token);
        return "registration/email-verified";
    }
    @GetMapping("/home")
    public String home(){
        return "registration/home";
    }

}
