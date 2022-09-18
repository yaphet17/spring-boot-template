package com.yaphet.springboottemplate.controllers;

import static com.yaphet.springboottemplate.controllers.error.CustomErrorController.getBindingErrorMessage;

import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yaphet.springboottemplate.exceptions.ResourceAlreadyExistsException;
import com.yaphet.springboottemplate.models.Privilege;
import com.yaphet.springboottemplate.models.Role;
import com.yaphet.springboottemplate.services.PrivilegeService;
import com.yaphet.springboottemplate.services.RolePrivilegeService;
import com.yaphet.springboottemplate.services.RoleService;
import com.yaphet.springboottemplate.utilities.SelectPrivilege;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("role")
public class RoleController {

    private static final Logger log = LogManager.getLogger();
    private final RoleService roleService;
    private final PrivilegeService privilegeService;

    private final RolePrivilegeService rolePrivilegeService;

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public String roleList(Model model,
                           @RequestParam(value = "page", required = false, defaultValue = "1") int currentPage,
                           @RequestParam(value = "size", required = false, defaultValue = "5") int size,
                           @RequestParam(value = "sort", required = false, defaultValue = "roleName") String sortBy) {
        Page<Role> rolesPage = roleService.getRolesByPage(currentPage - 1, size, sortBy);
        List<Role> roleList = rolesPage.getContent();

        model.addAttribute("roleList", roleList);
        model.addAttribute("totalPages", rolesPage.getTotalPages());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalElements", rolesPage.getTotalElements());
        return "role/role-list";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyAuthority('ROLE-CREATE')")
    public String createRoleForm(Model model) {
        Role role = new Role();
        model.addAttribute("role", role);
        return "role/create-role";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ROLE-CREATE')")
    public String createRole(@Valid @ModelAttribute Role role,
                             BindingResult result,
                             RedirectAttributes redirectAttr) {
        if (result.hasErrors()) {
            redirectAttr.addFlashAttribute("errorMessage", result.getFieldError());
            log.error(getBindingErrorMessage() + " : " + result.getAllErrors());
            return "redirect: role/create";
        }

        try {
            role = roleService.createRole(role);
            redirectAttr
                    .addAttribute("id", role.getId())
                    .addFlashAttribute("successMessage", "Role successfully created");
            return "redirect:/role/detail/{id}";
        } catch (ResourceAlreadyExistsException e) {
            redirectAttr.addFlashAttribute("errorMessage", e.getMessage());
            log.debug(e.getMessage(), e);
            return "redirect:/role/create";
        }
    }

    @GetMapping("/detail/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public String roleDetail(@PathVariable("id") Long id, Model model) {
        Role role = roleService.getRole(id);
        model.addAttribute("role", role);
        return "role/role-detail";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE-CREATE')")
    public String updateForm(@PathVariable("id") Long id, Model model) {
        Role role = roleService.getRole(id);
        model.addAttribute("role", role);
        return "role/edit-role";
    }

    @PostMapping("/edit")
    @PreAuthorize("hasAnyAuthority('ROLE-CREATE')")
    public String update(@Valid @ModelAttribute Role role,
                         BindingResult result,
                         RedirectAttributes redirectAttr) {
        redirectAttr.addAttribute("id", role.getId());
        if (result.hasErrors()) {
            log.error(getBindingErrorMessage() + " : " + result.getAllErrors());
            return "redirect:/role/edit/{id}";
        }
        if (roleService.updateRole(role)) {
            redirectAttr.addAttribute("success", "successfully updated");
        } else {
            redirectAttr.addAttribute("error", "no change found");
            return "redirect:/role/edit/{id}";
        }
        return "redirect:/role/detail/{id}";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE-CREATE')")
    public String deleteRole(@PathVariable("id") Long id) {
        roleService.deleteRole(id);
        return "redirect:/role/";
    }

    @GetMapping("/assign-privilege/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE-CREATE')")
    public String assignPrivilegeForm(@PathVariable("id") Long id, Model model) {
        Role role = roleService.getRole(id);
        List<Privilege> privilegeList = privilegeService.getPrivileges();
        model.addAttribute("role", role);
        model.addAttribute("selectedPrivilege", new SelectPrivilege(privilegeList));
        return "role/assign-privilege";
    }

    @PostMapping("/assign-privilege")
    @PreAuthorize("hasAnyAuthority('ROLE-EDIT')")
    public String assignPrivilege(@RequestParam("id") Long id,
                                  @Valid @ModelAttribute SelectPrivilege selectPrivilege,
                                  BindingResult result,
                                  RedirectAttributes redirectAttr) {
        Role role = roleService.getRole(id);
        redirectAttr.addAttribute("id", role.getId());
        if (result.hasErrors()) {
            log.error(getBindingErrorMessage() + " : " + result.getAllErrors());
            return "redirect:/role/assign-privilege/{id}";
        }
        rolePrivilegeService.assignPrivilege(selectPrivilege.getSelectedPrivileges(), role.getRoleName());
        return "redirect:/role/detail/{id}";
    }


}
