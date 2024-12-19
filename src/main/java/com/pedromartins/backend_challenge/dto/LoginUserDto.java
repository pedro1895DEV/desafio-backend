package com.pedromartins.backend_challenge.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class LoginUserDto {
    private final String email;
    private final String password;
}
