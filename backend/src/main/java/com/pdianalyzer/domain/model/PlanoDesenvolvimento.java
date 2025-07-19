package com.pdianalyzer.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "plano_desenvolvimento")
@Getter
@Setter
@NoArgsConstructor
public class PlanoDesenvolvimento {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(columnDefinition = "TEXT")
  private String objetivo;

  @Column(columnDefinition = "TEXT")
  private String acao;

  private LocalDate prazoConclusao;

  @Column(columnDefinition = "TEXT")
  private String indicadorSucesso;

  @Column(columnDefinition = "TEXT")
  private String recursoResponsavel;

}
