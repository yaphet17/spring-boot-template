package com.yaphet.springreacttemplate.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="app_users")
public class AppUser {
    @Id
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    private Long id;
    @NotBlank(message = "First name field required")
    @Pattern(regexp = "^[A-Za-z]*$", message = "Invalid first name format")
    @Column(nullable = false)
    private String firstName;
    @NotBlank(message = "Last name field required")
    @Pattern(regexp = "^[A-Za-z]*$", message = "Invalid last name format")
    @Column(nullable = false)
    private String lastName;
    private String userName;
    @NotBlank(message = "Email field required")
    @Email
    private String email;
    @NotBlank(message = "Password field required")
    @Column(nullable = false)
    private String password;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dob;
    @ManyToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
    @JoinTable(
            name="app_user_roles",
            joinColumns = @JoinColumn(name="app_user_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="app_role_id",referencedColumnName = "id")
    )
    private Set<Role> roles=new HashSet<>();
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt=LocalDateTime.now();
    private Boolean enabled = false;
    private Boolean locked = false;

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
