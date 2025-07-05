package com.pdianalyzer.domain.model;

import com.smarthirepro.domain.model.Candidato;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Colaborador extends Candidato {

    private UUID empresaId;
    private String departamentoAtual;
    private String cargoAtual;
}