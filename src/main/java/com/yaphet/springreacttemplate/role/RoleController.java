package com.yaphet.springreacttemplate.role;

import com.yaphet.springreacttemplate.privilege.Privilege;
import com.yaphet.springreacttemplate.privilege.PrivilegeService;
import com.yaphet.springreacttemplate.privilege.SelectPrivilege;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("role")
public class RoleController {

    private final RoleService roleService;
    private final PrivilegeService privilegeService;

    @GetMapping("/index")
    public String roleList(Model model){
        List<Role> roleList=roleService.getRoleList();
        model.addAttribute("roleList",roleList);
        return "role-list";
    }
    //TODO: role detail

    @GetMapping("/create")
    public String createRoleForm(Model model){
        Role role=new Role();
        model.addAttribute("role",role);
        return "create-role";
    }

    @PostMapping("/create")
    public String createRole(@Valid @ModelAttribute Role role, BindingResult result){
       if(result.hasErrors()){
           return "create-role";
       }
       roleService.createRole(role);
        return "redirect:/role/index";
    }
    @GetMapping("/delete/{id}")
    public String deleteRole(@PathVariable("id") Long id){
        roleService.deleteRole(id);
        return "role-list";
    }
    @GetMapping("/edit/{id}")
    public String updateForm(@PathVariable("id") Long id, Model model){
        Role role=roleService.getRoleById(id);
        model.addAttribute("role",role);
        return "edit-role";
    }
    @PostMapping("/edit")
    public String update(@Valid @ModelAttribute Role role,BindingResult result,RedirectAttributes redirectAttr){
        redirectAttr.addAttribute("id",role.getId());
        if(result.hasErrors()){
           return "redirect:/role/edit/{id}";
       }
        //if role is updated return success message else redirect back to update form
        if(roleService.update(role)){
            redirectAttr.addAttribute("success","successfully updated");
        }else{
            redirectAttr.addAttribute("error","no change found");
            return "redirect:/role/edit/{id}";
        }
        return "redirect:/role/detail/{id}";
    }
    @GetMapping("/assign-privilege/{id}")
    public String assignPrivilegeForm(@PathVariable("id") Long id,Model model){
        Role role=roleService.getRoleById(id);
        List<Privilege> privilegeList=privilegeService.getAllPrivileges();
        model.addAttribute("role",role);
        model.addAttribute("selectedPrivilege",new SelectPrivilege(privilegeList));
        return "assign-privilege";
    }
    @PostMapping("/assign-privilege")
    public String assignPrivilege(@ModelAttribute Role role,@ModelAttribute SelectPrivilege selectPrivilege,BindingResult result,
                                  RedirectAttributes redirectAttr){
        if(result.hasErrors()){
            redirectAttr.addAttribute("error","failed to assign privilege to "+role.getRoleName()+" role");
            return "redirect:assign-privilege/"+role.getId();
        }
        roleService.assignPrivilege(selectPrivilege.getSelectedPrivileges(),role.getRoleName());
        redirectAttr.addAttribute("id",role.getId());
        redirectAttr.addAttribute("success","privilege successfully assigned");
        return "redirect:/role/detail/{id}";
    }


}
