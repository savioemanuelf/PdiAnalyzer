package com.pdianalyzer.domain.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record NivelDto (
  @NotBlank(message = "Para cada nível deve haver uma senioridade associada") String senioridade,
  @NotNull(message = "Para cada senioridade deve haver uma ordem do nível") int ordem,
  @NotBlank(message = "Para cada nível devem haver competencias associadas") String competenciasTecnicas,
  @NotBlank(message = "Para cada nível devem haver competencias associadas") String competenciasPessoais,
  String habilidades,
  String formacaoAcademica,
  String experiencia,
  String idiomas,
  UUID proximoNivelId,
  UUID trilhaDeCarreiraId
){}
