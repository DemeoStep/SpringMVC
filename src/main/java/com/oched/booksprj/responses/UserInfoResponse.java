package com.oched.booksprj.responses;

import lombok.Value;

@Value
public class UserInfoResponse {
    long id;
    String login;
    String email;
}
