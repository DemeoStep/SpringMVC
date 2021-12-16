package com.oched.booksprj.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditUserRequest {
    @NotNull
    private long id;
    @NotBlank
    private String login;
    @Email
    private String email;
    @NotNull
    private String password;
    @NotBlank
    private String role;
}
