package com.bharatnaai.bharatnaaibackend.UserDto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PasswordResetDto {
    private String token;
    private String newPassword;
}
