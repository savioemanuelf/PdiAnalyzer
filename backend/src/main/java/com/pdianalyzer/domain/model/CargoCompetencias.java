package com.pdianalyzer.domain.model;

import com.smarthirepro.domain.model.CargoCompetenciasGenerico;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cargo_habilidades")
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("PDI")
public class CargoCompetencias extends CargoCompetenciasGenerico {

  @Column(name = "competencias_tecnicas")
  private String competenciasTecnicas;

  @Column(name = "competencias_pessoais")
  private String competenciasPessoais;

  @Column(name = "certificacoes")
  private String certificacoes;
}
