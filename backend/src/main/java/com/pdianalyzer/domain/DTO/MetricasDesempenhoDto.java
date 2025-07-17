package com.pdianalyzer.domain.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record MetricasDesempenhoDto(
  @NotNull(message = "O ID do colaborador é obrigatório")
  UUID colaboradorId,

  @NotBlank(message = "As competências técnicas devem ser informadas")
  String competenciasTecnicas,

  @NotBlank(message = "As competências pessoais devem ser informadas")
  String competenciasPessoais
) {}
