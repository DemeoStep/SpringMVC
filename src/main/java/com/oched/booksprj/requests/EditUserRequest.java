package com.oched.booksprj.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditUserRequest {
    @NotBlank
    private long id;
    @NotBlank
    private String login;
    @Email
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String role;
}
