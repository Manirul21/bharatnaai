package com.bharatnaai.bharatnaaibackend.UserDto;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}
