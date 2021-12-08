package com.oched.booksprj.responses;

import lombok.Value;

@Value
public class BookResponseWithContent {
    long id;
    String title;
    int year;
    String authorFirstName;
    String authorLastName;
    String content;
}
