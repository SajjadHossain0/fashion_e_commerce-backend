package com.fashion_e_commerce.User.Entities;

import lombok.*;

@Getter
@Setter
public class LoginRequest {
    private String email;
    private String password;
}