package com.bharatnaai.bharatnaaibackend.UserDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class PasswordResetTokenResponse {
    private String email;
    private String token;
}
