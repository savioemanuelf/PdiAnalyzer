package com.pdianalyzer.domain.DTO;

import java.util.UUID;

public record NivelResponseDto (
  UUID id,
  String senioridade,
  int ordem,
  UUID trilhaId,
  String competenciasTecnicas,
  String competenciasPessoais
){}
