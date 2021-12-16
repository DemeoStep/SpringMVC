package com.oched.booksprj.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewBookRequest {
    @NotBlank
    private String title;
    @NotNull
    private int year;
    @NotBlank
    private String authorFirstName;
    @NotBlank
    private String authorLastName;
    @NotBlank
    private String content;
}
