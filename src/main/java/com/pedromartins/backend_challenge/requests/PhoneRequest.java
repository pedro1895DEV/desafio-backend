package com.pedromartins.backend_challenge.requests;
import com.pedromartins.backend_challenge.models.PhoneType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneRequest {
    private String number;
    private PhoneType type;
}
