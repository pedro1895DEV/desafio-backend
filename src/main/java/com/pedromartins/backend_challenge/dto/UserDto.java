package com.pedromartins.backend_challenge.dto;

import com.pedromartins.backend_challenge.models.Address;
import com.pedromartins.backend_challenge.models.Phone;
import com.pedromartins.backend_challenge.models.User;
import com.pedromartins.backend_challenge.models.Email;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserDto {
    private UUID id;
    private String name;
    private List<String> emails;
    private String cpf;
    private List <String> phone;
    private List<String> role;
    private String cep;
    private String patio;
    private String neighborhood;
    private String city;
    private String uf;
    private String complement;

    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.emails = user.getEmails().stream().map(Email::getEmail).collect(Collectors.toList());
        this.cpf = user.getCpf();
        this.phone = user.getPhones().stream().map(Phone::getPhone).collect(Collectors.toList());
        this.role = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());
        Address address = user.getAddress();
        if (address != null) {
            this.cep = address.getCep();
            if (this.cep != null) {
                this.cep = this.cep.replaceAll("[^0-9]", "");
                if (this.cep.length() >= 8) {
                    this.cep = "XXX.XXX-" + this.cep.substring(5);
                }
            }
            this.patio = address.getPatio();
            this.neighborhood = address.getNeighborhood();
            this.city = address.getCity();
            this.uf = address.getUf();
            this.complement = address.getComplement();
        }
    }
}
