package com.pedromartins.backend_challenge.dto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class RecoveryJwtTokenDto {
    private final String token;
}
