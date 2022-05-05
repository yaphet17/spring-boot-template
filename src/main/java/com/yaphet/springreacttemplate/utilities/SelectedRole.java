package com.yaphet.springreacttemplate.utilities;

import com.yaphet.springreacttemplate.models.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SelectedRole {
    public List<Role> selectedRoles;
}
