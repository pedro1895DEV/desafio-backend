package com.pedromartins.backend_challenge.requests;
import com.pedromartins.backend_challenge.models.RoleName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserRequest {
    private String name;
    private String cpf;
    private List<EmailRequest> emails;
    private List<PhoneRequest> phones;
    private String password;
    private List<RoleName> role;
    private AddressRequest address;
}
