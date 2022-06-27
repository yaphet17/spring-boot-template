package com.yaphet.springtemplate.viewmodels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangePassword {

    private Long id;
    private String newPassword;
    private String oldPassword;

}
