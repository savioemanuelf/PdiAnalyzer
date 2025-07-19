package com.pdianalyzer.domain.DTO;

import com.smarthirepro.core.dto.AvaliacaoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlanoDesenvolvimentoDto extends AvaliacaoDto {
  private String objetivo;          // Objetivo principal de desenvolvimento (SMART)
  private String acao;              // Ação prática que será realizada (ex: curso, projeto)
  private String prazoConclusao;    // Prazo para concluir a ação (ex: "2025-09-30")
  private String indicadorSucesso;  // Como o sucesso será medido (ex: certificado, entrega)
  private String recursoResponsavel; // Recursos e responsáveis (ex: "RH fornecerá curso", ou "Gestor orientará")

}
