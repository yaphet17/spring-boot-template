package com.yaphet.springboottemplate.controllers;

import com.yaphet.springboottemplate.models.AppUser;
import com.yaphet.springboottemplate.models.Role;
import com.yaphet.springboottemplate.services.AppUserService;
import com.yaphet.springboottemplate.services.RoleService;
import com.yaphet.springboottemplate.utilities.SelectedRole;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;

import static com.yaphet.springboottemplate.controllers.error.CustomErrorController.getBindingErrorMessage;

@RequiredArgsConstructor
@Controller
@RequestMapping("user")
@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
public class AppUserController {
    private static final Logger log = LogManager.getLogger(AppUserController.class);
    private final AppUserService appUserService;
    private final RoleService roleService;

    @GetMapping
    public String getAppUsers(Model model){
        List<AppUser> appUserList = appUserService.getAppUsers();

        model.addAttribute("appUserList", appUserList);
        return "appuser/appuser-list";
    }

    @GetMapping("/detail/{id}")
    public String getAppUser(@PathVariable("id") Long id, Model model){
        AppUser appUser = appUserService.getAppUser(id);
        model.addAttribute("appUser", appUser);
        return "/appuser/appuser-detail";
    }

    @GetMapping("/create")
    public String createForm(Model model){
        AppUser appUser = new AppUser();
        List<Role> roleList = roleService.getRoles();

        model.addAttribute("appUser", appUser);
        model.addAttribute("selectedRole", new SelectedRole(roleList));
        return "appuser/create-appuser";
    }
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute AppUser appUser,
                         @Valid @ModelAttribute SelectedRole selectedRole,
                         BindingResult result,
                         RedirectAttributes redirectAttr){
        if(result.hasErrors()){
            log.error(getBindingErrorMessage() + " : " + result.getAllErrors());
            return "redirect:user/create";
        }
        appUser.setRoles(new HashSet<>(selectedRole.selectedRoles));
        appUser.setEnabled(true);
        appUserService.saveAppUser(appUser);
        Long id = appUserService.getAppUser(appUser.getEmail()).getId();
        redirectAttr.addAttribute("id", id);
        return "redirect:/user/detail/{id}";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        appUserService.deleteAppUser(id);
        return "redirect:/user";
    }

    @GetMapping("/assign-role/{id}")
    public String assignRoleForm(@PathVariable("id") Long id, Model model){
        AppUser appUser = appUserService.getAppUser(id);

        List<Role> roleList = roleService.getRoles();
        model.addAttribute("appUser", appUser);
        model.addAttribute("selectedRole", new SelectedRole(roleList));
        return "appuser/assign-role";
    }
    @PostMapping("/assign-role")
    public String assignRole(@RequestParam("id") Long id,
                             @Valid @ModelAttribute SelectedRole selectedRoles,
                             BindingResult result,
                             RedirectAttributes redirectAttributes){
        AppUser appUser = appUserService.getAppUser(id);

        redirectAttributes.addAttribute("id", id);
        if(result.hasErrors()){
            log.error(getBindingErrorMessage() + " : " + result.getAllErrors());
            return "redirect:/user/assign-role/{id}";
        }
        appUser.setRoles(new HashSet<>(selectedRoles.getSelectedRoles()));
        appUserService.updateAppUserRole(appUser);
        return "redirect:/user/detail/{id}";
    }



}
