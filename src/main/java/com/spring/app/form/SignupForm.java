package com.spring.app.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.spring.app.validate.Email;

import lombok.Data;

@Data
public class SignupForm {
    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 4, max = 16)
    private String password;
}
