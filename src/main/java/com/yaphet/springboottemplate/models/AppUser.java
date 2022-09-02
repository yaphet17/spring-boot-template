package com.yaphet.springboottemplate.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.yaphet.springboottemplate.security.AuthenticationType;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "app_users")
@SQLDelete(sql = "UPDATE app_users SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class AppUser implements Serializable {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    @Column(name = "appuser_id")
    private Long id;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String userName;
    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "password")
    private String password;
    @Past
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dob;
    @ManyToMany(cascade= {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "appuser_roles",
            joinColumns = @JoinColumn(
                    name = "appuser_id",
                    referencedColumnName = "appuser_id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id",
                    referencedColumnName = "role_id"
            )
    )
    private Set<Role> roles = new HashSet<>();
    @Column(name = "auth_type", nullable = false)
    private AuthenticationType authType;
    @Column(name = "created_at")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "modified_at")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime modifiedAt = LocalDateTime.now();
    private Boolean enabled = false;
    private Boolean locked = false;
    private Boolean deleted = false;


    public AppUser(String firstName, String lastName, String email, String password, LocalDate dob) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.dob = dob;
    }

    public String getUserName() {
        return email;
    }

    public void setUserName(String email) {
        this.userName = email;
    }
}
