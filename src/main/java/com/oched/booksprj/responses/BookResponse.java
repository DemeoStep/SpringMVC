package com.oched.booksprj.responses;

import lombok.Value;

@Value
public class BookResponse {
    long id;
    String title;
    int year;
    String authorFirstName;
    String authorLastName;
}
