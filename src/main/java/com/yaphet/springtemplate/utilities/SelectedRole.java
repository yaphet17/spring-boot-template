package com.yaphet.springtemplate.utilities;

import com.yaphet.springtemplate.models.Role;
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
