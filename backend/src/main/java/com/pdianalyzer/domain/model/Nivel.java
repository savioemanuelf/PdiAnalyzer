package com.pdianalyzer.domain.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Table(name = "nivel")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Nivel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    private String senioridade;
    private int ordem;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cargo_competencias_id")
    private CargoCompetencias cargoCompetencias;

    @OneToOne
    @JoinColumn(name = "proximo_nivel_id")
    private Nivel proximoNivel;

    @ManyToOne
    @JoinColumn(name = "trilha_de_carreira_id")
    private TrilhaDeCarreira trilhaDeCarreira;

  }

