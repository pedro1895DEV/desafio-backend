package com.pedromartins.backend_challenge.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
        @Id
        @GeneratedValue
        private UUID id;

        @Column(name = "name", nullable = false, length = 100)
        private String name;

        @Column(name = "cpf", nullable = false, unique = true)
        private String cpf;

        @Column(name="password", nullable = false)
        private String password;

        @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
        @JoinTable(name="users_roles",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name="role_id"))
        private List<Role> roles;

        @OneToOne(cascade = CascadeType.ALL)
        @JsonManagedReference
        @JoinColumn(name = "address_id", referencedColumnName = "id")
        private Address address;

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonManagedReference
        private List<Phone> phones = new ArrayList<>();

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonManagedReference
        private List<Email> emails = new ArrayList<>();


    public void setName(String name) throws Exception{
        if (name.length() <= 3) {
            throw new Exception("Name must have more than 3 characters");
        } else if (name.length() > 100) {
            throw new Exception("Name must have less than 100 characters");
        }
        this.name = name;
    }
}
