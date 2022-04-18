package com.yaphet.springreacttemplate.role;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="app_roles")
public class Role {

    @Id
    @SequenceGenerator(name="role_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="role_sequence")
    private Long id;
    @NotBlank(message="Role name required")
    private String roleName;
    private String roleDescription;

    public Role(String roleName,String roleDescription){
        this.roleName=roleName;
        this.roleDescription=roleDescription;
    }



}
