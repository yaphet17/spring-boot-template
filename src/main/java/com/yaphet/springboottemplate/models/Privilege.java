package com.yaphet.springboottemplate.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Set;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@NoArgsConstructor
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "privileges")
public class Privilege implements Serializable {
    @Id
    @SequenceGenerator(
            name = "privilege_sequence",
            sequenceName = "privilege_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "privilege_sequence"
    )
    @Column(name = "privilege_id")
    private Long id;
    @Column(name = "privilege_name", nullable = false, unique = true)
    private String privilegeName;

    @ManyToMany(mappedBy = "privileges")
    private Set<Role> roles;

    public Privilege(String privilegeName) {
        this.privilegeName = privilegeName;
    }

    public void addRole(Role role) {
        roles.add(role);
        role.getPrivileges().add(this);
    }

    public void removeRole(Role role) {
        roles.remove(role);
        role.getPrivileges().remove(this);
    }

}
