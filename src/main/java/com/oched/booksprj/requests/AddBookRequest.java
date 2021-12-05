package com.oched.booksprj.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddBookRequest {
    @NotBlank
    private long id;
    @NotBlank
    private String title;
    @NotBlank
    private int year;
    @NotBlank
    private String authorFirstName;
    @NotBlank
    private String authorLastName;
}
