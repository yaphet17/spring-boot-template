package com.yaphet.springreacttemplate.privilege;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SelectPrivilege {
    private List<Privilege> selectedPrivileges;
}
