package com.bharatnaai.bharatnaaibackend.UserDto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PasswordResetRequest {
    private String email;
}
