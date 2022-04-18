package com.yaphet.springreacttemplate.appuserregistration;

import com.yaphet.springreacttemplate.appuser.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@AllArgsConstructor
@Controller
@RequestMapping("user")
public class AppUserRegistrationController {

    private final AppUserRegistrationService appUserRegistrationService;

    @GetMapping("/register")
    public String registerForm(Model model){
        AppUser appUser=new AppUser();
        model.addAttribute("appUser",appUser);
        return "register-user";
    }
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("appUser") AppUser appUser, BindingResult result){
        if(result.hasErrors()){
            return "register-user";
        }
        appUserRegistrationService.register(appUser);
        return "redirect:/login";
    }
    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token){
        appUserRegistrationService.confirmToken(token);
        return "email-verified";
    }
    @GetMapping("/home")
    public String home(){
        return "home";
    }

}
