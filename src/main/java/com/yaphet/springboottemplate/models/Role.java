package com.yaphet.springboottemplate.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

@NoArgsConstructor
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "roles")
public class Role implements Serializable {

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
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<AppUser> appUsers;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_privileges",
            joinColumns = @JoinColumn(
                    name = "role_id",
                    referencedColumnName = "role_id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id",
                    referencedColumnName = "privilege_id"
            )
    )
    private Set<Privilege> privileges;
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt;
    @Column(name = "modified_at")
    @LastModifiedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime modifiedAt;

    public Role(String roleName, String roleDescription, Set<Privilege> privileges) {
        this.roleName = roleName;
        this.roleDescription = roleDescription;
        this.privileges = privileges;
    }

    public void addAppUser(AppUser appUser) {
        appUsers.add(appUser);
        appUser.getRoles().add(this);
    }

    public void removeAppUser(AppUser appUser) {
        appUsers.remove(appUser);
        appUser.getRoles().remove(this);
    }


}
