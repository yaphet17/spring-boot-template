package com.yaphet.springtemplate.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @SequenceGenerator(
            name = "role_sequence",
            sequenceName = "role_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "role_sequence"
    )
    @Column(name = "role_id")
    private Long id;
    @Column(name = "role_name", nullable = false, unique = true)
    private String roleName;
    @Column(name = "role_description")
    private String roleDescription;

    @ManyToMany(mappedBy =  "roles")
    private Set<AppUser> appUsers;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name ="role_privileges",
            joinColumns = @JoinColumn(
                    name ="role_id",
                    referencedColumnName = "role_id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name ="privilege_id",
                    referencedColumnName = "privilege_id"
            )
    )
    private Set<Privilege> privileges;

    public Role(String roleName, String roleDescription, Set<Privilege> privileges){
        this.roleName = roleName;
        this.roleDescription = roleDescription;
        this.privileges = privileges;
    }



}
