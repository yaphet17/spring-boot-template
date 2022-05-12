package com.yaphet.springreacttemplate.viewmodels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangePasword {

    private String newPassword;
    private String oldPassword;

}
