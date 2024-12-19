package com.pedromartins.backend_challenge.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "addresses")
@Getter
@Setter
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "cep", nullable = false)
    private String cep;

    @Column(name= "patio", nullable = false)
    private String patio;

    @Column(name = "Neighborhood", nullable = false)
    private String neighborhood;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "UF", nullable = false)
    private String uf;

    @Column(name = "Complement")
    private String complement;

    @OneToOne(mappedBy = "address")
    @JsonBackReference
    private User user;
}
