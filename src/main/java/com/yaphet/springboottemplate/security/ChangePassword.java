package com.yaphet.springboottemplate.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ChangePassword {
    private String username;
    private String newPassword;
    private String oldPassword;
}
