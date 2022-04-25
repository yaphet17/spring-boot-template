package com.yaphet.springreacttemplate.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
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

    @ManyToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
    @JoinTable(
            name="app_role_privileges",
            joinColumns = @JoinColumn(name="app_role_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="app_privilege_id",referencedColumnName = "id")
    )
    private Set<Privilege> privileges=new HashSet<>();

    public Role(String roleName,String roleDescription){
        this.roleName=roleName;
        this.roleDescription=roleDescription;

    }



}
