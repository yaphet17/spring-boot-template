package com.yaphet.springreacttemplate.utilities;

import com.yaphet.springreacttemplate.models.Privilege;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SelectPrivilege {
    private List<Privilege> selectedPrivileges;
}
