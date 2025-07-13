package com.pdianalyzer.domain.model;

import com.smarthirepro.domain.model.Curriculo;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MetricasDesempenho {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @OneToOne(optional = false)
  @JoinColumn(name = "colaborador_id")
  private Colaborador colaborador;

  @Column(name = "competencias_tecnicas")
  private String competenciasTecnicas;

  @Column(name = "competencias_pessoais")
  private String competenciasPessoais;

  @Column(name = "pontos_melhoria")
  private String pontosMelhoria;
}
