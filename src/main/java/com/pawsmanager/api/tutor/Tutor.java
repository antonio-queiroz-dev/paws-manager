package com.pawsmanager.api.tutor;

import com.pawsmanager.api.pet.Pet;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tutores")
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String nome;

    @Email
    private String email;

    private String telefone;

    @Embedded
    private AddressTutor enderecoTutor;

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pet> pets = new ArrayList<>();

    public Tutor() {
    }

    public Tutor(long id, String nome, String email, String telefone, AddressTutor addressTutor, List<Pet> pets) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.enderecoTutor = addressTutor;
        this.pets = pets;
    }

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public AddressTutor getEnderecoTutor() {
        return enderecoTutor;
    }

    public void setEnderecoTutor(AddressTutor addressTutor) {
        this.enderecoTutor = addressTutor;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }
}
