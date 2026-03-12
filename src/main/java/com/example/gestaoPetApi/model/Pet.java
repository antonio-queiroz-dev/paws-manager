package com.example.gestaoPetApi.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nomePet;

    @Enumerated(EnumType.STRING)
    private PetTipo petTipo;

    @Enumerated(EnumType.STRING)
    private PetSexo petSexo;

    private Integer idade;
    private BigDecimal peso;
    private String raca;

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private Tutor tutor;

    public Pet() {
    }

    public Pet(long id, String nomePet, PetTipo petTipo, PetSexo petSexo, Integer idade, BigDecimal peso, String raca, Tutor tutor) {
        this.id = id;
        this.nomePet = nomePet;
        this.petTipo = petTipo;
        this.petSexo = petSexo;
        this.idade = idade;
        this.peso = peso;
        this.raca = raca;
        this.tutor = tutor;
    }

    public long getId() {
        return id;
    }

      public String getNomePet() {
        return nomePet;
    }

    public void setNomePet(String nomePet) {
        this.nomePet = nomePet;
    }

    public PetTipo getPetTipo() {
        return petTipo;
    }

    public void setPetTipo(PetTipo petTipo) {
        this.petTipo = petTipo;
    }

    public PetSexo getPetSexo() {
        return petSexo;
    }

    public void setPetSexo(PetSexo petSexo) {
        this.petSexo = petSexo;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }
}
