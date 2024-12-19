package com.pedromartins.backend_challenge.requests;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequest {
    private String cep;
    private String patio;
    private String neighborhood;
    private String city;
    private String uf;
    private String complement;
}
