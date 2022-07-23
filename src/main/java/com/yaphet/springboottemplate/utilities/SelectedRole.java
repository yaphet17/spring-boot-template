package com.yaphet.springboottemplate.utilities;

import com.yaphet.springboottemplate.models.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SelectedRole {
    public List<Role> selectedRoles;
}
